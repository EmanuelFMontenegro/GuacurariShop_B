package guacuri_tech.guacurari_shop.repository;

import guacuri_tech.guacurari_shop.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByCliente_ClienteId(UUID clienteId);
}