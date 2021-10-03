package com.anasdidi.security.domain.user;

import java.util.Date;
import java.util.Optional;

import com.anasdidi.security.common.ApplicationUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
class UserServiceBean implements UserService {

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
    vo.setLastModifiedDate(new Date());
    vo.setVersion(0);

    if (logger.isDebugEnabled()) {
      logger.debug("[create:{}] {}", dto.sessionId, vo);
    }

    return Mono.just(vo).map(userRepository::save).doOnError(e -> {
      logger.error("[create:{}] {}", dto.sessionId, e.getMessage());
      logger.error("[create:{}] {}", dto.sessionId, vo);
      e.addSuppressed(userException.throwUserCreationFailed(e.getMessage()));
    }).map(result -> result.getId());
  }

  @Override
  public Mono<String> update(UserDTO dto) {
    UserVO reqVO = dto.toVO();
    return Mono.defer(() -> {
      Optional<UserVO> result = userRepository.findById(dto.id);

      if (logger.isDebugEnabled()) {
        logger.debug("[update:{}] id={}, isPresent={}", dto.sessionId, dto.id, result.isPresent());
      }

      if (result.isPresent()) {
        UserVO vo = result.get();
        if (vo.getVersion() == dto.version) {
          return Mono.just(vo);
        } else {
          logger.error("[update:{}] id={}, dto.version={}, vo.version={}", dto.sessionId, dto.version, vo.getVersion());
          return Mono.error(userException.throwVersionNotMatched(vo));
        }
      } else {
        logger.error("[update:{}] id={}, isPresent={}", dto.sessionId, dto.id, result.isPresent());
        return Mono.error(userException.throwUserNotFound(dto));
      }
    }).map(dbVO -> {
      reqVO.setId(dbVO.getId());
      reqVO.setPassword(dbVO.getPassword());
      reqVO.setLastModifiedDate(new Date());
      reqVO.setVersion(dbVO.getVersion() + 1);

      if (logger.isDebugEnabled()) {
        logger.debug("[update:{}] {}", dto.sessionId, reqVO);
      }

      return reqVO;
    }).map(userRepository::save).map(result -> result.getId());
  }

  @Override
  public Mono<String> delete(UserDTO dto) {
    return Mono.defer(() -> {
      Optional<UserVO> result = userRepository.findById(dto.id);

      if (logger.isDebugEnabled()) {
        logger.debug("[delete:{}] id={}, isPresent={}", dto.sessionId, dto.id, result.isPresent());
      }

      if (result.isPresent()) {
        UserVO vo = result.get();
        if (vo.getVersion() == dto.version) {
          return Mono.just(result.get());
        } else {
          logger.error("[delete:{}] id={}, dto.version={}, vo.version={}", dto.sessionId, dto.version, vo.getVersion());
          return Mono.error(userException.throwVersionNotMatched(vo));
        }
      } else {
        logger.error("[delete:{}] id={}, isPresent={}", dto.sessionId, dto.id, result.isPresent());
        return Mono.error(userException.throwUserNotFound(dto));
      }
    }).map(vo -> {
      userRepository.delete(vo);
      return vo;
    }).map(vo -> vo.getId());
  }
}
