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
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .anyRequest().authenticated())
                .httpBasic(withDefaults())
                .formLogin(Customizer.withDefaults());
        return http.build();
    }

}