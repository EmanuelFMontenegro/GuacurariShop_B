package guacuri_tech.guacurari_shop.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;


@Getter
@Entity
@Table(name = "rubros")
public class Rubro {

    // Getters y setters
    @Id
    @Column(name = "rubro_id")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nombre;

    // Constructor vacío (obligatorio para JPA)
    public Rubro() {}

    // Constructor con campos
    public Rubro(UUID id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
