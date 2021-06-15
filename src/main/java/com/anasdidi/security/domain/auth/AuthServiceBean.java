package com.anasdidi.security.domain.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
class AuthServiceBean implements AuthService {

  private static final Logger logger = LogManager.getLogger(AuthServiceBean.class);

  @Override
  public Mono<String> login(AuthDTO dto) {
    final String TAG = "login";

    if (logger.isDebugEnabled()) {
      logger.debug("[{}:{}] {}", TAG, dto.sessionId, dto);
    }

    return Mono.defer(() -> {
      return Mono.just("" + System.currentTimeMillis());
    });
  }

}
