package com.electronic.store.Electronic_Store.Configurations;

import com.electronic.store.Electronic_Store.Entities.Role;
import com.electronic.store.Electronic_Store.Repositories.RoleRepository;
import com.electronic.store.Electronic_Store.Security.JwtAuthenticationEntryPoint;
import com.electronic.store.Electronic_Store.Security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity(debug = true)
@Configuration
@EnableMethodSecurity
public class SecurityConfig implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //It tells Spring Security not to create or use sessions. Every request must contain everything needed to authenticate (like a JWT token), because the server won’t remember anything between requests
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET,"/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/users/getProfileImage/**", "/users/search/**","/category","/category/*","/products","/products/**","/category/*/products","/orders/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/users/**","/users/email/**","/users/uploadProfile/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/orders/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/category","/category/**","/products","/products/**", "/category/*/products","/category/*/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/auth/login","/auth/token/refresh","/users").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/users/**","/category/**","/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/users/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/category/**","/products/**").hasRole("ADMIN")
                        .requestMatchers("/cart","/cart/**","/orders/*/status","/orders/user/**").hasRole("USER")
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/auth/**").authenticated()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex-> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))  //Return 401 if no/invalid token
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) throws Exception {
        Role existingAdminRole = roleRepository.findByRole("ROLE_ADMIN").orElse(null);
        if(existingAdminRole == null){
            existingAdminRole = new Role();
            existingAdminRole.setRole("ROLE_ADMIN");
            roleRepository.save(existingAdminRole);
            System.out.println("--ADMIN ROLE CREATED--");
        }
        Role existingUserRole = roleRepository.findByRole("ROLE_USER").orElse(null);
        if(existingUserRole == null){
            existingUserRole = new Role();
            existingUserRole.setRole("ROLE_USER");
            roleRepository.save(existingUserRole);
            System.out.println("--USER ROLE CREATED--");
        }
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
