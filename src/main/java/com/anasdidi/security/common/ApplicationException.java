package com.anasdidi.security.common;

import java.util.Arrays;
import java.util.List;

public class ApplicationException extends Exception {

  private final String code;
  private final String message;
  private final List<String> errorList;

  public ApplicationException(String code, String message, List<String> errorList) {
    this.code = code;
    this.message = message;
    this.errorList = errorList;
  }

  public ApplicationException(String code, String message, String error) {
    this.code = code;
    this.message = message;
    this.errorList = Arrays.asList(error);
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public List<String> getErrorList() {
    return errorList;
  }
}
