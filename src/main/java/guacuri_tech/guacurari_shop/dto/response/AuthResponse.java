package guacuri_tech.guacurari_shop.dto.response;



import lombok.Setter;

import java.util.List;


@Setter
public class AuthResponse {
    private String token;
    private String expiration;
    private String result;
    private String status;
    private List<String> roles;

    public AuthResponse(String token, String expiration, String result, String status, List<String> roles) {
        this.token = token;
        this.expiration = expiration;
        this.result = result;
        this.status = status;
        this.roles = roles;
    }

}
