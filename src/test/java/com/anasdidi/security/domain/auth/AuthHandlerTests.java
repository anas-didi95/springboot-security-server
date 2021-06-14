package com.anasdidi.security.domain.auth;

import java.util.HashMap;
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
public class AuthHandlerTests {

  private final WebTestClient webTestClient;

  @Autowired
  public AuthHandlerTests(WebTestClient webTestClient) {
    this.webTestClient = webTestClient;
  }

  @Test
  public void testAuthLoginSuccess() {
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("username", "username");
    requestBody.put("password", "password");

    ResponseSpec response =
        webTestClient.post().uri("/auth/login").accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON).bodyValue(requestBody).exchange();
    response.expectStatus().isEqualTo(HttpStatus.OK);

    @SuppressWarnings("unchecked")
    Map<String, Object> responseBody =
        response.expectBody(Map.class).returnResult().getResponseBody();
    Assertions.assertNotNull(responseBody.get("accessToken"));
  }
}
