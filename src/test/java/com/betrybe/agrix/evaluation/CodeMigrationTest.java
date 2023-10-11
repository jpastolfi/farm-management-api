package com.betrybe.agrix.evaluation;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Req 01")
class CodeMigrationTest {

  @Autowired
  @Qualifier("requestMappingHandlerMapping")
  RequestMappingHandlerMapping handlerMapping;

  @Test
  @DisplayName("1- Migre seu código da Fase B para este projeto (Fase C)")
  void testEndpointsExist() throws Exception {
    checkEndpoint("/farms", RequestMethod.GET);
    checkEndpoint("/crops", RequestMethod.GET);
    checkEndpoint("/fertilizers", RequestMethod.GET);
  }

  private void checkEndpoint(String path, RequestMethod method) {
    for (RequestMappingInfo info : handlerMapping.getHandlerMethods().keySet()) {
      PathPatternsRequestCondition patterns = info.getPathPatternsCondition();
      if (patterns == null) {
        continue;
      }

      long machingPatterns = patterns.getPatterns().stream().filter(
          (pattern) -> pattern.getPatternString().equals(path)
      ).count();

      if (machingPatterns > 0 &&
          info.getMethodsCondition().getMethods().contains(method)) {
        return;
      }
    }
    fail("Endpoint %s %s não encontrado!".formatted(method, path));
  }
}
