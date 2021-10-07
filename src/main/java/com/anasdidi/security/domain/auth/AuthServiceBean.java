package com.anasdidi.security.domain.auth;

import java.util.Arrays;
import java.util.List;

import com.anasdidi.security.common.ApplicationUtils;
import com.anasdidi.security.config.TokenProvider;
import com.anasdidi.security.repository.UserRepository;
import com.anasdidi.security.vo.UserVO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
class AuthServiceBean implements AuthService {

  private static final Logger logger = LogManager.getLogger(AuthServiceBean.class);
  private final TokenProvider tokenProvider;
  private final BCryptPasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final AuthException authException;

  @Autowired
  AuthServiceBean(TokenProvider tokenProvider, BCryptPasswordEncoder passwordEncoder, UserRepository userRepository,
      AuthException authException) {
    this.tokenProvider = tokenProvider;
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.authException = authException;
  }

  @Override
  public Mono<String> login(AuthDTO dto) {
    return Mono.defer(() -> {
      List<UserVO> resultList = userRepository.findByUsername(dto.username);

      if (logger.isDebugEnabled()) {
        logger.debug("[login]{} dto.username={}, resultList.size={}", dto.traceId, dto.username,
            (resultList != null ? resultList.size() : -1));
      }

      if (resultList != null && !resultList.isEmpty()) {
        UserVO vo = resultList.get(0);
        if (passwordEncoder.matches(dto.password, vo.getPassword())) {
          String accessToken = tokenProvider.generateToken(vo.getId(), Arrays.asList("ADMIN"), dto.traceId);

          if (logger.isDebugEnabled()) {
            logger.debug("[login]{} accessToken={}", dto.traceId, ApplicationUtils.hideValue(accessToken));
          }

          return Mono.just(accessToken);
        }
      }

      logger.error("[login]{} dto.username={}, resultList.size={}", dto.traceId, dto.username,
          (resultList != null ? resultList.size() : -1));
      return Mono.error(authException.throwInvalidCredentials(dto));
    });
  }
}
