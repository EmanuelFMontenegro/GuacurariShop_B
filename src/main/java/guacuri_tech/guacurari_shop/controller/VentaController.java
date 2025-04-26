package guacuri_tech.guacurari_shop.controller;

import guacuri_tech.guacurari_shop.entity.Venta;
import guacuri_tech.guacurari_shop.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public List<Venta> getAll() {
        return ventaService.getAllVentas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> getById(@PathVariable Long id) {
        return ventaService.getVentaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Venta create(@RequestBody Venta venta) {
        return ventaService.saveVenta(venta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venta> update(@PathVariable Long id, @RequestBody Venta venta) {
        return ventaService.getVentaById(id).map(existing -> {
            venta.setId(id);
            return ResponseEntity.ok(ventaService.saveVenta(venta));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (ventaService.getVentaById(id).isPresent()) {
            ventaService.deleteVenta(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}