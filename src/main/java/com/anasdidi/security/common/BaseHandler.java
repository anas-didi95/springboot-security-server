package com.anasdidi.security.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public abstract class BaseHandler {

  private static final Logger logger = LogManager.getLogger(BaseHandler.class);
  private static final String ERROR_REQUEST_BODY_EMPTY = "E001";
  private final ApplicationMessage message;

  public BaseHandler(ApplicationMessage message) {
    this.message = message;
  }

  protected Mono<ServerResponse> sendResponse(Mono<Map<String, Object>> subscriber, HttpStatus httpStatus,
      ServerRequest request) {
    final String TAG = "sendResponse";
    return request.session().flatMap(session -> {
      String traceId = session.getAttribute("traceId");
      return subscriber.log(TAG).flatMap(responseBody -> {
        logResponseStatus(TAG, traceId, true, httpStatus);
        return ServerResponse.status(httpStatus).bodyValue(responseBody);
      }).onErrorResume(e -> {
        Map<String, Object> responseBody = new HashMap<>();
        ApplicationException ex = null;

        if (e instanceof ApplicationException) {
          ex = (ApplicationException) e;
        } else if (e.getSuppressed().length > 0) {
          Optional<Throwable> ee = Arrays.stream(e.getSuppressed()).filter(t -> t instanceof ApplicationException)
              .findFirst();
          if (ee.isPresent()) {
            ex = (ApplicationException) ee.get();
          }
        }

        if (ex != null) {
          responseBody.put("code", ex.getCode());
          responseBody.put("message", ex.getMessage());
          responseBody.put("errors", ex.getErrorList());
        } else {
          responseBody.put("message", e.getMessage());
        }

        logResponseStatus(TAG, traceId, false, httpStatus, (String) responseBody.get("code"),
            (String) responseBody.get("message"));
        return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(responseBody);
      });
    });
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  protected Mono<Map<String, Object>> getRequestBody(ServerRequest request, String... keys) {
    Mono<Map> requestBody = request.bodyToMono(Map.class);
    if (keys != null && keys.length > 0) {
      requestBody = requestBody
          .switchIfEmpty(Mono.defer(() -> Mono.error(new ApplicationException(ERROR_REQUEST_BODY_EMPTY,
              message.getErrorMessage(ERROR_REQUEST_BODY_EMPTY), "Required keys: " + String.join(",", keys)))));
    } else {
      requestBody = requestBody.defaultIfEmpty(new HashMap<>());
    }

    return Mono.zip(getSessionData(request), requestBody, (sessionMap, requestMap) -> {
      Map<String, Object> map = new HashMap<>();
      map.putAll(sessionMap);
      map.putAll(requestMap);
      return map;
    });
  }

  private Mono<Map<String, Object>> getSessionData(ServerRequest request) {
    return request.session().map(s -> {
      Map<String, Object> map = new HashMap<>();
      map.put("traceId", s.getAttribute("traceId"));
      return map;
    });
  }

  private void logResponseStatus(String tag, String traceId, boolean isSuccess, HttpStatus httpStatus) {
    logResponseStatus(tag, traceId, isSuccess, httpStatus, null, null);
  }

  private void logResponseStatus(String tag, String traceId, boolean isSuccess, HttpStatus httpStatus, String code,
      String message) {
    if (isSuccess) {
      logger.info("[{}:{}] onSuccess : httpStatus={}", tag, traceId, httpStatus);
    } else {
      logger.error("[{}:{}] onError : code={}, message={}", tag, traceId, code, message);
    }
  }
}
