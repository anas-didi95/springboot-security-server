package com.anasdidi.security.domain.user;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserHandlerTests {

  private final WebTestClient webTestClient;

  @Autowired
  public UserHandlerTests(WebTestClient webTestClient) {
    this.webTestClient = webTestClient;
  }

  @Test
  public void testUserCreateSuccess() {
    String suffix = "" + System.currentTimeMillis();
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("username", "username" + suffix);
    requestBody.put("password", "password" + suffix);
    requestBody.put("fullName", "fullName" + suffix);
    requestBody.put("email", "email" + suffix);

    ResponseSpec response = webTestClient.post().uri("/user").accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON).bodyValue(requestBody).exchange();
    response.expectStatus().isCreated();

    @SuppressWarnings("unchecked")
    Map<String, Object> responseBody =
        response.expectBody(Map.class).returnResult().getResponseBody();
    Assertions.assertNotNull(responseBody.get("id"));
  }
}
