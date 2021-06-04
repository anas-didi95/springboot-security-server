package com.anasdidi.security.domain.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserHandlerTests {

  private final WebTestClient webTestClient;
  private final UserService userService;

  @Autowired
  public UserHandlerTests(WebTestClient webTestClient, UserService userService) {
    this.webTestClient = webTestClient;
    this.userService = userService;
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

  @Test
  public void testUserCreateSuccess() {
    Map<String, Object> requestBody = generateUserMap();

    ResponseSpec response = webTestClient.post().uri("/user").accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON).bodyValue(requestBody).exchange();
    response.expectStatus().isEqualTo(HttpStatus.CREATED);

    @SuppressWarnings("unchecked")
    Map<String, Object> responseBody =
        response.expectBody(Map.class).returnResult().getResponseBody();
    Assertions.assertNotNull(responseBody.get("id"));
  }

  @Test
  public void testUserCreateRequestBodyError() {
    ResponseSpec response = webTestClient.post().uri("/user").accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON).exchange();
    response.expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);

    @SuppressWarnings("unchecked")
    Map<String, Object> responseBody =
        response.expectBody(Map.class).returnResult().getResponseBody();
    Assertions.assertEquals("E002", responseBody.get("code"));
    Assertions.assertEquals("Request body is empty!", responseBody.get("message"));

    @SuppressWarnings("unchecked")
    List<String> errorList = (List<String>) responseBody.get("errors");
    Assertions.assertEquals(true, !errorList.isEmpty());
  }

  @Test
  public void testUserCreateValidationError() {
    Map<String, Object> requestBody = generateUserMap();
    requestBody.put("username", null);

    ResponseSpec response = webTestClient.post().uri("/user").accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON).bodyValue(requestBody).exchange();
    response.expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);

    @SuppressWarnings("unchecked")
    Map<String, Object> responseBody =
        response.expectBody(Map.class).returnResult().getResponseBody();
    Assertions.assertEquals("E001", responseBody.get("code"));
    Assertions.assertEquals("Validation error!", responseBody.get("message"));

    @SuppressWarnings("unchecked")
    List<String> errorList = (List<String>) responseBody.get("errors");
    Assertions.assertEquals(true, !errorList.isEmpty());
  }

  @Test
  public void testUserCreateServiceError() {
    Map<String, Object> requestBody = generateUserMap();

    ResponseSpec response = userService.create(UserDTO.fromMap(requestBody))
        .map(id -> webTestClient.post().uri("/user").accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON).bodyValue(requestBody).exchange())
        .block();
    response.expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);

    @SuppressWarnings("unchecked")
    Map<String, Object> responseBody =
        response.expectBody(Map.class).returnResult().getResponseBody();
    Assertions.assertEquals("E101", responseBody.get("code"));
    Assertions.assertEquals("User creation failed!", responseBody.get("message"));

    @SuppressWarnings("unchecked")
    List<String> errorList = (List<String>) responseBody.get("errors");
    Assertions.assertEquals(true, !errorList.isEmpty());
  }
}
