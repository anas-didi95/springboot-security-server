package com.anasdidi.security.domain.graphql.type;

import java.time.Instant;
import com.anasdidi.security.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class UserMapper {

  private String id;
  private String username;
  private String fullName;
  private String email;
  private Instant lastModifiedDate;
  private String lastModifiedBy;
  private Integer version;

  public static UserMapper fromVO(UserVO vo) {
    String id = vo.getId();
    String username = vo.getUsername();
    String fullName = vo.getFullName();
    String email = vo.getEmail();
    Instant lastModifiedDate = vo.getLastModifiedDate();
    String lastModifiedBy = vo.getLastModifiedBy();
    Integer version = vo.getVersion();
    return new UserMapper(id, username, fullName, email, lastModifiedDate, lastModifiedBy, version);
  }
}
