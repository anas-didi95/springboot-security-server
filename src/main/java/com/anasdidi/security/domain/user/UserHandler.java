package com.anasdidi.security.domain.user;

import java.util.HashMap;
import java.util.Map;
import com.anasdidi.security.common.BaseHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
class UserHandler extends BaseHandler {

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

    Mono<Map<String, Object>> subscriber =
        getRequestData(request, "{username,password,fullName,email}")//
            .map(map -> UserDTO.fromMap(map))//
            .flatMap(dto -> userValidator.validate(UserValidator.Action.CREATE, dto))//
            .flatMap(dto -> userService.create(dto))//
            .map(id -> {
              Map<String, Object> map = new HashMap<>();
              map.put("id", id);
              return map;
            });

    return sendResponse(subscriber, HttpStatus.CREATED, request);
  }
}
