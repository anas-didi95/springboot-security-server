package com.anasdidi.security.domain.user;

import java.time.LocalDateTime;

import com.anasdidi.security.common.ApplicationUtils;
import com.anasdidi.security.repository.UserRepository;
import com.anasdidi.security.vo.UserVO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
final class UserServiceBean implements UserService {

  private static final Logger logger = LogManager.getLogger(UserServiceBean.class);
  private final BCryptPasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final UserException userException;

  @Autowired
  UserServiceBean(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, UserException userException) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.userException = userException;
  }

  @Override
  public Mono<String> create(UserDTO dto) {
    UserVO vo = dto.toVO();
    vo.setId(ApplicationUtils.getFormattedUUID());
    vo.setPassword(passwordEncoder.encode(dto.password));
    vo.setLastModifiedDate(LocalDateTime.now());
    vo.setVersion(0);

    if (logger.isDebugEnabled()) {
      logger.debug("[create]{} {}", dto.traceId, vo);
    }

    return userRepository.save(vo).doOnError(e -> {
      logger.error("[create]{} {}", dto.traceId, e.getMessage());
      logger.error("[create]{} {}", dto.traceId, vo);
      e.addSuppressed(userException.throwUserCreationFailed(dto, e.getMessage()));
    }).map(result -> result.getId());
  }

  @Override
  public Mono<String> update(UserDTO dto) {
    UserVO reqVO = dto.toVO();

    if (logger.isDebugEnabled()) {
      logger.debug("[update]{} id={}", dto.traceId, dto.id);
    }

    return userRepository.findById(dto.id).switchIfEmpty(Mono.defer(() -> {
      logger.error("[update]{} id={}", dto.traceId, dto.id);
      return Mono.error(userException.throwUserNotFound(dto));
    })).flatMap(vo -> {
      if (vo.getVersion() == dto.version) {
        return Mono.just(vo);
      } else {
        logger.error("[update]{} id={}, dto.version={}, vo.version={}", dto.traceId, dto.id, dto.version,
            vo.getVersion());
        return Mono.error(userException.throwVersionNotMatched(vo, dto));
      }
    }).map(dbVO -> {
      reqVO.setId(dbVO.getId());
      reqVO.setPassword(dbVO.getPassword());
      reqVO.setLastModifiedDate(LocalDateTime.now());
      reqVO.setVersion(dbVO.getVersion() + 1);

      if (logger.isDebugEnabled()) {
        logger.debug("[update]{} {}", dto.traceId, reqVO);
      }

      return reqVO;
    }).flatMap(userRepository::save).map(result -> result.getId());
  }

  @Override
  public Mono<String> delete(UserDTO dto) {
    if (logger.isDebugEnabled()) {
      logger.debug("[delete]{} id={}", dto.traceId, dto.id);
    }

    return userRepository.findById(dto.id).switchIfEmpty(Mono.defer(() -> {
      logger.error("[delete]{} id={}", dto.traceId, dto.id);
      return Mono.error(userException.throwUserNotFound(dto));
    })).flatMap(vo -> {
      if (vo.getVersion() == dto.version) {
        return Mono.just(vo);
      } else {
        logger.error("[delete]{} id={}, dto.version={}, vo.version={}", dto.traceId, dto.id, dto.version,
            vo.getVersion());
        return Mono.error(userException.throwVersionNotMatched(vo, dto));
      }
    }).flatMap(vo -> userRepository.delete(vo).thenReturn(vo)).map(vo -> vo.getId());
  }
}
