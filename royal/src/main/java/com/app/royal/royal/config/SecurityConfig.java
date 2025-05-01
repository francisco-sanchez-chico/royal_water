package com.app.royal.royal.config;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        // Gestión de usuarios: solo administrador
                        .requestMatchers("/api/usuarios/**").hasAuthority("ADMINISTRADOR")

                        // Categorías: solo administrador
                        .requestMatchers("/api/categorias/**").hasAuthority("ADMINISTRADOR")

                        // Productos:
                        .requestMatchers(HttpMethod.POST, "/api/productos").hasAuthority("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasAuthority("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/api/productos/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO")

                        // Movimientos: ambos pueden registrar y ver
                        .requestMatchers("/api/movimientos/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO")

                        // Defectos: ambos pueden reportar/ver
                        .requestMatchers("/api/defectos/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO")

                        // Por defecto: todo requiere autenticación
                        .anyRequest().authenticated()
                )
                .httpBasic();

        return http.build();
    }

}
