package guacuri_tech.guacurari_shop.model;

import lombok.Getter;

@Getter
public class Register {
    // Getters y Setters
    private String name;
    private String email;
    private String password;

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
