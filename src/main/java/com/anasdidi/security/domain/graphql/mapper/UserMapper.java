package com.anasdidi.security.domain.graphql.mapper;

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
}
