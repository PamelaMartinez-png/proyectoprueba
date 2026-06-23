package mx.ipn.upiiz.sirese.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad.
 *
 * - Expone BCryptPasswordEncoder para hashear contraseñas.
 * - Permite todas las rutas públicamente (la autenticación se maneja
 *   manualmente via /api/auth/login + sessionStorage en el frontend).
 *   En producción se reemplazaría por JWT o sesiones con Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
