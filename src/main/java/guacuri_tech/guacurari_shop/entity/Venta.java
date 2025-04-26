package guacuri_tech.guacurari_shop.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venta_id")
    private Long id;

    @Column(name = "total")
    private Double total;

    @Column(name = "fecha")
    private String fecha;

    @ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "cliente_id")
    private Cliente cliente;
}