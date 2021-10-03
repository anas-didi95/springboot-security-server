package com.anasdidi.security.domain.auth;

import java.util.Map;
import com.anasdidi.security.common.BaseDTO;

class AuthDTO extends BaseDTO {

  final String username;
  final String password;

  public AuthDTO(String sessionId, String username, String password) {
    super(sessionId);
    this.username = username;
    this.password = password;
  }

  static AuthDTO fromMap(Map<String, Object> map) {
    String sessionId = (String) map.get("sessionId");
    String username = (String) map.get("username");
    String password = (String) map.get("password");

    return new AuthDTO(sessionId, username, password);
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + ":"//
        + ": username=" + username//
        + ", password=" + password;
  }
}
