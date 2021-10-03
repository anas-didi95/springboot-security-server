package com.anasdidi.security.domain.user;

import com.anasdidi.security.common.ApplicationException;
import com.anasdidi.security.common.ApplicationMessage;

import org.springframework.stereotype.Component;

@Component
final class UserException {

  private final ApplicationMessage message;

  UserException(ApplicationMessage message) {
    this.message = message;
  }

  ApplicationException throwUserCreationFailed(String errorMessage) {
    return new ApplicationException(UserConstants.ERROR_CREATE, message.getErrorMessage(UserConstants.ERROR_CREATE),
        errorMessage);
  }

  ApplicationException throwUserNotFound(UserDTO dto) {
    return new ApplicationException(UserConstants.ERROR_NOT_FOUND,
        message.getErrorMessage(UserConstants.ERROR_NOT_FOUND), "Failed to find user with id: " + dto.id);
  }

  ApplicationException throwVersionNotMatched(UserVO vo) {
    return new ApplicationException(UserConstants.ERROR_VERSION_NOT_MATCHED,
        message.getErrorMessage(UserConstants.ERROR_VERSION_NOT_MATCHED),
        "Requested user version not matched with current version: " + vo.getVersion());
  }
}
