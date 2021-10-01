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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthHandlerTests {

  private final WebTestClient webTestClient;
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  @Autowired
  public AuthHandlerTests(WebTestClient webTestClient, UserRepository userRepository,
      BCryptPasswordEncoder passwordEncoder) {
    this.webTestClient = webTestClient;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testAuthLoginSuccess() {
    UserVO userVO = TestUtils.generateUserVO();
    String password = userVO.getPassword();
    userVO.setPassword(passwordEncoder.encode(password));

    ResponseSpec response = Mono.just(userRepository.save(userVO)).map(vo -> {
      Map<String, Object> requestBody = new HashMap<>();
      requestBody.put("username", vo.getUsername());
      requestBody.put("password", password);
      return requestBody;
    }).map(requestBody -> webTestClient.post().uri("/auth/login").accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON).bodyValue(requestBody).exchange()).block();
    response.expectStatus().isEqualTo(HttpStatus.OK);
    Map<String, Object> responseBody = response.expectBody(Map.class).returnResult().getResponseBody();
    Assertions.assertNotNull(responseBody.get("accessToken"));

    String accessToken = (String) responseBody.get("accessToken");
    response = webTestClient.get().uri("/auth/check").accept(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + accessToken).exchange();
    response.expectStatus().isEqualTo(HttpStatus.OK);
  }
}
