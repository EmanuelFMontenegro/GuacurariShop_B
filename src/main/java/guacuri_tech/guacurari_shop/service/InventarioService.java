package guacuri_tech.guacurari_shop.service;

import guacuri_tech.guacurari_shop.entity.Inventario;
import guacuri_tech.guacurari_shop.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    public List<Inventario> getAll() {
        return inventarioRepository.findAll();
    }

    public Optional<Inventario> getById(Long id) {
        return inventarioRepository.findById(id);
    }

    public Inventario save(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    public void delete(Long id) {
        inventarioRepository.deleteById(id);
    }
}
