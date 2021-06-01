package com.anasdidi.security;

import com.anasdidi.security.common.ApplicationConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SecurityApplicationTests {

  private final ApplicationConstants constants;

  @Autowired
  public SecurityApplicationTests(ApplicationConstants constants) {
    this.constants = constants;
  }

  @Test
  public void contextLoads() {
    Assertions.assertEquals("Validation Error!", constants.getProperty("error.E001"));
  }

}
