package com.anasdidi.security.common;

import java.util.Date;

import com.anasdidi.security.domain.user.UserVO;

public class TestUtils {

  public static UserVO generateUserVO() {
    String prefix = "" + System.currentTimeMillis();
    String id = prefix + ":id";
    String username = prefix + ":id";
    String password = prefix + ":id";
    String fullName = prefix + ":id";
    String email = prefix + ":id";
    Date lastModifiedDate = new Date();
    int version = 0;

    return new UserVO(id, username, password, fullName, email, lastModifiedDate, version);
  }
}
