package com.anasdidi.security.domain.auth;

import java.util.Map;
import com.anasdidi.security.common.BaseDTO;

class AuthDTO extends BaseDTO {

  final String username;
  final String password;

  public AuthDTO(String traceId, String username, String password) {
    super(traceId);
    this.username = username;
    this.password = password;
  }

  static AuthDTO fromMap(Map<String, Object> map) {
    String traceId = (String) map.get("traceId");
    String username = (String) map.get("username");
    String password = (String) map.get("password");

    return new AuthDTO(traceId, username, password);
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + ":"//
        + ": username=" + username//
        + ", password=" + password;
  }
}
