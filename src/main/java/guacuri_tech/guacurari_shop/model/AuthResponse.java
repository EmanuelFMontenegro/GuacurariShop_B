package guacuri_tech.guacurari_shop.model;

public class AuthResponse {
    private String token;
    private String expiration;
    private String result;
    private String status;

    public AuthResponse(String token, String expiration, String result, String status) {
        this.token = token;
        this.expiration = expiration;
        this.result = result;
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
