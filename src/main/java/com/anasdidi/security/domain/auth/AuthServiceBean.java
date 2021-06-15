package com.anasdidi.security.domain.auth;

import java.util.List;
import com.anasdidi.security.domain.user.UserRepository;
import com.anasdidi.security.domain.user.UserVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
class AuthServiceBean implements AuthService {

  private static final Logger logger = LogManager.getLogger(AuthServiceBean.class);
  private final UserRepository userRepository;

  @Autowired
  AuthServiceBean(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Mono<String> login(AuthDTO dto) {
    final String TAG = "login";

    if (logger.isDebugEnabled()) {
      logger.debug("[{}:{}] {}", TAG, dto.sessionId, dto);
    }

    return Mono.defer(() -> {
      List<UserVO> resultList = userRepository.findByUsername(dto.username);

      if (logger.isDebugEnabled()) {
        logger.debug("[{}:{}] resultList.size=", TAG, dto.sessionId,
            (resultList != null ? resultList.size() : -1));
      }

      if (resultList != null && !resultList.isEmpty()) {
        UserVO vo = resultList.get(0);
        if (vo.getPassword().equals(dto.password)) {
          return Mono.just("" + System.currentTimeMillis());
        }
      }

      return Mono.empty();
    });
  }

}
