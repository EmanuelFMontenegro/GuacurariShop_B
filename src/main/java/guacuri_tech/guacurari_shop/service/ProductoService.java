package guacuri_tech.guacurari_shop.service;

import guacuri_tech.guacurari_shop.entity.Producto;
import guacuri_tech.guacurari_shop.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> getAll() {
        return productoRepository.findAll();
    }

    public Optional<Producto> getById(UUID id) {
        return productoRepository.findById(id);
    }


    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public void delete(UUID id) {
        productoRepository.deleteById(id);
    }
}
