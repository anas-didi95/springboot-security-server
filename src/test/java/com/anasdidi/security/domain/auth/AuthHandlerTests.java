package com.anasdidi.security.domain.auth;

import java.util.HashMap;
import java.util.Map;
import com.anasdidi.security.common.TestUtils;
import com.anasdidi.security.domain.user.UserRepository;
import com.anasdidi.security.domain.user.UserVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthHandlerTests {

  private final WebTestClient webTestClient;
  private final UserRepository userRepository;

  @Autowired
  public AuthHandlerTests(WebTestClient webTestClient, UserRepository userRepository) {
    this.webTestClient = webTestClient;
    this.userRepository = userRepository;
  }

  @Test
  public void testAuthLoginSuccess() {
    UserVO userVO = TestUtils.generateUserVO();

    ResponseSpec response = Mono.just(userRepository.save(userVO)).map(vo -> {
      Map<String, Object> requestBody = new HashMap<>();
      requestBody.put("username", vo.getUsername());
      requestBody.put("password", vo.getPassword());
      return requestBody;
    }).map(requestBody -> webTestClient.post().uri("/auth/login").accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON).bodyValue(requestBody).exchange()).block();
    response.expectStatus().isEqualTo(HttpStatus.OK);

    @SuppressWarnings("unchecked")
    Map<String, Object> responseBody =
        response.expectBody(Map.class).returnResult().getResponseBody();
    Assertions.assertNotNull(responseBody.get("accessToken"));
  }
}
