package guacuri_tech.guacurari_shop.repository;

import guacuri_tech.guacurari_shop.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID> {}
