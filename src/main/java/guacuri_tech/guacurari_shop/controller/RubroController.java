package guacuri_tech.guacurari_shop.controller;

import guacuri_tech.guacurari_shop.entity.Rubro;
import guacuri_tech.guacurari_shop.service.RubroService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rubros")
public class RubroController {

    private final RubroService rubroService;

    public RubroController(RubroService rubroService) {
        this.rubroService = rubroService;
    }

    @GetMapping
    public List<Rubro> getAllRubros() {
        return rubroService.getAllRubros();
    }

    @PostMapping
    public String addRubro(@RequestBody String nombre) {
        Rubro rubro = rubroService.addRubro(nombre);
        return "Rubro '" + rubro.getNombre() + "' agregado exitosamente con ID: " + rubro.getId();
    }

    @GetMapping("/{rubroName}")
    public String getRubro(@PathVariable String rubroName) {
        return rubroService.getRubroByNombre(rubroName)
                .map(rubro -> "Rubro encontrado: " + rubro.getNombre())
                .orElse("Rubro no encontrado");
    }
}
