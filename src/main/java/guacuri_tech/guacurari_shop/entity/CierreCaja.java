package guacuri_tech.guacurari_shop.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "cierre_caja")
public class CierreCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cierre_caja_id")
    private Long cierreCajaId;

    @ManyToOne
    @JoinColumn(name = "caja_id", referencedColumnName = "caja_id", nullable = false)
    private Caja caja; // Relación con la tabla Caja

    private LocalDateTime fecha;

    @Column(name = "total_ventas")
    private Double totalVentas;

    @Column(name = "total_pagado")
    private Double totalPagado;

    @Column(name = "saldo_final")
    private Double saldoFinal;

    @Column(name = "tipo_moneda")
    private String tipoMoneda;

    private String observaciones;

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}
