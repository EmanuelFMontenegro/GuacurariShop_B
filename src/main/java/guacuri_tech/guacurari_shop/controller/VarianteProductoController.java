package guacuri_tech.guacurari_shop.controller;

import guacuri_tech.guacurari_shop.entity.VarianteProducto;
import guacuri_tech.guacurari_shop.service.VarianteProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/variantes")
public class VarianteProductoController {
    @Autowired
    private VarianteProductoService varianteProductoService;

    @GetMapping
    public List<VarianteProducto> getAllVariantes() {
        return varianteProductoService.getAllVariantes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VarianteProducto> getVarianteById(@PathVariable Long id) {
        Optional<VarianteProducto> variante = varianteProductoService.getVarianteById(id);
        return variante.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<VarianteProducto> createVariante(@Valid @RequestBody VarianteProducto variante) {
        VarianteProducto savedVariante = varianteProductoService.saveVariante(variante);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVariante);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVariante(@PathVariable Long id) {
        varianteProductoService.deleteVariante(id);
        return ResponseEntity.noContent().build();
    }
}