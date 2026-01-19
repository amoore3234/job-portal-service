package com.portal.job_portal_service.config;

import com.portal.job_portal_service.service.UserService;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private final UserService userService;

    @Bean
    public UserDetailsService userDetailsService() {
      return userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
      provider.setUserDetailsService(userService);
      provider.setPasswordEncoder(passwordEncoder());
      return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration config
    ) throws Exception {
      return config.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration config = new CorsConfiguration();

      config.setAllowedOrigins(List.of("http://localhost:5173"));
      config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
      config.setAllowedHeaders(List.of("*"));
      config.setAllowCredentials(true);

      UrlBasedCorsConfigurationSource source =
              new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", config);
      return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return httpSecurity
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(registry -> {
              registry.requestMatchers(
                "/service/portal/jobPostings",
                "/auth/register",
                "/auth/login",
                "/service/portal/addJobPostings").permitAll();
              registry.requestMatchers(
                "/css/**",
                "/js/**",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/").permitAll();
              registry.anyRequest().authenticated();
            })
            .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class)
            .oauth2Login(oauth2Login -> {
              oauth2Login.loginPage("/portal/user/login").permitAll();
            })
            .formLogin(httpForm -> {
                httpForm
                    .loginPage("/portal/user/login")
                    .loginProcessingUrl("/portal/user/login")
                    .defaultSuccessUrl("/portal/user/home", true)
                    .permitAll();
            })
            .logout(logout -> {
              logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/portal/user/login")
                .invalidateHttpSession(true)
                .permitAll();
            })
            .build();
    }
}
