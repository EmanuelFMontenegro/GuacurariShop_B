package guacuri_tech.guacurari_shop.service;

import guacuri_tech.guacurari_shop.entity.CierreCaja;
import guacuri_tech.guacurari_shop.repository.CierreCajaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CierreCajaService {

    @Autowired
    private CierreCajaRepository cierreCajaRepository;

    public List<CierreCaja> getAllCierres() {
        return cierreCajaRepository.findAll();
    }

    public Optional<CierreCaja> getCierreById(Long id) {
        return cierreCajaRepository.findById(id);
    }

    public CierreCaja saveCierreCaja(CierreCaja cierreCaja) {
        return cierreCajaRepository.save(cierreCaja);
    }

    public void deleteCierreCaja(Long id) {
        cierreCajaRepository.deleteById(id);
    }
}
