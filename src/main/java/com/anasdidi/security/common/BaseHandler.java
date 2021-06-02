package com.anasdidi.security.common;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public abstract class BaseHandler {

  private static final Logger logger = LogManager.getLogger(BaseHandler.class);

  protected Mono<ServerResponse> sendResponse(Mono<Map<String, Object>> subscriber,
      HttpStatus httpStatus, ServerRequest request) {
    final String TAG = "sendResponse";

    return subscriber.log(TAG).flatMap(responseBody -> {
      logResponseStatus(request, TAG, true, httpStatus);
      return ServerResponse.status(httpStatus).bodyValue(responseBody);
    }).onErrorResume(e -> {
      Map<String, Object> responseBody = new HashMap<>();
      if (e instanceof ApplicationException) {
        ApplicationException ex = (ApplicationException) e;
        responseBody.put("code", ex.getCode());
        responseBody.put("message", ex.getMessage());
        responseBody.put("errors", ex.getErrorList());
      } else {
        responseBody.put("message", e.getMessage());
      }

      logResponseStatus(request, TAG, false, httpStatus, (String) responseBody.get("code"),
          (String) responseBody.get("message"));
      return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(responseBody);
    });
  }

  @SuppressWarnings("unchecked")
  protected Mono<Map<String, Object>> getRequestData(ServerRequest request) {
    return Mono.zip(getSessionData(request), request.bodyToMono(Map.class),
        (sessionMap, requestBody) -> {
          Map<String, Object> map = new HashMap<>();
          map.putAll(sessionMap);
          map.putAll(requestBody);
          return map;
        });
  }

  private Mono<Map<String, Object>> getSessionData(ServerRequest request) {
    return request.session().map(s -> {
      Map<String, Object> map = new HashMap<>();
      map.put("sessionId", ApplicationUtils.getFormattedUUID(s.getId()));
      return map;
    });
  }

  private void logResponseStatus(ServerRequest request, String tag, boolean isSuccess,
      HttpStatus httpStatus) {
    logResponseStatus(request, tag, isSuccess, httpStatus, null, null);
  }

  private void logResponseStatus(ServerRequest request, String tag, boolean isSuccess,
      HttpStatus httpStatus, String code, String message) {
    if (isSuccess) {
      getSessionData(request).subscribe(map -> logger.info("[{}:{}] onSuccess : httpStatus={}", tag,
          map.get("sessionId"), httpStatus));
    } else {
      getSessionData(request).subscribe(map -> logger.error("[{}:{}] onError : code={}, message={}",
          tag, map.get("sessionId"), code, message));
    }
  }
}
