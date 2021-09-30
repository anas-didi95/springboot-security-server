package com.anasdidi.security.domain.auth;

import java.util.List;

import com.anasdidi.security.config.TokenProvider;
import com.anasdidi.security.domain.user.UserRepository;
import com.anasdidi.security.domain.user.UserVO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
class AuthServiceBean implements AuthService {

  private static final Logger logger = LogManager.getLogger(AuthServiceBean.class);
  private final UserRepository userRepository;
  private final TokenProvider tokenProvider;
  private final BCryptPasswordEncoder passwordEncoder;

  @Autowired
  AuthServiceBean(UserRepository userRepository, TokenProvider tokenProvider, BCryptPasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.tokenProvider = tokenProvider;
    this.passwordEncoder = passwordEncoder;
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
        logger.debug("[{}:{}] resultList.size=", TAG, dto.sessionId, (resultList != null ? resultList.size() : -1));
      }

      if (resultList != null && !resultList.isEmpty()) {
        UserVO vo = resultList.get(0);
        if (passwordEncoder.matches(dto.password, vo.getPassword())) {
          String accessToken = tokenProvider.generateToken(vo);

          if (logger.isDebugEnabled()) {
            logger.debug("[login:{}] accessToken={}", dto.sessionId, accessToken);
          }

          return Mono.just(accessToken);
        }
      }

      return Mono.empty();
    });
  }

}
