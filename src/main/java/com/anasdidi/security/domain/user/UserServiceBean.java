package com.anasdidi.security.domain.user;

import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
class UserServiceBean implements UserService {

  private static final Logger logger = LogManager.getLogger(UserServiceBean.class);

  @Autowired
  UserServiceBean() {}

  @Override
  public Mono<String> create(UserDTO dto) {
    final String TAG = "create";

    if (logger.isDebugEnabled()) {
      logger.debug("[{}] {}", TAG, dto);
    }

    return Mono.just(UserVO.fromDTO(dto)).map(vo -> UUID.randomUUID().toString());
  }
}
