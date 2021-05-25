package com.anasdidi.security.domain.user;

import java.util.Map;

class UserVO {

  private final String username;
  private final String password;
  private final String email;

  private UserVO(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }

  static UserVO fromMap(Map<String, Object> map) {
    String username = (String) map.get("username");
    String password = (String) map.get("password");
    String email = (String) map.get("email");

    return new UserVO(username, password, email);
  }

  @Override
  public String toString() {
    return "UserVO :" //
        + ": username=" + username//
        + ", password=" + password//
        + ", email=" + email;
  }
}
