package guacuri_tech.guacurari_shop.controller;

import guacuri_tech.guacurari_shop.entity.Producto;
import guacuri_tech.guacurari_shop.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> getAll() {
        return productoService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable UUID id) {
        return productoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Producto create(@RequestBody Producto producto) {
        return productoService.save(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> update(@PathVariable UUID id, @RequestBody Producto producto) {
        return productoService.getById(id).map(existing -> {
            producto.setProductoId(id);
            return ResponseEntity.ok(productoService.save(producto));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (productoService.getById(id).isPresent()) {
            productoService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
