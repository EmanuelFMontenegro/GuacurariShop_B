package guacuri_tech.guacurari_shop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "clientes")
public class Cliente {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cliente_id")
    private UUID clienteId;

    @Email(message = "El formato del email es inválido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    private String telefono;
    private Short status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<UserEntity> usuarios;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
