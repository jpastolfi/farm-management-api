package com.betrybe.agrix.evaluation;

import static com.betrybe.agrix.evaluation.util.TestHelpers.objectToJson;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Req 03")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Execution(ExecutionMode.CONCURRENT)
public class AuthenticationTest {

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
  @DisplayName("3- Adicione autenticação no projeto")
  void testLogin() throws Exception {
    MockPerson person = PersonFixtures.person_user;
    performPersonCreation(person);

    testLoginFail(person);
    testLoginSuccess(person);
  }

  void testLoginFail(MockPerson person) throws Exception {
    Map<String, Object> loginInfo = Map.of(
        "username", person.get("username"),
        "password", "incorrectpassword"
    );

    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectToJson(loginInfo)))
        .andExpect(status().isForbidden());

    loginInfo = Map.of(
        "username", "nonexistingusername",
        "password", person.get("username")
    );

    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectToJson(loginInfo)))
        .andExpect(status().isForbidden());

  }

  void testLoginSuccess(MockPerson person) throws Exception {
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

    assertTrue(
        isJwt(loginResponse.get("token")),
        "Resposta da autenticação deve incluir um token JWT válido!"
    );
  }

  public boolean isJwt(String token) {
    if (token == null || token.isEmpty()) {
      return false;
    }

    String[] parts = token.split("\\.");

    return parts.length == 3;
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

