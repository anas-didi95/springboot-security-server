package com.anasdidi.security.domain.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
class UserConfig {

  @Bean
  RouterFunction<ServerResponse> userRouter(UserHandler userHandler) {
    return RouterFunctions//
        .route(
            RequestPredicates.POST("/user")
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON))
                .and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)),
            userHandler::create);
  }
}
