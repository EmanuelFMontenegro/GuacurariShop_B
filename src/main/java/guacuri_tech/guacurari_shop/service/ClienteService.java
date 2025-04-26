package guacuri_tech.guacurari_shop.service;

import guacuri_tech.guacurari_shop.entity.Cliente;
import guacuri_tech.guacurari_shop.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Page<Cliente> findAll(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    public Optional<Cliente> findById(UUID clienteId) {
        return clienteRepository.findById(clienteId);
    }

    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
}
