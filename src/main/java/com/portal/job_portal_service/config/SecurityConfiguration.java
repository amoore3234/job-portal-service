package com.portal.job_portal_service.config;

import com.portal.job_portal_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(registry -> {
                registry.requestMatchers("service/portal/jobPostings", "service/portal/register", "service/portal/addJobPostings").permitAll();
                registry.requestMatchers(
                  "/portal/register",
                  "/portal/user/login",
                  "/portal/user/register",
                  "/css/**",
                  "/js/**",
                  "/swagger-ui.html",
                  "/swagger-ui/**",
                  "/v3/api-docs/**",
                  "/").permitAll();
                registry.anyRequest().authenticated();
            })
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
