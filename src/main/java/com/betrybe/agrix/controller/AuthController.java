package com.betrybe.agrix.controller;


import com.betrybe.agrix.controller.dto.AuthDto;
import com.betrybe.agrix.controller.dto.TokenDto;
import com.betrybe.agrix.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Criar o AuthController com o mapping /auth e o atributo AuthenticationManager sendo recebido por
// injeção de dependências.

/**
 * The type Auth controller.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  // Injetamos o tokenService no AuthController. Após autenticar a pessoa no método login, usamos o
  // auth.getPrincipal para obter a entidade autenticada e salvamos uma variável fazendo o casting.
  // Geramos um token para ela com essa variável
  private final TokenService tokenService;

  /**
   * Instantiates a new Auth controller.
   *
   * @param authenticationManager the authentication manager
   * @param tokenService          the token service
   */
  @Autowired
  public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
    this.authenticationManager = authenticationManager;
    this.tokenService = tokenService;
  }

  /**
   * Login token dto.
   *
   * @param authDto the auth dto
   * @return the token dto
   */
  // Criar o POST /login no AuthController, recebendo o AuthDto no corpo da requisição. Usar a
  // classe com o nome mais longo do mundo (UsernamePasswordAuthenticationToken) e passar o username
  // e password recebidos como parâmetro. Pedir para o authenticationManager atenticar a pessoa com
  // o método .authenticate, o que retorna um objeto Authentication.
  @PostMapping("/login")
  public TokenDto login(@RequestBody AuthDto authDto) {
    UsernamePasswordAuthenticationToken usernamePassword =
        new UsernamePasswordAuthenticationToken(
            authDto.username(),
            authDto.password()
        );
    Authentication authentication = authenticationManager.authenticate(usernamePassword);
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String token = tokenService.generateToken(userDetails);
    return new TokenDto(token);
  }
}
