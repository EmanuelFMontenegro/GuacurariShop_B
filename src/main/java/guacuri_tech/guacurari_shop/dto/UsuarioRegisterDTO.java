package guacuri_tech.guacurari_shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRegisterDTO {
    private String username;
    private String email;
    private String password;
    private String role;
}