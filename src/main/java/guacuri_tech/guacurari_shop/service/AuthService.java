package guacuri_tech.guacurari_shop.service;

import guacuri_tech.guacurari_shop.dto.request.RecoverPasswordDTO;
import guacuri_tech.guacurari_shop.entity.UserEntity;
import guacuri_tech.guacurari_shop.model.Register;
import guacuri_tech.guacurari_shop.repository.UserRepository;
import guacuri_tech.guacurari_shop.security.jwt.JwtService;
import guacuri_tech.guacurari_shop.exception.EmailAlreadyExistsException;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletResponse;

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


    public void recoverPassword(@Valid RecoverPasswordDTO recoverPasswordDTO, HttpServletResponse response) {
        String email = recoverPasswordDTO.getEmail();

        // Buscar el usuario por email
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email no registrado"));

        // 1. Generar token JWT para recuperación
        String recoveryToken = jwtService.generateToken(user.getEmail(), user.getRole().name());
        System.out.println("Recovery Token: " + recoveryToken);
        // 2. Guardar el token en el usuario (esto se hace en la base de datos)
        user.setRecoveryToken(recoveryToken);

        userRepository.save(user);

        // 3. Configurar la cookie con el token de recuperación
        Cookie cookie = new Cookie("recovery_token", recoveryToken);
        cookie.setHttpOnly(false); // Es importante para que solo pueda ser accedido por el backend
        cookie.setSecure(false);  // En desarrollo no es necesario usar Secure
        cookie.setPath("/");      // El path puede ser ajustado según necesites
        cookie.setMaxAge(15 * 60); // 15 minutos de vida útil
        response.addCookie(cookie);

        // 4. Enviar correo con el link de recuperación (sin el token en la URL)
        String recoveryLink = "http://localhost:4200/auth/resetear-contrasena?id=" + user.getId();

        try {
            emailService.sendRecoveryEmail(user.getEmail(), user.getUsername(), recoveryLink);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage(), e);
        }
    }

}