package guacuri_tech.guacurari_shop.controller;

import guacuri_tech.guacurari_shop.entity.Inventario;
import guacuri_tech.guacurari_shop.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventarios")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    public List<Inventario> getAll() {
        return inventarioService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> getById(@PathVariable Long id) {
        return inventarioService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Inventario create(@RequestBody Inventario inventario) {
        return inventarioService.save(inventario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventario> update(@PathVariable Long id, @RequestBody Inventario inventario) {
        return inventarioService.getById(id).map(existing -> {
            inventario.setId(id);
            return ResponseEntity.ok(inventarioService.save(inventario));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (inventarioService.getById(id).isPresent()) {
            inventarioService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}