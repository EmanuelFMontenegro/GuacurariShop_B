package guacuri_tech.guacurari_shop.service;

import guacuri_tech.guacurari_shop.entity.ListaPrecio;
import guacuri_tech.guacurari_shop.repository.ListaPrecioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListaPrecioService {

    @Autowired
    private ListaPrecioRepository listaPrecioRepository;

    public List<ListaPrecio> getAll() {
        return listaPrecioRepository.findAll();
    }

    public Optional<ListaPrecio> getById(Long id) {
        return listaPrecioRepository.findById(id);
    }

    public ListaPrecio save(ListaPrecio listaPrecio) {
        return listaPrecioRepository.save(listaPrecio);
    }

    public void delete(Long id) {
        listaPrecioRepository.deleteById(id);
    }
}
