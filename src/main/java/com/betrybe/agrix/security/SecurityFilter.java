package com.betrybe.agrix.security;

// Criamos um filtro para lidar com o Jwt, um @Component que extende OncePerRequestFilter,
// implementando o método doFilterInternal. Vamos criar o método extractToken para extrair o token
// sem o bearer. No doFilterInternal, vamos obter o token, obter o username validando o token usando
// o tokenService (IoD), buscar o UserDetails pelo username usando a userService (IoD) e informar o
// Spring que essa pessoa está autenticada usando a classe com o maior nome do mundo passando três
// parâmetros: userDetails obtido na busca pelo usuário, null, que seriam as credentials mas são
// desnecessárias porque a pessoa já está logada, e os papeis da pessa (userDetails.getAuthorities).
// Colocamos o retorno desse método no SecurityContextHolder. Dizemos para o Spring continuar os
// filtros com o filterChain recebido por parâmetro no método.

import com.betrybe.agrix.service.PersonService;
import com.betrybe.agrix.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * The type Security filter.
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {
  private final TokenService tokenService;
  private final PersonService personService;

  /**
   * Instantiates a new Security filter.
   *
   * @param tokenService  the token service
   * @param personService the person service
   */
  @Autowired
  public SecurityFilter(TokenService tokenService, PersonService personService) {
    this.tokenService = tokenService;
    this.personService = personService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    Optional<String> token = extractToken(request);
    if (token.isPresent()) {
      String subject = tokenService.validadeToken(token.get());

      UserDetails userDetails = personService.loadUserByUsername(subject);

      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
          userDetails, null, userDetails.getAuthorities()
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }

  private Optional<String> extractToken(HttpServletRequest httpServletRequest) {
    String authHeader = httpServletRequest.getHeader("Authorization");
    if (authHeader == null) {
      return Optional.empty();
    }
    return Optional.of(
        authHeader.replace("Bearer ", "")
    );
  }
}
