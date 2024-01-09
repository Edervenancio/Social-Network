package com.example.socialnetwork.social.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {


    // Realiza as restrições de URL
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{


        http.authorizeHttpRequests((requests) -> requests.requestMatchers("/hello").hasRole("USER")
                        .requestMatchers("/**").authenticated())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());
        return http.build();
    }


    // Responsável por obter os dados do BD dinamicamente.
    @Bean
    public UserDetailsService userDetailsService (DataSource dataSource){

        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);


        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select email, password, enabled from users where email=?"
        );

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select email, authority from authorities where email=?"
        );

        return jdbcUserDetailsManager;
    }


    // Não exige codificação da senha em primeira instancia
    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}
