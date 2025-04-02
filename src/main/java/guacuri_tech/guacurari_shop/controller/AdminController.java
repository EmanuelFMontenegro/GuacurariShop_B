package guacuri_tech.guacurari_shop.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    // Acceso solo para ADMIN y SUPERADMIN
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    @GetMapping("/admin/dashboard")
    public String getAdminDashboard() {
        return "Bienvenido al dashboard de administración";
    }

    // Acceso solo para SUPERADMIN
    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/superadmin/settings")
    public String getSuperAdminSettings() {
        return "Configuraciones avanzadas para SUPERADMIN";
    }

    // Acceso solo para USER
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user/profile")
    public String getUserProfile() {
        return "Bienvenido al perfil de usuario";
    }
}
