package com.anasdidi.security.domain.user;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/user")
class UserController {

  private static final Logger logger = LogManager.getLogger(UserController.class);
  private final UserService userService;

  @Autowired
  UserController(UserService userService) {
    this.userService = userService;
  }

  @RequestMapping(method = RequestMethod.POST, value = "",
      consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
  ResponseEntity<Mono<Map<String, Object>>> create(@RequestBody Map<String, Object> requestBody) {
    final String TAG = "create";
    UserDTO userDTO = UserDTO.fromMap(requestBody);

    Mono<Map<String, Object>> responseBody = Mono.just(userDTO)//
        .flatMap(dto -> userService.create(dto))//
        .map(id -> {
          Map<String, Object> map = new HashMap<>();
          map.put("id", id);
          return map;
        });

    return ResponseEntity.created(URI.create("/security/user/id")).body(responseBody);
  }
}
