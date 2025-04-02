package guacuri_tech.guacurari_shop.controller;

import guacuri_tech.guacurari_shop.dto.ResetPasswordDTO;
import guacuri_tech.guacurari_shop.dto.UsuarioRegisterDTO;
import guacuri_tech.guacurari_shop.dto.UsuarioLoginDTO;
import guacuri_tech.guacurari_shop.dto.RecoverPasswordDTO;
import guacuri_tech.guacurari_shop.entity.UserEntity;
import guacuri_tech.guacurari_shop.model.AuthResponse;
import guacuri_tech.guacurari_shop.repository.UserRepository;
import guacuri_tech.guacurari_shop.service.AuthService;
import guacuri_tech.guacurari_shop.service.EmailService;
import guacuri_tech.guacurari_shop.service.UserRegistrationService;
import guacuri_tech.guacurari_shop.security.jwt.JwtService;
import guacuri_tech.guacurari_shop.service.CustomUserDetailsService;
import guacuri_tech.guacurari_shop.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UsuarioLoginDTO loginDTO, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );

            UserDetails user = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(user);

            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();


            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60);
            cookie.setSecure(false);
            response.addCookie(cookie);

            return ResponseEntity.ok(new AuthResponse(token, "24h", "Login exitoso", "success"));
        } catch (BadCredentialsException e) {
            throw e;
        }
    }
    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no proporcionado");
        }
        try {
            if (!jwtService.isTokenValid(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o expirado");
            }

            String email = jwtService.extractUsername(token);
            List<String> roles = jwtService.extractRoles(token);

            System.out.println("Roles del usuario: " + roles);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "email", email,
                    "roles", roles
            ));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
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
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UsuarioRegisterDTO userDTO) {
        try {
            if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty()) {
                return ResponseEntity.badRequest().body("El email es obligatorio.");
            }
            if (userDTO.getUsername() == null || userDTO.getUsername().isEmpty()) {
                return ResponseEntity.badRequest().body("El nombre de usuario es obligatorio.");
            }
            if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body("La contraseña es obligatoria.");
            }

            userRegistrationService.registerNewUser(userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword(), userDTO.getRole());

            return ResponseEntity.ok("Usuario registrado exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
        return ResponseEntity.ok("Logout exitoso");
    }

    @PostMapping("/recover-password")
    public ResponseEntity<?> recoverPassword(@Valid @RequestBody RecoverPasswordDTO dto) {
        try {
            authService.recoverPassword(dto);
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
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO resetRequest) {
        try {
            if (!jwtService.isTokenValid(resetRequest.getToken())) {
                return ResponseEntity.badRequest().body("Token inválido o expirado");
            }

            String email = jwtService.extractUsername(resetRequest.getToken());
            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            user.setPassword(passwordEncoder.encode(resetRequest.getNewPassword()));
            user.setRecoveryToken(null);
            userRepository.save(user);

            return ResponseEntity.ok("Contraseña restablecida exitosamente");

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no existe");
        } catch (JwtException e) {
            return ResponseEntity.badRequest().body("Token inválido");
        }
    }
}
