package com.anasdidi.security.domain.auth;

import java.util.Arrays;
import com.anasdidi.security.common.ApplicationUtils;
import com.anasdidi.security.config.TokenProvider;
import com.anasdidi.security.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
final class AuthServiceBean implements AuthService {

  private static final Logger logger = LogManager.getLogger(AuthServiceBean.class);
  private final TokenProvider tokenProvider;
  private final BCryptPasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final AuthException authException;

  @Autowired
  AuthServiceBean(TokenProvider tokenProvider, BCryptPasswordEncoder passwordEncoder,
      UserRepository userRepository, AuthException authException) {
    this.tokenProvider = tokenProvider;
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.authException = authException;
  }

  @Override
  public Mono<String> login(AuthDTO dto) {
    return userRepository.findByUsername(dto.username).switchIfEmpty(Mono.defer(() -> {
      logger.error("[login]{} dto.username={}", dto.traceId, dto.username);
      return Mono.error(authException.throwInvalidCredentials(dto));
    })).flatMap(result -> {
      if (logger.isDebugEnabled()) {
        logger.debug("[login]{} dto.username={}, result.id={}", dto.traceId, dto.username,
            result.getId());
      }

      if (!passwordEncoder.matches(dto.password, result.getPassword())) {
        logger.error("[login]{} dto.username={}, result.id={}", dto.traceId, dto.username,
            result.getId());
        return Mono.error(authException.throwInvalidCredentials(dto));
      }

      String accessToken =
          tokenProvider.generateToken(result.getId(), Arrays.asList("ADMIN"), dto.traceId);

      if (logger.isDebugEnabled()) {
        logger.debug("[login]{} accessToken={}", dto.traceId,
            ApplicationUtils.hideValue(accessToken));
      }

      return Mono.just(accessToken);
    });
  }

  @Override
  public Mono<AuthDTO> check(AuthDTO dto) {
    if (logger.isDebugEnabled()) {
      logger.debug("[check]{} userId={}", dto.traceId, dto.principal);
    }

    return userRepository.findById(dto.principal).switchIfEmpty(Mono.defer(() -> {
      logger.error("[check]{} userId={}", dto.traceId, dto.principal);
      return Mono.error(authException.throwUserNotFound(dto));
    })).flatMap(vo -> {
      if (logger.isDebugEnabled()) {
        logger.debug("[check]{} userId={}", dto.traceId, dto.principal);
      }

      return Mono.just(AuthDTO.fromVO(vo, dto.traceId));
    });
  }
}
