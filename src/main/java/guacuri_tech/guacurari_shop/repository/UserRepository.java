package guacuri_tech.guacurari_shop.repository;

import guacuri_tech.guacurari_shop.entity.UserEntity;  // Asegúrate de importar UserEntity
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // Buscar usuario por nombre de usuario (username)
    Optional<UserEntity> findByUsername(String username);

    // Verificar si un usuario ya existe por su nombre de usuario
    boolean existsByUsername(String username);

    // Buscar usuario por email (si es necesario en tu caso)
    @Query("SELECT u FROM UserEntity u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<UserEntity> findByEmail(String email);

    // Verificar si un usuario ya existe por su email
    boolean existsByEmail(String email);
}
