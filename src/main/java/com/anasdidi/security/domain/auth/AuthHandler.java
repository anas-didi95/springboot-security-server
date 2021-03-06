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
final class AuthHandler extends BaseHandler {

  private final AuthService authService;
  private final AuthValidator authValidator;

  @Autowired
  public AuthHandler(ApplicationMessage message, AuthService authService, AuthValidator authValidator) {
    super(message);
    this.authService = authService;
    this.authValidator = authValidator;
  }

  Mono<ServerResponse> login(ServerRequest request) {
    Mono<Map<String, Object>> subscriber = getRequestBody(request, "username", "password")
        .map(map -> AuthDTO.fromMap(map)).flatMap(dto -> authValidator.validate(AuthValidator.Action.LOGIN, dto))
        .flatMap(dto -> authService.login(dto)).map(accessToken -> {
          Map<String, Object> responseBody = new HashMap<>();
          responseBody.put("accessToken", accessToken);
          return responseBody;
        });

    return sendResponse(subscriber, HttpStatus.OK, request);
  }

  Mono<ServerResponse> check(ServerRequest request) {
    Mono<Map<String, Object>> subscriber = getRequestBody(request).map(map -> AuthDTO.fromMap(map))
        .flatMap(dto -> authService.check(dto)).map(dto -> {
          Map<String, Object> responseBody = new HashMap<>();
          responseBody.put("username", dto.username);
          responseBody.put("fullName", dto.fullName);
          return responseBody;
        });

    return sendResponse(subscriber, HttpStatus.OK, request);
  }
}
