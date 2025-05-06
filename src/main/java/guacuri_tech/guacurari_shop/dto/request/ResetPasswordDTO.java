package guacuri_tech.guacurari_shop.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDTO {

    @NotBlank(message = "La nueva contraseña es requerida")
    private String newPassword;

    @NotBlank(message = "El token es requerido")
    private String token;
}
