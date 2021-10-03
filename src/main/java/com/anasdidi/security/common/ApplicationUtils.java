package com.anasdidi.security.common;

import java.util.UUID;

public class ApplicationUtils {

  public static String getFormattedUUID() {
    return getFormattedUUID(UUID.randomUUID().toString());
  }

  public static String getFormattedUUID(String uuid) {
    return uuid.replaceAll("-", "").toUpperCase();
  }
}
