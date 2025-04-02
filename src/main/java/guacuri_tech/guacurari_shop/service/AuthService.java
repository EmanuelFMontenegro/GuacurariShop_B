package guacuri_tech.guacurari_shop.service;

import guacuri_tech.guacurari_shop.dto.RecoverPasswordDTO;
import guacuri_tech.guacurari_shop.entity.UserEntity;
import guacuri_tech.guacurari_shop.model.Register;
import guacuri_tech.guacurari_shop.repository.UserRepository;
import guacuri_tech.guacurari_shop.security.jwt.JwtService;
import guacuri_tech.guacurari_shop.exception.EmailAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


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

    // Método de registro
    public void register(Register request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("El email ya está registrado.");
        }

        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
    }

    // Método de login
    public Map<String, Object> login(String email, String password) {
        // Autenticación del usuario
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));

        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        // Obtener el rol del usuario
        String role = userEntity.getRole().name();

        // Crear el mapa de claims adicionales
        Map<String, Object> extraClaims = Map.of("role", role);

        // Generar el token correctamente
        String token = jwtService.generateToken(userEntity, extraClaims);

        // Construir la respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", role);

        return response;
    }


    // Método para recuperar la contraseña
    public void recoverPassword(RecoverPasswordDTO recoverPasswordDTO) {
        String email = recoverPasswordDTO.getEmail();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email no registrado"));

        // Claims adicionales para el token
        Map<String, Object> extraClaims = Map.of("exp", System.currentTimeMillis() + (15 * 60 * 1000));

        // Generar el token correctamente
        String recoveryToken = jwtService.generateToken(user, extraClaims);

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
