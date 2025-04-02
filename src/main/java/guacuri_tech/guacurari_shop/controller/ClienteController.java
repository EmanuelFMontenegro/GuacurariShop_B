package guacuri_tech.guacurari_shop.controller;

import guacuri_tech.guacurari_shop.model.Cliente;
import guacuri_tech.guacurari_shop.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Obtener todos los clientes con paginación
    @GetMapping
    public ResponseEntity<Page<Cliente>> getAllClientes(Pageable pageable) {
        Page<Cliente> clientes = clienteService.findAll(pageable);
        return ResponseEntity.ok(clientes);
    }

    // Crear un nuevo cliente
    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@Valid @RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.save(cliente);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    // Obtener un cliente por ID (UUID)
    @GetMapping("/{cliente_id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable UUID cliente_id) {
        Optional<Cliente> cliente = clienteService.findById(cliente_id);
        if (cliente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente.get());
    }
}
