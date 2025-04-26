package guacuri_tech.guacurari_shop.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {
    // Getters y setters
    private String username;
    private String email;
    private String role;

    public UserDto() {}

    public UserDto(String username, String email, String role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
