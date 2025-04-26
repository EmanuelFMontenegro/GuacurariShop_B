package guacuri_tech.guacurari_shop.service;

import guacuri_tech.guacurari_shop.entity.Caja;
import guacuri_tech.guacurari_shop.repository.CajaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CajaService {

    @Autowired
    private CajaRepository cajaRepository;

    public List<Caja> getAllCajas() {
        return cajaRepository.findAll();
    }

    public Optional<Caja> getCajaById(Long id) {
        return cajaRepository.findById(id);
    }

    public Caja abrirCaja(Caja caja) {
        caja.setApertura(LocalDateTime.now());
        caja.setEstado("abierta");
        caja.setSaldoActual(caja.getSaldoInicial()); // Se iguala al iniciar
        return cajaRepository.save(caja);
    }

    public Caja cerrarCaja(Long id, Double totalFinal) {
        Optional<Caja> cajaOpt = cajaRepository.findById(id);
        if (cajaOpt.isEmpty()) {
            throw new RuntimeException("Caja no encontrada");
        }
        Caja caja = cajaOpt.get();
        caja.setEstado("cerrada");
        caja.setTotalFinal(totalFinal);
        caja.setCierre(LocalDateTime.now()); // Setea el cierre
        return cajaRepository.save(caja);
    }

    public Caja saveCaja(Caja caja) {
        return cajaRepository.save(caja);
    }

    public void deleteCaja(Long id) {
        cajaRepository.deleteById(id);
    }
}
