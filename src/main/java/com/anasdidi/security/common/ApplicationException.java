package com.anasdidi.security.common;

import java.util.Arrays;
import java.util.List;

public class ApplicationException extends Exception {

  private final String code;
  private final String message;
  private final String traceId;
  private final List<String> errorList;

  public ApplicationException(String code, String message, String traceId, String error) {
    this(code, message, traceId, Arrays.asList(error));
  }

  public ApplicationException(String code, String message, String traceId, List<String> errorList) {
    this.code = code;
    this.message = message;
    this.traceId = traceId;
    this.errorList = errorList;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public String getTraceId() {
    return traceId;
  }

  public List<String> getErrorList() {
    return errorList;
  }
}
