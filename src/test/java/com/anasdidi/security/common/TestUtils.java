package com.anasdidi.security.common;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.anasdidi.security.config.TokenProvider;
import com.anasdidi.security.vo.UserVO;

import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.RequestBodySpec;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

public class TestUtils {

  public static void assertResponseError(ResponseSpec response, HttpStatus expectedStatus, String expectedCode,
      String expectedMessage) {
    assertResponseError(response, expectedStatus, expectedCode, expectedMessage, null);
  }

  @SuppressWarnings("unchecked")
  public static void assertResponseError(ResponseSpec response, HttpStatus expectedStatus, String expectedCode,
      String expectedMessage, String expectedError) {
    response.expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);

    Map<String, Object> responseBody = response.expectBody(Map.class).returnResult().getResponseBody();
    Assertions.assertEquals(expectedCode, responseBody.get("code"));
    Assertions.assertEquals(expectedMessage, responseBody.get("message"));
    Assertions.assertNotNull(responseBody.get("traceId"));

    if (expectedError != null) {
      List<String> errorList = (List<String>) responseBody.get("errors");
      Assertions.assertEquals(expectedError, errorList.get(0));
    } else {
      List<String> errorList = (List<String>) responseBody.get("errors");
      Assertions.assertTrue(!errorList.isEmpty());
    }
  }

  public static void assertValidationError(List<String> actualList, List<String> expectedList) {
    Assertions.assertEquals(expectedList.size(), actualList.size());
    expectedList.stream().forEach(error -> {
      Assertions.assertTrue(actualList.contains(error), "Expected error not found! " + error);
    });
  }

  public static String getAccessToken(TokenProvider tokenProvider) {
    return getAccessToken(tokenProvider, "UNITTEST", Arrays.asList("ADMIN"));
  }

  public static String getAccessToken(TokenProvider tokenProvider, String userId, List<String> permissionList) {
    return tokenProvider.generateToken(userId, permissionList, null);
  }

  public static UserVO generateUserVO() {
    String prefix = "" + System.currentTimeMillis();
    String id = prefix + ":id";
    String username = prefix + ":id";
    String password = prefix + ":id";
    String fullName = prefix + ":id";
    String email = prefix + ":id";
    Date lastModifiedDate = new Date();
    int version = 0;

    return new UserVO(id, username, password, fullName, email, lastModifiedDate, version);
  }

  public static final ResponseSpec doPost(WebTestClient webTestClient, String uri, Map<String, Object> requestBody) {
    return doPost(webTestClient, uri, requestBody, null);
  }

  public static final ResponseSpec doPost(WebTestClient webTestClient, String uri, Map<String, Object> requestBody,
      String accessToken) {
    RequestBodySpec request = webTestClient.post().uri(uri).accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON);
    request = accessToken != null ? request.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) : request;
    return requestBody != null ? request.bodyValue(requestBody).exchange() : request.exchange();
  }

  public static final ResponseSpec doPut(WebTestClient webTestClient, String uri, Map<String, Object> requestBody,
      String accessToken) {
    RequestBodySpec request = webTestClient.put().uri(uri).accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
    return requestBody != null ? request.bodyValue(requestBody).exchange() : request.exchange();
  }

  public static final ResponseSpec doDelete(WebTestClient webTestClient, String uri, String accessToken) {
    return webTestClient.delete().uri(uri).accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken).exchange();
  }

  public static final ResponseSpec doGet(WebTestClient webTestClient, String uri, String accessToken) {
    return webTestClient.get().uri(uri).accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken).exchange();
  }
}
