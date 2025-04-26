package guacuri_tech.guacurari_shop.service;

import guacuri_tech.guacurari_shop.entity.Rubro;
import guacuri_tech.guacurari_shop.repository.RubroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RubroService {

    private final RubroRepository rubroRepository;

    public RubroService(RubroRepository rubroRepository) {
        this.rubroRepository = rubroRepository;
    }

    public List<Rubro> getAllRubros() {
        return rubroRepository.findAll();
    }

    public Rubro addRubro(String nombre) {
        Rubro rubro = new Rubro(UUID.randomUUID(), nombre);
        return rubroRepository.save(rubro);
    }

    public Optional<Rubro> getRubroByNombre(String nombre) {
        return rubroRepository.findByNombre(nombre);
    }
}
