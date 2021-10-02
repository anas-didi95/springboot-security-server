package com.anasdidi.security.domain.user;

import java.util.ArrayList;
import java.util.List;

import com.anasdidi.security.common.ApplicationMessage;
import com.anasdidi.security.common.BaseValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class UserValidator extends BaseValidator<UserDTO> {

  @Autowired
  public UserValidator(ApplicationMessage message) {
    super(message);
  }

  @Override
  protected List<String> validateCreate(UserDTO dto) {
    List<String> errorList = new ArrayList<>();

    isBlank(errorList, dto.username, "Username is mandatory field!");
    isBlank(errorList, dto.password, "Password is mandatory field!");
    isBlank(errorList, dto.fullName, "Full Name is mandatory field!");
    isBlank(errorList, dto.email, "Email is mandatory field!");

    return errorList;
  }

  @Override
  protected List<String> validateUpdate(UserDTO dto) {
    List<String> errorList = new ArrayList<>();

    isBlank(errorList, dto.id, "Id is mandatory field!");
    isBlank(errorList, dto.fullName, "Full Name is mandatory field!");
    isBlank(errorList, dto.email, "Email is mandatory field!");
    isBlank(errorList, dto.version, "Version is mandatory field!");

    return errorList;
  }
}
