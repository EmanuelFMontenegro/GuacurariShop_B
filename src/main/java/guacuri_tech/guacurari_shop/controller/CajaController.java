package guacuri_tech.guacurari_shop.controller;

import guacuri_tech.guacurari_shop.entity.Caja;
import guacuri_tech.guacurari_shop.service.CajaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cajas")
public class CajaController {

    @Autowired
    private CajaService cajaService;

    @GetMapping
    public ResponseEntity<List<Caja>> getAllCajas() {
        return ResponseEntity.ok(cajaService.getAllCajas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Caja> getCajaById(@PathVariable Long id) {
        return cajaService.getCajaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/abrir")
    public ResponseEntity<Caja> abrirCaja(@Valid @RequestBody Caja caja) {
        caja.setEstado("abierta");
        caja.setApertura(LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
        Caja nuevaCaja = cajaService.saveCaja(caja);
        return ResponseEntity.status(201).body(nuevaCaja);
    }

    @PutMapping("/cerrar/{id}")
    public ResponseEntity<Caja> cerrarCaja(@PathVariable Long id, @RequestBody Caja cajaData) {
        Optional<Caja> cajaOpt = cajaService.getCajaById(id);
        if (cajaOpt.isEmpty()) return ResponseEntity.notFound().build();

        Caja caja = cajaOpt.get();
        caja.setEstado("cerrada");
        caja.setTotalFinal(cajaData.getTotalFinal());
        Caja actualizada = cajaService.saveCaja(caja);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCaja(@PathVariable Long id) {
        if (cajaService.getCajaById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        cajaService.deleteCaja(id);
        return ResponseEntity.noContent().build();
    }
}