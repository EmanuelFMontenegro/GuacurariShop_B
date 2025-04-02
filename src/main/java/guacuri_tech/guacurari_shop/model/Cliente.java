package guacuri_tech.guacurari_shop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // ✅ Compatible con UUID
    @Column(name = "cliente_id")
    private UUID clienteId;

    @Email(message = "El formato del email es inválido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    private String telefono;
    private Short status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;  // ✅ Cambiado a LocalDateTime

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)  // Relación de 1 a N con Usuario
    private List<Usuario> usuarios;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();  // ✅ Asigna fecha automáticamente
    }

    // Getters y Setters
    public UUID getClienteId() {
        return clienteId;
    }

    public void setClienteId(UUID clienteId) {
        this.clienteId = clienteId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
