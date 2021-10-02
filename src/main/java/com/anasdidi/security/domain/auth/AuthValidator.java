package com.anasdidi.security.domain.auth;

import java.util.ArrayList;
import java.util.List;

import com.anasdidi.security.common.ApplicationMessage;
import com.anasdidi.security.common.BaseValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
final class AuthValidator extends BaseValidator<AuthDTO> {

  @Autowired
  protected AuthValidator(ApplicationMessage message) {
    super(message);
  }

  @Override
  protected List<String> validateLogin(AuthDTO dto) {
    List<String> errorList = new ArrayList<>();

    isBlank(errorList, dto.username, "Username is mandatory field!");
    isBlank(errorList, dto.password, "Password is mandatory field!");

    return errorList;
  }
}
