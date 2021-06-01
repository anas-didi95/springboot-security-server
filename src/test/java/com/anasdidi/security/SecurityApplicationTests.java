package com.anasdidi.security;

import com.anasdidi.security.common.ApplicationMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SecurityApplicationTests {

  private final ApplicationMessage message;

  @Autowired
  public SecurityApplicationTests(ApplicationMessage message) {
    this.message = message;
  }

  @Test
  public void contextLoads() {
    Assertions.assertEquals("Validation Error!", message.getMessage("error.E001"));
  }
}
