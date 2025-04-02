package guacuri_tech.guacurari_shop.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/rubros")
public class RubroController {

    // Lista temporal para almacenar los rubros (esto puede ser reemplazado con una base de datos)
    private List<String> rubros = new ArrayList<>();

    // Método GET para obtener todos los rubros
    @GetMapping
    public List<String> getAllRubros() {
        return rubros;  // Devuelve todos los rubros almacenados
    }

    // Método POST para agregar un nuevo rubro
    @PostMapping
    public String addRubro(@RequestBody String rubro) {
        rubros.add(rubro);  // Agrega el nuevo rubro a la lista
        return "Rubro '" + rubro + "' agregado exitosamente";
    }

    // Método para obtener un rubro específico por nombre
    @GetMapping("/{rubroName}")
    public String getRubro(@PathVariable String rubroName) {
        if (rubros.contains(rubroName)) {
            return "Rubro encontrado: " + rubroName;
        } else {
            return "Rubro no encontrado";
        }
    }
}
