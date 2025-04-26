package guacuri_tech.guacurari_shop.service;

import guacuri_tech.guacurari_shop.dto.request.RecoverPasswordDTO;
import guacuri_tech.guacurari_shop.entity.UserEntity;
import guacuri_tech.guacurari_shop.model.Register;
import guacuri_tech.guacurari_shop.repository.UserRepository;
import guacuri_tech.guacurari_shop.security.jwt.JwtService;
import guacuri_tech.guacurari_shop.exception.EmailAlreadyExistsException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;


    public void register(Register request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("El email ya está registrado.");
        }

        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
    }

    public Map<String, Object> login(String email, String password) {
       Optional<UserEntity> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        UserEntity userEntity = optionalUser.get();
        boolean match = passwordEncoder.matches(password, userEntity.getPassword());

        if (!match) {
            throw new BadCredentialsException("Credenciales inválidas");
        }
        String role = userEntity.getRole().name();
        String token = jwtService.generateToken(userEntity.getEmail(), role);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", role);

        return response;
    }
    public void recoverPassword(@Valid RecoverPasswordDTO recoverPasswordDTO) {
        String email = recoverPasswordDTO.getEmail();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email no registrado"));


        Map<String, Object> extraClaims = Map.of("exp", System.currentTimeMillis() + (15 * 60 * 1000));


        String recoveryToken = jwtService.generateToken(user.getEmail(), user.getRole().name());



        user.setRecoveryToken(recoveryToken);
        userRepository.save(user);

        String recoveryLink = "http://localhost:4200/auth/resetear-contrasena?token=" + recoveryToken;

        try {
            emailService.sendRecoveryEmail(user.getEmail(), user.getUsername(), recoveryLink);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage(), e);
        }
    }
}
