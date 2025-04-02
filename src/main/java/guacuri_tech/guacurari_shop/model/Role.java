package guacuri_tech.guacurari_shop.model;

public enum Role {
    USER,
    ADMIN,
    SUPERADMIN;

    public String toUpperCase() {
        return this.name();
    }
}
