package guacuri_tech.guacurari_shop.repository;

import guacuri_tech.guacurari_shop.entity.Rubro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RubroRepository extends JpaRepository<Rubro, UUID> {
    Optional<Rubro> findByNombre(String nombre);
}
