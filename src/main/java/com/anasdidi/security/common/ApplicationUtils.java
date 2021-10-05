package com.anasdidi.security.common;

import java.util.UUID;

public final class ApplicationUtils {

  public static final String getFormattedUUID() {
    return getFormattedUUID(UUID.randomUUID().toString());
  }

  public static final String getFormattedUUID(String uuid) {
    return uuid.replaceAll("-", "").toUpperCase();
  }

  public static final String hideValue(String value) {
    return String.format("[len: %d]", value != null ? value.length() : -1);
  }
}
