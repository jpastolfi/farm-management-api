package com.betrybe.agrix.evaluation;

import static com.betrybe.agrix.evaluation.util.TestHelpers.objectToJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.betrybe.agrix.evaluation.mock.MockPerson;
import com.betrybe.agrix.evaluation.mock.PersonFixtures;
import com.betrybe.agrix.evaluation.util.SimpleResultHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
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
@DisplayName("Req 02")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Execution(ExecutionMode.CONCURRENT)
public class PersonManagementTest {

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
        .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
        .alwaysDo(new SimpleResultHandler())
        .build();
  }

  @Test
  @DisplayName("2- Crie a rota POST /persons")
  void testPersonCreation() throws Exception {
    MockPerson person = PersonFixtures.person_user;

    MockPerson savedPerson = performPersonCreation(person);

    assertNotNull(savedPerson.get("id"), "A resposta deve incluir o ID da pessoa criada");

    // Add id and remove password so that comparison makes sense
    MockPerson expectedPerson = new MockPerson(person);
    expectedPerson.put("id", savedPerson.get("id"));
    expectedPerson.remove("password");

    assertEquals(
        expectedPerson,
        savedPerson
    );
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
}