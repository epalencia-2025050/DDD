package com.eduardoemilio.KinalApp.config;

import com.eduardoemilio.KinalApp.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/login", "/registro").permitAll()
                        //XD XD XD XD XD XD
                        .requestMatchers("/clientes/eliminar/**",
                                "/productos/eliminar/**",
                                "/usuarios/eliminar/**",
                                "/ventas/eliminar/**",
                                "/detallesVentas/eliminar/**").hasRole("ADMIN")

                        .requestMatchers("/clientes/editar/**", "/clientes/actualizar/**",
                                "/productos/editar/**", "/productos/actualizar/**",
                                "/usuarios/editar/**", "/usuarios/actualizar/**",
                                "/ventas/editar/**", "/ventas/actualizar/**",
                                "/detallesVentas/editar/**", "/detallesVentas/actualizar/**").hasRole("ADMIN")

                        .requestMatchers("/clientes", "/clientes/nuevo", "/clientes/guardar", "/clientes/activos/**",
                                "/productos", "/productos/nuevo", "/productos/guardar", "/productos/activos/**",
                                "/usuarios", "/usuarios/nuevo", "/usuarios/guardar", "/usuarios/activos/**",
                                "/ventas", "/ventas/nuevo", "/ventas/guardar", "/ventas/activos/**", "/ventas/ver/**",
                                "/detallesVentas", "/detallesVentas/nuevo", "/detallesVentas/guardar",
                                "/menu", "/").hasAnyRole("ADMIN", "USER")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/menu", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return email -> {
            return usuarioRepository.findByEmail(email)
                    .map(usuario -> User.builder()
                            .username(usuario.getEmail())
                            .password(usuario.getPassword())
                            .roles(usuario.getRol())
                            .build())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}