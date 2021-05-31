package com.anasdidi.security.common;

public class ApplicationException extends Exception {

  private final String code;
  private final String message;

  public ApplicationException(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

}
