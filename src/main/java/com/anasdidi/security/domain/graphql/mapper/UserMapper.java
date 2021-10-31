package com.anasdidi.security.domain.graphql.mapper;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import com.anasdidi.security.vo.UserVO;
import graphql.schema.DataFetchingEnvironment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserMapper {

  private String id;
  private String username;
  private String fullName;
  private String email;
  private Instant lastModifiedDate;
  private Integer version;

  public String getLastModifiedDate(String format, DataFetchingEnvironment env) {
    if (format != null) {
      if (lastModifiedDate == null) {
        return null;
      }

      Date date = Date.from(lastModifiedDate);
      return new SimpleDateFormat(format).format(date);
    }
    return lastModifiedDate.toString();
  }

  public static UserMapper fromVO(UserVO vo) {
    String id = vo.getId();
    String username = vo.getUsername();
    String fullName = vo.getFullName();
    String email = vo.getEmail();
    Instant lastModifiedDate = vo.getLastModifiedDate();
    Integer version = vo.getVersion();
    return new UserMapper(id, username, fullName, email, lastModifiedDate, version);
  }
}
