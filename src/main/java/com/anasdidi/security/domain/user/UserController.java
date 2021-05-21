package com.anasdidi.security.domain.user;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/user")
public class UserController {

  private static final Logger logger = LogManager.getLogger(UserController.class);

  @RequestMapping(method = {RequestMethod.POST}, value = "",
      consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Mono<Map<String, String>>> create() {
    final String TAG = "create";

    Map<String, String> responseBody = new HashMap<>();
    responseBody.put("path", "/users");
    responseBody.put("statusCode", "201");

    if (logger.isDebugEnabled()) {
      logger.debug("{} Returning response body", TAG);
    }

    return ResponseEntity.ok().body(Mono.just(responseBody));
  }
}
