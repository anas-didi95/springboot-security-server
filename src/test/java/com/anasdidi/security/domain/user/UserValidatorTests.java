package com.anasdidi.security.domain.user;

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
public class UserValidatorTests {

  private final UserValidator validator;

  @Autowired
  public UserValidatorTests(ApplicationMessage message) {
    validator = new UserValidator(message);
  }

  @Test
  public void testValidateCreate() {
    List<String> actualList = validator.validateCreate(UserDTO.fromMap(new HashMap<>()));
    List<String> expectedList = Arrays.asList("Username is mandatory field!", "Password is mandatory field!",
        "Full Name is mandatory field!", "Email is mandatory field!");
    TestUtils.assertValidationError(actualList, expectedList);
  }

  @Test
  public void testValidateUpdate() {
    List<String> actualList = validator.validateUpdate(UserDTO.fromMap(new HashMap<>()));
    List<String> expectedList = Arrays.asList("Id is mandatory field!", "Full Name is mandatory field!",
        "Email is mandatory field!", "Version is mandatory field!");
    TestUtils.assertValidationError(actualList, expectedList);
  }
}
