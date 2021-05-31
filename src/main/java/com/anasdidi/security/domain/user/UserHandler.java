package com.anasdidi.security.domain.user;

import java.util.HashMap;
import java.util.Map;
import com.anasdidi.security.common.ApplicationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
class UserHandler {

  private static final Logger logger = LogManager.getLogger(UserHandler.class);
  private final UserValidator userValidator;
  private final UserService userService;

  @Autowired
  UserHandler(UserValidator userValidator, UserService userService) {
    this.userValidator = userValidator;
    this.userService = userService;
  }

  Mono<ServerResponse> create(ServerRequest request) {
    final String TAG = "create";

    if (logger.isDebugEnabled()) {
      logger.debug("[{}] request={}", TAG, request);
    }

    @SuppressWarnings("unchecked")
    Mono<Map<String, Object>> subscriber = request.bodyToMono(Map.class)//
        .map(map -> UserDTO.fromMap(map))//
        .flatMap(dto -> userValidator.validate(UserValidator.Action.CREATE, dto))//
        .flatMap(dto -> userService.create(dto))//
        .map(id -> {
          Map<String, Object> map = new HashMap<>();
          map.put("id", id);
          return map;
        });

    return subscriber
        .flatMap(responseBody -> ServerResponse.status(HttpStatus.CREATED).bodyValue(responseBody))//
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
}
