package com.anasdidi.security.domain.user;

import com.anasdidi.security.common.ApplicationException;
import com.anasdidi.security.common.ApplicationMessage;

class UserException {

  static ApplicationException getUserNotFound(ApplicationMessage message, UserDTO dto) {
    return new ApplicationException(UserConstants.ERROR_NOT_FOUND,
        message.getErrorMessage(UserConstants.ERROR_NOT_FOUND),
        "Failed to find user with id: " + dto.id);
  }

  static ApplicationException getVersionNotMatched(ApplicationMessage message, UserVO vo) {
    return new ApplicationException(UserConstants.ERROR_VERSION_NOT_MATCHED,
        message.getErrorMessage(UserConstants.ERROR_VERSION_NOT_MATCHED),
        "Requested user version not matched with current version: " + vo.getVersion());
  }
}
