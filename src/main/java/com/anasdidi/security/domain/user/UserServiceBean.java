package com.anasdidi.security.domain.user;

import java.util.Date;
import java.util.UUID;
import com.anasdidi.security.common.ApplicationException;
import com.anasdidi.security.common.ApplicationMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
class UserServiceBean implements UserService {

  private static final Logger logger = LogManager.getLogger(UserServiceBean.class);
  private final UserRepository userRepository;
  private final ApplicationMessage message;

  @Autowired
  UserServiceBean(UserRepository userRepository, ApplicationMessage message) {
    this.userRepository = userRepository;
    this.message = message;
  }

  @Override
  public Mono<String> create(UserDTO dto) {
    final String TAG = "create";

    if (logger.isDebugEnabled()) {
      logger.debug("[{}:{}] {}", TAG, dto.sessionId, dto);
    }

    return Mono.just(dto.toVO())//
        .map(vo -> {
          String id = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
          vo.setId(id);
          vo.setLastModifiedDate(new Date());
          vo.setVersion(0);
          return vo;
        })//
        .map(vo -> userRepository.save(vo))//
        .doOnError(e -> {
          logger.error("[{}:{}] {}", TAG, dto.sessionId, e.getMessage());
          logger.error("[{}:{}] {}", TAG, dto.sessionId, dto);
          e.addSuppressed(new ApplicationException(UserConstants.ERROR_CREATE,
              message.getErrorMessage(UserConstants.ERROR_CREATE), e.getMessage()));
        }).map(vo -> vo.getId());
  }
}
