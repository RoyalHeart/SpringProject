/*******************************************************************************
 * Class        ：
 * Created date ：2023/12/20
 * Lasted date  ：2023/12/20
 * Author       ：TamTH1
 * Change log   ：2023/12/20 01-00 TamTH1 create a new
******************************************************************************/
package com.example.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.example.service.impl.UserDetailsServiceImpl;

/**
 * class
 * 
 * @version 01-00
 * @since 01-00
 * @author TamTH1
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setHideUserNotFoundExceptions(false);
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new AuthenticationFailureHandlerImpl();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/login", "/signup").permitAll()
                        .requestMatchers("/edit/**", "/delete/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .httpBasic(withDefaults())
                .formLogin(login -> login.loginPage("/login")
                        .failureHandler(authenticationFailureHandler()).permitAll());
        return http.build();
    }

}