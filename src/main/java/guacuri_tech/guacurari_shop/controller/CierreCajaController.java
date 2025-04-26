package guacuri_tech.guacurari_shop.controller;

import guacuri_tech.guacurari_shop.entity.CierreCaja;
import guacuri_tech.guacurari_shop.service.CierreCajaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cierres-caja")
public class CierreCajaController {

    @Autowired
    private CierreCajaService cierreCajaService;

    @GetMapping
    public List<CierreCaja> getAllCierres() {
        return cierreCajaService.getAllCierres();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CierreCaja> getCierreById(@PathVariable Long id) {
        Optional<CierreCaja> cierreCaja = cierreCajaService.getCierreById(id);
        return cierreCaja.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CierreCaja> createCierre(@Valid @RequestBody CierreCaja cierreCaja) {
        CierreCaja savedCierre = cierreCajaService.saveCierreCaja(cierreCaja);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCierre);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCierre(@PathVariable Long id) {
        cierreCajaService.deleteCierreCaja(id);
        return ResponseEntity.noContent().build();
    }
}