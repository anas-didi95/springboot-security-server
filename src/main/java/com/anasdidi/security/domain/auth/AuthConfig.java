package com.anasdidi.security.domain.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
class AuthConfig {

  @Bean
  RouterFunction<ServerResponse> authRouter(AuthHandler authHandler) {
    return RouterFunctions
        .route(RequestPredicates.POST("/auth/login").and(RequestPredicates.accept(MediaType.APPLICATION_JSON))
            .and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), authHandler::login)
        .andRoute(RequestPredicates.GET("/auth/check").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
            authHandler::check);
  }
}
