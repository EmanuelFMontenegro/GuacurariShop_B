package guacuri_tech.guacurari_shop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecoverPasswordDTO {

    @Email(message = "El formato del correo electrónico es inválido")
    @NotBlank(message = "El correo electrónico es obligatorio")
    private String email;
}
