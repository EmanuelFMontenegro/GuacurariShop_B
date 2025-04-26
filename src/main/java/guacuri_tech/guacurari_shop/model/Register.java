package guacuri_tech.guacurari_shop.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Register {
    private String name;
    private String email;
    private String password;
    private Role role;

}
