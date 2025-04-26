package guacuri_tech.guacurari_shop.service;

import guacuri_tech.guacurari_shop.entity.Proveedor;
import guacuri_tech.guacurari_shop.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    public List<Proveedor> getAllProveedores() {
        return proveedorRepository.findAll();
    }

    public Optional<Proveedor> getProveedorById(Long id) {
        return proveedorRepository.findById(id);
    }

    public Proveedor saveProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public void deleteProveedor(Long id) {
        proveedorRepository.deleteById(id);
    }
}
