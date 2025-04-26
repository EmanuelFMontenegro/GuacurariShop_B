package guacuri_tech.guacurari_shop.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return isPublicEndpoint(request);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = jwtService.extractToken(request);

        try {
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            String username = jwtService.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(token)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                sendError(response, "Token inválido");
                return;
            }
        } catch (ExpiredJwtException ex) {
            log.warn("Token expirado: {}", ex.getMessage());
            sendError(response, "Token expirado");
            return;
        } catch (SignatureException ex) {
            log.error("Error de firma del token: {}", ex.getMessage());
            sendError(response, "Token con firma inválida");
            return;
        } catch (Exception ex) {
            log.error("Error de autenticación: {}", ex.getMessage());
            sendError(response, "Error de autenticación");
            return;
        }

        filterChain.doFilter(request, response);
    }




    private String getTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        return Arrays.stream(request.getCookies())
                .filter(cookie -> "token".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/auth/login")
                || path.startsWith("/auth/register")
                || path.startsWith("/rubros")
                || path.startsWith("/public")
                || path.equals("/error");

    }


    private boolean isProduction() {
        return "prod".equalsIgnoreCase(System.getenv("ENV"));
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        String json = String.format(
                "{\"error\": \"%s\", \"status\": 401, \"timestamp\": \"%s\"}",
                message,
                java.time.ZonedDateTime.now().toString()
        );
        response.getWriter().write(json);
    }

}