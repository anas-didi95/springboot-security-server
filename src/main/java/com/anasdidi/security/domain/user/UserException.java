package com.anasdidi.security.domain.user;

import com.anasdidi.security.common.ApplicationException;
import com.anasdidi.security.common.ApplicationMessage;

class UserException {

  static ApplicationException getUserNotFound(ApplicationMessage message, UserDTO dto) {
    return new ApplicationException(UserConstants.ERROR_NOT_FOUND,
        message.getErrorMessage(UserConstants.ERROR_NOT_FOUND),
        "Failed to find user with id: " + dto.id);
  }
}
