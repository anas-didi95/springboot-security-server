package com.anasdidi.security.common;

public class ApplicationUtils {

  public static String getFormattedUUID(String uuid) {
    return uuid.replaceAll("-", "").toUpperCase();
  }
}
