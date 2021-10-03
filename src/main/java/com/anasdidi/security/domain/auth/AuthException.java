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

  ApplicationException throwInvalidCredentials() {
    return new ApplicationException(AuthConstants.ERROR_INVALID_CREDENTIALS,
        message.getErrorMessage(AuthConstants.ERROR_INVALID_CREDENTIALS), "Wrong username/password");
  }
}
