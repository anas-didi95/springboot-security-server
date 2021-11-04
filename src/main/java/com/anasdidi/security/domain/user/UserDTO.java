package com.anasdidi.security.domain.user;

import java.time.Instant;
import java.util.Map;

import com.anasdidi.security.common.BaseDTO;
import com.anasdidi.security.vo.UserVO;
import lombok.ToString;

@ToString
final class UserDTO extends BaseDTO {

  final String id;
  final String username;
  final String password;
  final String fullName;
  final String email;
  final Instant lastModifiedDate;
  final String lastModifiedBy;
  final Integer version;

  private UserDTO(String traceId, String id, String username, String password, String fullName,
      String email, Instant lastModifiedDate, String lastModifiedBy, Integer version) {
    super(traceId);
    this.id = id;
    this.username = username;
    this.password = password;
    this.fullName = fullName;
    this.email = email;
    this.lastModifiedDate = lastModifiedDate;
    this.lastModifiedBy = lastModifiedBy;
    this.version = version;
  }

  static UserDTO fromMap(Map<String, Object> map) {
    String traceId = (String) map.get("traceId");
    String id = (String) map.get("id");
    String username = (String) map.get("username");
    String password = (String) map.get("password");
    String fullName = (String) map.get("fullName");
    String email = (String) map.get("email");
    Instant lastModifiedDate = (Instant) map.get("lastModifiedDate");
    String lastModifiedBy = (String) map.get("lastModifiedBy");
    Integer version = (Integer) map.get("version");

    return new UserDTO(traceId, id, username, password, fullName, email, lastModifiedDate,
        lastModifiedBy, version);
  }

  UserVO toVO() {
    return new UserVO(id, username, password, fullName, email, lastModifiedDate, lastModifiedBy,
        version);
  }
}
