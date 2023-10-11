package com.betrybe.agrix.evaluation;

import static com.betrybe.agrix.evaluation.util.TestHelpers.objectToJson;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.betrybe.agrix.evaluation.mock.MockPerson;
import com.betrybe.agrix.evaluation.mock.PersonFixtures;
import com.betrybe.agrix.evaluation.util.SimpleResultHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Req 04-06")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Execution(ExecutionMode.CONCURRENT)
public class AuthorizationTest {

  MockMvc mockMvc;

  @Autowired
  WebApplicationContext wac;

  @Autowired
  ObjectMapper objectMapper;

  @BeforeEach
  public void setup() throws Exception {
    // We need this to make sure the response body is in UTF-8,
    // since we're testing raw strings
    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(wac)
        .apply(springSecurity())
        .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
        .alwaysDo(new SimpleResultHandler())
        .build();
  }

  @Test
  @DisplayName("4- Limitar acesso à rota GET /farms")
  void testFarmsAuthorization() throws Exception {
    String url = "/farms";

    String token = null;
    checkGetAuthorization(url, token, HttpStatus.FORBIDDEN);

    token = createPersonAndAuthenticate(PersonFixtures.person_user);
    checkGetAuthorization(url, token, HttpStatus.OK);

    token = createPersonAndAuthenticate(PersonFixtures.person_manager);
    checkGetAuthorization(url, token, HttpStatus.OK);

    token = createPersonAndAuthenticate(PersonFixtures.person_admin);
    checkGetAuthorization(url, token, HttpStatus.OK);
  }


  @Test
  @DisplayName("5- Limitar acesso à rota GET /crops")
  void testCropsAuthorization() throws Exception {
    String url = "/crops";

    String token = null;
    checkGetAuthorization(url, token, HttpStatus.FORBIDDEN);

    token = createPersonAndAuthenticate(PersonFixtures.person_user);
    checkGetAuthorization(url, token, HttpStatus.FORBIDDEN);

    token = createPersonAndAuthenticate(PersonFixtures.person_manager);
    checkGetAuthorization(url, token, HttpStatus.OK);

    token = createPersonAndAuthenticate(PersonFixtures.person_admin);
    checkGetAuthorization(url, token, HttpStatus.OK);
  }


  @Test
  @DisplayName("6- Limitar acesso à rota GET /fertilizers")
  void testFertilizersAuthorization() throws Exception {
    String url = "/fertilizers";

    String token = null;
    checkGetAuthorization(url, token, HttpStatus.FORBIDDEN);

    token = createPersonAndAuthenticate(PersonFixtures.person_user);
    checkGetAuthorization(url, token, HttpStatus.FORBIDDEN);

    token = createPersonAndAuthenticate(PersonFixtures.person_manager);
    checkGetAuthorization(url, token, HttpStatus.FORBIDDEN);

    token = createPersonAndAuthenticate(PersonFixtures.person_admin);
    checkGetAuthorization(url, token, HttpStatus.OK);
  }

  private void checkGetAuthorization(String url, String token, HttpStatus expectedStatus)
      throws Exception {
    MockHttpServletRequestBuilder builder = get(url);

    if (token != null) {
      builder = builder.header("Authorization", "Bearer " + token);
    }

    mockMvc.perform(builder.accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is(expectedStatus.value()));
  }


  private boolean isJwt(String token) {
    if (token == null || token.isEmpty()) {
      return false;
    }

    String[] parts = token.split("\\.");

    return parts.length == 3;
  }

  private String createPersonAndAuthenticate(MockPerson person) throws Exception {
    performPersonCreation(person);

    Map<String, Object> loginInfo = Map.of(
        "username", person.get("username"),
        "password", person.get("password")
    );

    String responseContent =
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(loginInfo)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn().getResponse().getContentAsString();

    LoginResponse loginResponse = objectMapper.readValue(responseContent, LoginResponse.class);
    String token = loginResponse.get("token");

    assertTrue(
        isJwt(token),
        "Resposta da autenticação deve incluir um token JWT válido!"
    );

    return token;
  }

  /**
   * Auxiliar method to create persons.
   */
  private MockPerson performPersonCreation(MockPerson person) throws Exception {
    String url = "/persons";

    String responseContent =
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(person)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn().getResponse().getContentAsString();

    return objectMapper.readValue(responseContent, MockPerson.class);
  }

  private static class LoginResponse extends HashMap<String, String> {

    public <K, V> LoginResponse() {
      super();
    }

    public <K, V> LoginResponse(Map<K, V> source) {
      super((Map<String, String>) source);
    }

    public LoginResponse clone() {
      return new LoginResponse(this);
    }
  }
}

