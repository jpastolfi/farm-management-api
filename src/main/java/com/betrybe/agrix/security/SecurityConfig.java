package com.betrybe.agrix.security;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * The type Security config.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private final SecurityFilter securityFilter;

  @Autowired
  public SecurityConfig(SecurityFilter securityFilter) {
    this.securityFilter = securityFilter;
  }

  /**
   * Security filter chain security filter chain.
   *
   * @param httpSecurity the http security
   * @return the security filter chain
   * @throws Exception the exception
   */

  // Criar o filtro base por onde as requisições vão passar. Desabilitar CSRF e
  // tornar a aplicação stateless

  // No securityFilterChain dentro do SecurityConfig, configuramos as autenticações com
  // authorizeHttpRequest e adicionamos o filtro criado (IoD) com addFilterBefore antes do
  // UsernamePasswordAuthenticationFilter. Se estivermos usando H2, também adicionamos a permissão
  // para o console.

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.POST, "/persons").permitAll()
            .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
            .requestMatchers(toH2Console()).permitAll()
            .anyRequest().authenticated())
        .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin))
        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  // Avisar o Spring Security qual foi o codificador usado para criptografar a senha. Fazer isso com
  // o método passwordEncoder dentro da classe SecurityConfig. Não esquecer que o tipo de retorno é
  // PasswordEncoder, da notação @Bean e do retorno BCryptPasswordEncoder().

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // Criar o @Bean AuthenticationManager no SecurityConfig que vai retornar
  // authenticationConfiguration.getAuthenticationManager.
  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration
  ) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}
