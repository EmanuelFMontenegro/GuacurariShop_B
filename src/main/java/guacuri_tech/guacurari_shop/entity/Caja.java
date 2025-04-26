package guacuri_tech.guacurari_shop.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "caja")
public class Caja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "caja_id")
    private Long cajaId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    private String estado; // abierta, cerrada, etc.

    private Double saldoActual; // Agregado
    private Double saldoInicial; // Agregado

    @Column(name = "total_inicial")
    private Double totalInicial;

    @Column(name = "total_final")
    private Double totalFinal;

    private LocalDateTime apertura;
    private LocalDateTime cierre; // Agregado

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}
