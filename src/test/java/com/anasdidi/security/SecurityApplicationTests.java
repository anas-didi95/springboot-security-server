package com.anasdidi.security;

import com.anasdidi.security.common.ApplicationMessage;
import com.anasdidi.security.common.TestUtils;
import com.anasdidi.security.config.TokenProvider;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SecurityApplicationTests {

  private final ApplicationMessage message;
  private final TokenProvider tokenProvider;

  @Autowired
  public SecurityApplicationTests(ApplicationMessage message, TokenProvider tokenProvider) {
    this.message = message;
    this.tokenProvider = tokenProvider;
  }

  @Test
  public void contextLoads() {
    Assertions.assertEquals("Request body is empty!", message.getMessage("message.error.E001"));
    Assertions.assertEquals("Validation error!", message.getMessage("message.error.E002"));
    Assertions.assertEquals("User creation failed!", message.getMessage("message.error.E101"));
    Assertions.assertEquals("User not found!", message.getMessage("message.error.E102"));
    Assertions.assertEquals("User version not matched!", message.getMessage("message.error.E103"));
  }

  @Test
  public void testGenerateAccessToken() {
    String accessToken = TestUtils.getAccessToken(tokenProvider);
    Assertions.assertNotNull(accessToken);
    System.out.println("[testGenerateAccessToken] accessToken=" + accessToken);
  }
}
