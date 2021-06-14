package com.anasdidi.security.domain.auth;

import java.util.HashMap;
import java.util.Map;
import com.anasdidi.security.common.ApplicationMessage;
import com.anasdidi.security.common.BaseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
class AuthHandler extends BaseHandler {

  @Autowired
  public AuthHandler(ApplicationMessage message) {
    super(message);
  }

  Mono<ServerResponse> login(ServerRequest request) {
    Mono<Map<String, Object>> subscriber = request.bodyToMono(Map.class).map(map -> {
      Map<String, Object> responseBody = new HashMap<>();
      responseBody.put("accessToken", System.currentTimeMillis());
      return responseBody;
    });

    return sendResponse(subscriber, HttpStatus.OK, request);
  }
}
