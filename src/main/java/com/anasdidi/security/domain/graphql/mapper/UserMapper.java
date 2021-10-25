package com.anasdidi.security.domain.graphql.mapper;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import graphql.schema.DataFetchingEnvironment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
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
}
