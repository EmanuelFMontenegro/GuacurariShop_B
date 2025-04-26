package guacuri_tech.guacurari_shop.controller;

import guacuri_tech.guacurari_shop.entity.ListaPrecio;
import guacuri_tech.guacurari_shop.service.ListaPrecioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lista-precios")
public class ListaPrecioController {

    @Autowired
    private ListaPrecioService listaPrecioService;

    @GetMapping
    public List<ListaPrecio> getAll() {
        return listaPrecioService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListaPrecio> getById(@PathVariable Long id) {
        return listaPrecioService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ListaPrecio create(@RequestBody ListaPrecio listaPrecio) {
        return listaPrecioService.save(listaPrecio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListaPrecio> update(@PathVariable Long id, @RequestBody ListaPrecio listaPrecio) {
        return listaPrecioService.getById(id).map(existing -> {
            listaPrecio.setId(id);
            return ResponseEntity.ok(listaPrecioService.save(listaPrecio));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (listaPrecioService.getById(id).isPresent()) {
            listaPrecioService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}