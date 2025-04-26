package guacuri_tech.guacurari_shop.controller;

import guacuri_tech.guacurari_shop.entity.Proveedor;
import guacuri_tech.guacurari_shop.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/proveedores")
public class ProveedorController {
    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public List<Proveedor> getAllProveedores() {
        return proveedorService.getAllProveedores();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> getProveedorById(@PathVariable Long id) {
        Optional<Proveedor> proveedor = proveedorService.getProveedorById(id);
        return proveedor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Proveedor> createProveedor(@Valid @RequestBody Proveedor proveedor) {
        Proveedor savedProveedor = proveedorService.saveProveedor(proveedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProveedor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProveedor(@PathVariable Long id) {
        proveedorService.deleteProveedor(id);
        return ResponseEntity.noContent().build();
    }
}