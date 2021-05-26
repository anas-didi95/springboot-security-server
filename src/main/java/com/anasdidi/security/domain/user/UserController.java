package com.anasdidi.security.domain.user;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
  private final UserRepository userRepository;

  @Autowired
  UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @RequestMapping(method = RequestMethod.POST, value = "",
      consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
  ResponseEntity<Mono<Map<String, String>>> create(@RequestBody Map<String, Object> requestBody) {
    final String TAG = "create";
    UserVO userVO = UserVO.fromMap(requestBody);

    Map<String, String> responseBody = new HashMap<>();
    responseBody.put("path", "/users");
    responseBody.put("statusCode", "201");

    if (logger.isDebugEnabled()) {
      logger.debug("[{}] {}", TAG, userVO);
    }

    User user = new User();
    user.setUsername(userVO.username);
    user = userRepository.save(user);

    if (logger.isDebugEnabled()) {
      logger.debug("[{}] user.id={}", TAG, user.getId());
    }

    return ResponseEntity.ok().body(Mono.just(responseBody));
  }
}
