package com.anasdidi.security.common;

public abstract class BaseDTO {

  public final String traceId;

  public BaseDTO(String sessionId) {
    this.traceId = sessionId;
  }
}
