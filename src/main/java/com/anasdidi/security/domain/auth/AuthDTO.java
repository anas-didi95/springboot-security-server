package com.anasdidi.security.domain.auth;

import java.util.Map;

import com.anasdidi.security.common.BaseDTO;
import com.anasdidi.security.vo.UserVO;

final class AuthDTO extends BaseDTO {

  final String username;
  final String password;
  final String principal;
  final String fullName;

  public AuthDTO(String traceId, String username, String password, String principal, String fullName) {
    super(traceId);
    this.username = username;
    this.password = password;
    this.principal = principal;
    this.fullName = fullName;
  }

  static AuthDTO fromMap(Map<String, Object> map) {
    String traceId = (String) map.get("traceId");
    String username = (String) map.get("username");
    String password = (String) map.get("password");
    String principal = (String) map.get("principal");
    String fullName = (String) map.get("fullName");

    return new AuthDTO(traceId, username, password, principal, fullName);
  }

  static AuthDTO fromVO(UserVO vo, String traceId) {
    String username = vo.getUsername();
    String password = vo.getPassword();
    String principal = vo.getId();
    String fullName = vo.getFullName();

    return new AuthDTO(traceId, username, password, principal, fullName);
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + ":"//
        + ": username=" + username//
        + ", password=" + password//
        + ", principal=" + principal//
        + ", fullName=" + fullName;
  }
}
