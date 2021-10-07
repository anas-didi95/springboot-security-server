package com.anasdidi.security.domain.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserHandlerTests {

  private final WebTestClient webTestClient;
  private final UserService userService;
  private final UserRepository userRepository;
  private final TokenProvider tokenProvider;
  private final BCryptPasswordEncoder passwordEncoder;

  @Autowired
  public UserHandlerTests(WebTestClient webTestClient, UserService userService, UserRepository userRepository,
      TokenProvider tokenProvider, BCryptPasswordEncoder passwordEncoder) {
    this.webTestClient = webTestClient;
    this.userService = userService;
    this.userRepository = userRepository;
    this.tokenProvider = tokenProvider;
    this.passwordEncoder = passwordEncoder;
  }

  private Map<String, Object> generateUserMap() {
    String suffix = "" + System.currentTimeMillis();
    Map<String, Object> map = new HashMap<>();
    map.put("username", "username" + suffix);
    map.put("password", "password" + suffix);
    map.put("fullName", "fullName" + suffix);
    map.put("email", "email" + suffix);
    return map;
  }

  private void assertVO(Map<String, Object> expected, UserVO actual) {
    Assertions.assertEquals(expected.get("username"), actual.getUsername());
    Assertions.assertTrue(passwordEncoder.matches((String) expected.get("password"), actual.getPassword()));
    Assertions.assertEquals(expected.get("fullName"), actual.getFullName());
    Assertions.assertEquals(expected.get("email"), actual.getEmail());
    Assertions.assertNotNull(actual.getVersion());
    Assertions.assertNotNull(actual.getLastModifiedDate());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testUserCreateSuccess() {
    Map<String, Object> requestBody = generateUserMap();

    ResponseSpec response = TestUtils.doPost(webTestClient, "/user", requestBody,
        TestUtils.getAccessToken(tokenProvider));
    response.expectStatus().isEqualTo(HttpStatus.CREATED);

    Map<String, Object> responseBody = response.expectBody(Map.class).returnResult().getResponseBody();
    Assertions.assertNotNull(responseBody.get("id"));

    String userId = (String) responseBody.get("id");
    Optional<UserVO> userVO = userRepository.findById(userId);
    if (userVO.isPresent()) {
      assertVO(requestBody, userVO.get());
    } else {
      Assertions.fail("User not found");
    }
  }

  @Test
  public void testUserCreateRequestBodyEmptyError() {
    ResponseSpec response = TestUtils.doPost(webTestClient, "/user", null, TestUtils.getAccessToken(tokenProvider));
    TestUtils.assertResponseError(response, HttpStatus.BAD_REQUEST, "E001", "Request body is empty!",
        "Required keys: username,password,fullName,email");
  }

  @Test
  public void testUserCreateValidationError() {
    Map<String, Object> requestBody = generateUserMap();
    requestBody.put("username", null);

    ResponseSpec response = TestUtils.doPost(webTestClient, "/user", requestBody,
        TestUtils.getAccessToken(tokenProvider));
    TestUtils.assertResponseError(response, HttpStatus.BAD_REQUEST, "E002", "Validation error!");
  }

  @Test
  public void testUserCreateServiceError() {
    Map<String, Object> requestBody = generateUserMap();

    ResponseSpec response = userService.create(UserDTO.fromMap(requestBody))
        .map(id -> TestUtils.doPost(webTestClient, "/user", requestBody, TestUtils.getAccessToken(tokenProvider)))
        .block();
    TestUtils.assertResponseError(response, HttpStatus.BAD_REQUEST, "E101", "User creation failed!");
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testUserUpdateSuccess() {
    Map<String, Object> userMap = generateUserMap();
    String suffix = "testUserUpdateSuccess";
    String fullName = "fullName" + suffix;
    String email = "email" + suffix;

    ResponseSpec response = userService.create(UserDTO.fromMap(userMap)).map(id -> {
      userMap.put("fullName", fullName);
      userMap.put("email", email);
      userMap.put("version", 0);

      return TestUtils.doPut(webTestClient, "/user/" + id, userMap, TestUtils.getAccessToken(tokenProvider));
    }).block();
    response.expectStatus().isEqualTo(HttpStatus.OK);

    Map<String, Object> responseBody = response.expectBody(Map.class).returnResult().getResponseBody();
    Assertions.assertNotNull(responseBody.get("id"));

    String userId = (String) responseBody.get("id");
    Optional<UserVO> result = userRepository.findById(userId);
    if (result.isPresent()) {
      assertVO(userMap, result.get());
    } else {
      Assertions.fail("User not found!");
    }
  }

  @Test
  public void testUserUpdateRequestBodyEmptyError() {
    Map<String, Object> userMap = generateUserMap();

    ResponseSpec response = userService.create(UserDTO.fromMap(userMap))
        .map(id -> TestUtils.doPut(webTestClient, "/user/" + id, null, TestUtils.getAccessToken(tokenProvider)))
        .block();
    TestUtils.assertResponseError(response, HttpStatus.BAD_REQUEST, "E001", "Request body is empty!",
        "Required keys: fullName,email");
  }

  @Test
  public void testUserUpdateValidationError() {
    Map<String, Object> userMap = generateUserMap();

    ResponseSpec response = userService.create(UserDTO.fromMap(userMap)).map(id -> {
      userMap.clear();
      return TestUtils.doPut(webTestClient, "/user/" + id, userMap, TestUtils.getAccessToken(tokenProvider));
    }).block();
    TestUtils.assertResponseError(response, HttpStatus.BAD_REQUEST, "E002", "Validation error!");
  }

  @Test
  public void testUserUpdateUserNotFoundError() {
    Map<String, Object> userMap = generateUserMap();

    ResponseSpec response = userService.create(UserDTO.fromMap(userMap)).map(id -> {
      userMap.put("version", 0);
      return TestUtils.doPut(webTestClient, "/user/" + System.currentTimeMillis(), userMap,
          TestUtils.getAccessToken(tokenProvider));
    }).block();
    TestUtils.assertResponseError(response, HttpStatus.BAD_REQUEST, "E102", "User not found!");
  }

  @Test
  public void testUserUpdateVersionNotMatchedError() {
    Map<String, Object> userMap = generateUserMap();

    ResponseSpec response = userService.create(UserDTO.fromMap(userMap)).map(id -> {
      userMap.put("version", -1);
      return TestUtils.doPut(webTestClient, "/user/" + id, userMap, TestUtils.getAccessToken(tokenProvider));
    }).block();
    TestUtils.assertResponseError(response, HttpStatus.BAD_REQUEST, "E103", "User version not matched!");
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testUserDeleteSuccess() {
    Map<String, Object> userMap = generateUserMap();

    ResponseSpec response = userService.create(UserDTO.fromMap(userMap))
        .map(id -> TestUtils.doDelete(webTestClient, "/user/" + id + "/0", TestUtils.getAccessToken(tokenProvider)))
        .block();
    response.expectStatus().isEqualTo(HttpStatus.OK);

    Map<String, Object> responseBody = response.expectBody(Map.class).returnResult().getResponseBody();
    Assertions.assertNotNull(responseBody.get("id"));

    String userId = (String) responseBody.get("id");
    Optional<UserVO> userVO = userRepository.findById(userId);
    Assertions.assertTrue(userVO.isEmpty());
  }

  @Test
  public void testUserDeleteUserNotFoundError() {
    Map<String, Object> userMap = generateUserMap();

    ResponseSpec response = userService.create(UserDTO.fromMap(userMap)).map(id -> TestUtils.doDelete(webTestClient,
        "/user/" + System.currentTimeMillis() + "/0", TestUtils.getAccessToken(tokenProvider))).block();
    TestUtils.assertResponseError(response, HttpStatus.BAD_REQUEST, "E102", "User not found!");
  }

  @Test
  public void testUserDeleteVersionNotMatchedError() {
    Map<String, Object> userMap = generateUserMap();

    ResponseSpec response = userService.create(UserDTO.fromMap(userMap))
        .map(id -> TestUtils.doDelete(webTestClient, "/user/" + id + "/-1", TestUtils.getAccessToken(tokenProvider)))
        .block();
    TestUtils.assertResponseError(response, HttpStatus.BAD_REQUEST, "E103", "User version not matched!");
  }
}
