package guacuri_tech.guacurari_shop.controller;

import guacuri_tech.guacurari_shop.dto.request.RecoverPasswordDTO;
import guacuri_tech.guacurari_shop.dto.request.ResetPasswordDTO;
import guacuri_tech.guacurari_shop.dto.request.UsuarioLoginDTO;
import guacuri_tech.guacurari_shop.dto.request.UsuarioRegisterDTO;
import guacuri_tech.guacurari_shop.dto.response.ResponseMessage;
import guacuri_tech.guacurari_shop.entity.UserEntity;
import guacuri_tech.guacurari_shop.model.Role;
import guacuri_tech.guacurari_shop.repository.UserRepository;
import guacuri_tech.guacurari_shop.service.AuthService;
import guacuri_tech.guacurari_shop.service.EmailService;
import guacuri_tech.guacurari_shop.service.UserRegistrationService;
import guacuri_tech.guacurari_shop.security.jwt.JwtService;
import guacuri_tech.guacurari_shop.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${jwt.secret}")
    private String secret;

    private final AuthService authService;
    private final UserRegistrationService userRegistrationService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UsuarioLoginDTO loginDTO,
                                   HttpServletResponse response) {
        try {
            Map<String, Object> result = authService.login(loginDTO.getEmail(), loginDTO.getPassword());
            String token = (String) result.get("token");

            ResponseCookie cookie = ResponseCookie.from("auth_token", token)
                    .httpOnly(true)
                    .secure(false) // ✅ Debe estar en true en producción
                    .path("/")
                    .maxAge(Duration.ofDays(1))
                    .sameSite("Lax")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Map.of(
                            "status", "success",
                            "role", result.get("role"),
                            "email", loginDTO.getEmail()
                    ));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "status", "error",
                            "code", "INVALID_CREDENTIALS",
                            "message", "Combinación usuario/contraseña incorrecta"
                    ));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", "error",
                            "code", "USER_NOT_FOUND",
                            "message", "Usuario no registrado"
                    ));
        }
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("auth_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        try {
            if (userRepository.count() == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "status", "error",
                        "message", "No hay usuarios registrados en el sistema"
                ));
            }

            String token = extractToken(request);

            if (token == null || !jwtService.isTokenValid(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "status", "error",
                        "message", "Token inválido o no encontrado"
                ));
            }

            String email = jwtService.extractUsername(token);

            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            return ResponseEntity.ok(Map.of(
                    "email", user.getEmail(),
                    "role", user.getRole().name()
            ));

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Usuario no encontrado"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error al recuperar sesión"
            ));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody UsuarioRegisterDTO userDTO) {
        try {
            if (userDTO.getUsername() == null || userDTO.getUsername().isEmpty() ||
                    userDTO.getEmail() == null || userDTO.getEmail().isEmpty() ||
                    userDTO.getPassword() == null || userDTO.getPassword().isEmpty() ||
                    userDTO.getRole() == null) {
                return ResponseEntity.badRequest().body(new ResponseMessage("Todos los campos son obligatorios."));
            }

            userRegistrationService.registerNewUser(
                    userDTO.getUsername(),
                    userDTO.getEmail(),
                    userDTO.getPassword(),
                    userDTO.getRole()
            );

            return ResponseEntity.ok(new ResponseMessage("Usuario registrado exitosamente."));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("auth_token", "");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok("Logout exitoso");
    }

    @PostMapping("/recover-password")
    public ResponseEntity<?> recoverPassword(@Valid @RequestBody RecoverPasswordDTO dto,
                                             HttpServletResponse response) {
        try {
            authService.recoverPassword(dto, response);
            return ResponseEntity.ok().body(Map.of(
                    "status", "success",
                    "message", "Correo de recuperación enviado"
            ));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Email no registrado"
            ));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO resetRequest,
                                           @CookieValue(name = "recovery_token", required = false) String token,
                                           @RequestParam(required = false) String tokenParam) {
        try {
            String recoveryToken = (token != null) ? token : tokenParam;

            if (recoveryToken == null || !jwtService.isTokenValid(recoveryToken)) {
                return ResponseEntity.badRequest().body(new ResponseMessage("Token inválido o expirado"));
            }

            String email = jwtService.extractUsername(recoveryToken);

            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            if (!recoveryToken.equals(user.getRecoveryToken())) {
                return ResponseEntity.badRequest().body(new ResponseMessage("Token no coincide con el registrado"));
            }

            user.setPassword(passwordEncoder.encode(resetRequest.getNewPassword()));
            user.setRecoveryToken(null);
            userRepository.save(user);

            return ResponseEntity.ok(new ResponseMessage("Contraseña restablecida exitosamente"));

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage("Token expirado"));
        } catch (JwtException e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Token inválido"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Error inesperado: " + e.getMessage()));
        }
    }

    // Clase de respuesta que encapsula el mensaje
    public class ResponseMessage {
        private String message;

        public ResponseMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @PostMapping("/validate-reset-token")
    public ResponseEntity<?> validateResetToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        try {
            if (token == null || !jwtService.isTokenValid(token)) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Token inválido o expirado"
                ));
            }
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Token válido"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error al validar el token"
            ));
        }
    }
}
