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
      HttpStatus httpStatus) {
    final String TAG = "sendResponse";

    return subscriber
        .flatMap(responseBody -> ServerResponse.status(httpStatus).bodyValue(responseBody))//
        .onErrorResume(e -> {
          Map<String, Object> responseBody = new HashMap<>();
          if (e instanceof ApplicationException) {
            ApplicationException ex = (ApplicationException) e;
            responseBody.put("code", ex.getCode());
            responseBody.put("message", ex.getMessage());
            responseBody.put("errors", ex.getErrorList());
          } else {
            responseBody.put("message", e.getMessage());
          }

          logger.error("[{}] onError : code={}, message={}", TAG, responseBody.get("code"),
              responseBody.get("message"));
          return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(responseBody);
        });
  }

  protected Mono<Map<String, Object>> getRequestData(ServerRequest request) {
    Mono<Map<String, Object>> session = request.session().map(s -> {
      Map<String, Object> map = new HashMap<>();
      map.put("sessionId", ApplicationUtils.getFormattedUUID(s.getId()));
      map.put("sessionStartTime", s.getCreationTime());
      return map;
    });

    @SuppressWarnings("unchecked")
    Mono<Map<String, Object>> requestData =
        Mono.zip(session, request.bodyToMono(Map.class), (sessionMap, requestBody) -> {
          Map<String, Object> map = new HashMap<>();
          map.putAll(sessionMap);
          map.putAll(requestBody);
          return map;
        });

    return requestData;
  }
}
