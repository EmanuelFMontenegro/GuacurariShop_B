package guacuri_tech.guacurari_shop.service;

import guacuri_tech.guacurari_shop.entity.VarianteProducto;
import guacuri_tech.guacurari_shop.repository.VarianteProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VarianteProductoService {
    @Autowired
    private VarianteProductoRepository varianteProductoRepository;

    public List<VarianteProducto> getAllVariantes() {
        return varianteProductoRepository.findAll();
    }

    public Optional<VarianteProducto> getVarianteById(Long id) {
        return varianteProductoRepository.findById(id);
    }

    public VarianteProducto saveVariante(VarianteProducto variante) {
        return varianteProductoRepository.save(variante);
    }

    public void deleteVariante(Long id) {
        varianteProductoRepository.deleteById(id);
    }
}

