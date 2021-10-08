package com.anasdidi.security.domain.auth;

import com.anasdidi.security.common.ApplicationException;
import com.anasdidi.security.common.ApplicationMessage;

import org.springframework.stereotype.Component;

@Component
final class AuthException {

  private final ApplicationMessage message;

  AuthException(ApplicationMessage message) {
    this.message = message;
  }

  ApplicationException throwInvalidCredentials(AuthDTO dto) {
    return new ApplicationException(AuthConstants.ERROR_INVALID_CREDENTIALS,
        message.getErrorMessage(AuthConstants.ERROR_INVALID_CREDENTIALS), dto.traceId, "Wrong username/password");
  }

  ApplicationException throwUserNotFound(AuthDTO dto) {
    return new ApplicationException(AuthConstants.ERROR_USER_NOT_FOUND,
        message.getErrorMessage(AuthConstants.ERROR_USER_NOT_FOUND), dto.traceId,
        "Failed to find user with id: " + dto.principal);
  }
}
