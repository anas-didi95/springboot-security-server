package com.anasdidi.security.domain.auth;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anasdidi.security.common.TestUtils;
import com.anasdidi.security.config.TokenProvider;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthHandlerTests {

  private final WebTestClient webTestClient;
  private final BCryptPasswordEncoder passwordEncoder;
  private final TokenProvider tokenProvider;
  private final UserRepository userRepository;

  @Autowired
  public AuthHandlerTests(WebTestClient webTestClient, BCryptPasswordEncoder passwordEncoder,
      TokenProvider tokenProvider, UserRepository userRepository) {
    this.webTestClient = webTestClient;
    this.passwordEncoder = passwordEncoder;
    this.tokenProvider = tokenProvider;
    this.userRepository = userRepository;
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testAuthLoginSuccess() {
    UserVO userVO = TestUtils.generateUserVO();
    String password = userVO.getPassword();
    userVO.setPassword(passwordEncoder.encode(password));

    UserVO vo = userRepository.save(userVO).block();
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("username", vo.getUsername());
    requestBody.put("password", password);
    ResponseSpec response = webTestClient.post().uri("/auth/login").accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON).bodyValue(requestBody).exchange();
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

    UserVO vo = userRepository.save(userVO).block();
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("username", vo.getUsername());
    requestBody.put("password", "password" + System.currentTimeMillis());
    ResponseSpec response = webTestClient.post().uri("/auth/login").accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON).bodyValue(requestBody).exchange();
    TestUtils.assertResponseError(response, HttpStatus.BAD_REQUEST, "E201", "Invalid credentials!",
        "Wrong username/password");
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testAuthCheckSuccess() {
    UserVO userVO = TestUtils.generateUserVO();
    List<String> permissionList = Arrays.asList("PERMISSION:" + System.currentTimeMillis());

    UserVO vo = userRepository.save(userVO).block();
    ResponseSpec response = TestUtils.doGet(webTestClient, "/auth/check",
        TestUtils.getAccessToken(tokenProvider, vo.getId(), permissionList));
    response.expectStatus().isEqualTo(HttpStatus.OK);

    Map<String, Object> responseBody = response.expectBody(Map.class).returnResult().getResponseBody();
    Assertions.assertEquals(userVO.getUsername(), responseBody.get("username"));
    Assertions.assertEquals(userVO.getFullName(), responseBody.get("fullName"));
  }

  @Test
  public void testAuthCheckUserNotFoundError() {
    String userId = "test" + System.currentTimeMillis();
    List<String> permissionList = Arrays.asList("ADMIN");

    ResponseSpec response = TestUtils.doGet(webTestClient, "/auth/check",
        TestUtils.getAccessToken(tokenProvider, userId, permissionList));
    TestUtils.assertResponseError(response, HttpStatus.BAD_REQUEST, "E202", "User not found!",
        "Failed to find user with id: " + userId);
  }
}
