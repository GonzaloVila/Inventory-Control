package com.example.inventorycontrol.config;

import com.example.inventorycontrol.security.jwt.AuthEntryPointJwt;
import com.example.inventorycontrol.security.jwt.AuthTokenFilter;
import com.example.inventorycontrol.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMethodSecurity
public class SecurityConfig implements WebMvcConfigurer {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))

                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/**").permitAll() // Sigue permitiendo tus endpoints de API REST
                                .requestMatchers("/api/test/**").permitAll()
                                .requestMatchers("/css/**", "/js/**", "/images/**", "/vendor/**").permitAll()
                                .requestMatchers("/web/login", "/web/register").permitAll()
                                .requestMatchers("/", "/web").permitAll() // Para las redirecciones iniciales
                                .requestMatchers("/login").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                                .loginPage("/web/login") // Esta es la URL de la página de login
                                .loginProcessingUrl("/login") // URL a la que el formulario POSTea los datos
                                .defaultSuccessUrl("/web/dashboard", true) // URL a la que redirigir después de login exitoso
                                .failureUrl("/web/login?error") // URL a la que redirigir si el login falla
                )
                // Configuración de Logout
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL para la acción de logout
                        .logoutSuccessUrl("/web/login?logout") // URL a la que redirigir después de un logout exitoso
                        .permitAll() // Permitir acceso a la URL de logout
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/web/login");
        registry.addViewController("/web").setViewName("redirect:/web/login");
    }
}
