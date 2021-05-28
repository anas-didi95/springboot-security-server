package com.anasdidi.security.domain.user;

import java.util.Date;
import java.util.Map;

class UserDTO {

  final String id;
  final String username;
  final String password;
  final String fullName;
  final String email;
  final Date lastModifiedDate;
  final Integer version;

  private UserDTO(String id, String username, String password, String fullName, String email,
      Date lastModifiedDate, Integer version) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.fullName = fullName;
    this.email = email;
    this.lastModifiedDate = lastModifiedDate;
    this.version = version;
  }

  static UserDTO fromMap(Map<String, Object> map) {
    String id = (String) map.get("id");
    String username = (String) map.get("username");
    String password = (String) map.get("password");
    String fullName = (String) map.get("fullName");
    String email = (String) map.get("email");
    Date lastModifiedDate = (Date) map.get("lastModifiedDate");
    Integer version = (Integer) map.get("version");

    return new UserDTO(id, username, password, fullName, email, lastModifiedDate, version);
  }

  UserVO toVO() {
    return new UserVO(id, username, password, fullName, email, lastModifiedDate, version);
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + " :" //
        + ": id=" + id//
        + ", username=" + username//
        + ", password=" + password//
        + ", fullName=" + fullName//
        + ", email=" + email//
        + ", lastModifiedDate=" + lastModifiedDate//
        + ", version=" + version;
  }
}
