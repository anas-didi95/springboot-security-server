package com.anasdidi.security.domain.auth;

import java.util.HashMap;
import java.util.Map;

import com.anasdidi.security.common.TestUtils;
import com.anasdidi.security.repository.UserRepository;
import com.anasdidi.security.vo.UserVO;

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
  private final BCryptPasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Autowired
  public AuthHandlerTests(WebTestClient webTestClient, BCryptPasswordEncoder passwordEncoder,
      UserRepository userRepository) {
    this.webTestClient = webTestClient;
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
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

  @Test
  public void testAuthLoginRequestBodyEmptyError() {
    ResponseSpec response = TestUtils.doPost(webTestClient, "/auth/login", null);
    TestUtils.assertResponseError(response, HttpStatus.BAD_REQUEST, "E001", "Request body is empty!",
        "Required keys: username,password");
  }

  @Test
  public void testAuthLoginValidationError() {
    ResponseSpec response = TestUtils.doPost(webTestClient, "/auth/login", new HashMap<>());
    TestUtils.assertResponseError(response, HttpStatus.BAD_REQUEST, "E002", "Validation error!");
  }

  @Test
  public void testAuthLoginInvalidUsernameError() {
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("username", "username" + System.currentTimeMillis());
    requestBody.put("password", "password" + System.currentTimeMillis());

    ResponseSpec response = TestUtils.doPost(webTestClient, "/auth/login", requestBody);
    TestUtils.assertResponseError(response, HttpStatus.BAD_REQUEST, "E201", "Invalid credentials!",
        "Wrong username/password");
  }

  @Test
  public void testAuthLoginInvalidPasswordError() {
    UserVO userVO = TestUtils.generateUserVO();
    String password = userVO.getPassword();
    userVO.setPassword(passwordEncoder.encode(password));

    ResponseSpec response = Mono.just(userRepository.save(userVO)).map(vo -> {
      Map<String, Object> requestBody = new HashMap<>();
      requestBody.put("username", vo.getUsername());
      requestBody.put("password", "password" + System.currentTimeMillis());
      return requestBody;
    }).map(requestBody -> webTestClient.post().uri("/auth/login").accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON).bodyValue(requestBody).exchange()).block();
    TestUtils.assertResponseError(response, HttpStatus.BAD_REQUEST, "E201", "Invalid credentials!",
        "Wrong username/password");
  }
}
