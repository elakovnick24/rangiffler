package com.elakov.rangiffler.config;

import com.elakov.rangiffler.cors.CorsCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
  private final CorsCustomizer corsCustomizer;
  private final Environment environment;

  @Autowired
  public SecurityConfig(CorsCustomizer corsCustomizer, Environment environment) {
    this.corsCustomizer = corsCustomizer;
    this.environment = environment;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    corsCustomizer.corsCustomizer(http);
    // Конфигурируем сам Spring Security
    // Конфигурируем авторизацию
    http.authorizeHttpRequests()
            .requestMatchers("/actuator/health").permitAll()
            .requestMatchers(HttpMethod.POST).permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .oauth2ResourceServer()
            .jwt();
    return http.build();
  }
}
