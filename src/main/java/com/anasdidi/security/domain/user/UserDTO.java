package com.anasdidi.security.domain.user;

import java.util.Map;

class UserDTO {

  final String username;
  final String password;
  final String email;

  private UserDTO(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }

  static UserDTO fromMap(Map<String, Object> map) {
    String username = (String) map.get("username");
    String password = (String) map.get("password");
    String email = (String) map.get("email");

    return new UserDTO(username, password, email);
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + " :" //
        + ": username=" + username//
        + ", password=" + password//
        + ", email=" + email;
  }
}
