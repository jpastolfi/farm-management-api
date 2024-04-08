package com.betrybe.agrix.service;

// Configuramos a aplicação para usar JWT. Adicionamos a dependência e criamos o TokenService.
// Adicionamos o segredo do token no application.properties e puxamos usando @Value no construtor,
// criando o algoritmo. Criamos com os métodos generateToken (recebe UserDetails para o subject e
// retorna String), generateExpiryDate e validateToken (recebe token como String e retorna String).

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * The type Token service.
 */
@Service
public class TokenService {
  private final Algorithm algorithm;

  /**
   * Instantiates a new Token service.
   *
   * @param secret the secret
   */
  public TokenService(@Value("${api.security.token.secret}") String secret) {
    this.algorithm = Algorithm.HMAC256(secret);
  }

  /**
   * Generate token string.
   *
   * @param userDetails the user details
   * @return the string
   */
  public String generateToken(UserDetails userDetails) {
    return JWT
        .create()
        .withSubject(userDetails.getUsername())
        .withExpiresAt(generateExpiryDate())
        .sign(algorithm);
  }

  /**
   * Validade token string.
   *
   * @param token the token
   * @return the string
   */
  public String validadeToken(String token) {
    return JWT
        .require(algorithm)
        .build()
        .verify(token)
        .getSubject();
  }

  /**
   * Generate expiry date instant.
   *
   * @return the instant
   */
  public Instant generateExpiryDate() {
    return Instant.now().plus(2, ChronoUnit.HOURS);
  }
}
