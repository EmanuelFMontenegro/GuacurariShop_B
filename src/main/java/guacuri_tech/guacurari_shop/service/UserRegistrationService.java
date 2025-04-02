package guacuri_tech.guacurari_shop.service;

import guacuri_tech.guacurari_shop.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import guacuri_tech.guacurari_shop.entity.UserEntity;
import guacuri_tech.guacurari_shop.repository.UserRepository;

@Service
public class UserRegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity registerNewUser(String username, String email, String password, String role) {
        // Validar que los campos obligatorios no sean nulos ni vacíos
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }
        if (role == null || role.isEmpty()) {
            throw new IllegalArgumentException("El rol es obligatorio.");
        }

        // Verificar si el usuario o el email ya existen
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("El usuario ya existe.");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        // Crear el nuevo usuario
        UserEntity newUser = new UserEntity();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));

        // Convertir el String del rol a la enum `Role`
        try {
            newUser.setRole(Role.valueOf(role.toUpperCase()));  // Asegurar mayúsculas
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol no válido. Debe ser SUPERADMIN, ADMIN o USER.");
        }

        // Guardar en la base de datos
        return userRepository.save(newUser);
    }


}
