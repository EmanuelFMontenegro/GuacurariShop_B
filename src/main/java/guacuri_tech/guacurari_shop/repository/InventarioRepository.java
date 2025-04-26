package guacuri_tech.guacurari_shop.repository;

import guacuri_tech.guacurari_shop.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
}
