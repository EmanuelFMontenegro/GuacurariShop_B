package guacuri_tech.guacurari_shop.service;

import guacuri_tech.guacurari_shop.model.Role;
import guacuri_tech.guacurari_shop.entity.UserEntity;
import guacuri_tech.guacurari_shop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserRegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity registerNewUser(String username, String email, String password, Role role) {
        validateUserData(username, email, password, role);

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("El usuario ya existe.");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole(role);

        return userRepository.save(newUser);
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    private void validateUserData(String username, String email, String password, Role role) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }
        if (role == null) {
            throw new IllegalArgumentException("El rol es obligatorio.");
        }
    }
}
