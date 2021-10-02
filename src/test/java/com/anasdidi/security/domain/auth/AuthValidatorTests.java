package com.anasdidi.security.domain.auth;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.anasdidi.security.common.ApplicationMessage;
import com.anasdidi.security.common.TestUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthValidatorTests {

  private final AuthValidator validator;

  @Autowired
  public AuthValidatorTests(ApplicationMessage message) {
    this.validator = new AuthValidator(message);
  }

  @Test
  public void testValidateLogin() {
    List<String> actualList = validator.validateLogin(AuthDTO.fromMap(new HashMap<>()));
    List<String> expectedList = Arrays.asList("Username is mandatory field!", "Password is mandatory field!");
    TestUtils.assertValidationError(actualList, expectedList);
  }
}
