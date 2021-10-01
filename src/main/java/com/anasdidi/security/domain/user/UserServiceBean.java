package com.anasdidi.security.domain.user;

import java.util.Date;
import java.util.Optional;

import com.anasdidi.security.common.ApplicationException;
import com.anasdidi.security.common.ApplicationMessage;
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
  private final UserRepository userRepository;
  private final ApplicationMessage message;
  private final BCryptPasswordEncoder passwordEncoder;

  @Autowired
  UserServiceBean(UserRepository userRepository, ApplicationMessage message, BCryptPasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.message = message;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Mono<String> create(UserDTO dto) {
    return Mono.just(dto.toVO()).map(vo -> {
      vo.setId(ApplicationUtils.getFormattedUUID());
      vo.setPassword(passwordEncoder.encode(dto.password));
      vo.setLastModifiedDate(new Date());
      vo.setVersion(0);
      return vo;
    }).map(vo -> userRepository.save(vo)).doOnError(e -> {
      logger.error("[create:{}] {}", dto.sessionId, e.getMessage());
      logger.error("[create:{}] {}", dto.sessionId, dto);
      e.addSuppressed(new ApplicationException(UserConstants.ERROR_CREATE,
          message.getErrorMessage(UserConstants.ERROR_CREATE), e.getMessage()));
    }).map(vo -> vo.getId());
  }

  @Override
  public Mono<String> update(UserDTO dto) {
    Mono<UserVO> db = Mono.defer(() -> {
      Optional<UserVO> result = userRepository.findById(dto.id);

      if (result.isPresent()) {
        UserVO vo = result.get();
        if (vo.getVersion() == dto.version) {
          return Mono.just(vo);
        } else {
          logger.error("[update:{}] {}", dto.sessionId, dto);
          return Mono.error(UserException.getVersionNotMatched(message, vo));
        }
      } else {
        logger.error("[update:{}] {}", dto.sessionId, dto);
        return Mono.error(UserException.getUserNotFound(message, dto));
      }
    });

    return Mono.zip(db, Mono.just(dto.toVO()), (dbVO, reqVO) -> {
      dbVO.setFullName(reqVO.getFullName());
      dbVO.setEmail(reqVO.getEmail());
      dbVO.setLastModifiedDate(new Date());
      dbVO.setVersion(dbVO.getVersion() + 1);
      return dbVO;
    }).map(vo -> userRepository.save(vo)).map(vo -> vo.getId());
  }

  @Override
  public Mono<String> delete(UserDTO dto) {
    return Mono.defer(() -> {
      Optional<UserVO> userVO = userRepository.findById(dto.id);

      if (userVO.isPresent()) {
        UserVO vo = userVO.get();
        if (vo.getVersion() == dto.version) {
          return Mono.just(userVO.get());
        } else {
          logger.error("[delete:{}", dto.sessionId, dto);
          return Mono.error(UserException.getVersionNotMatched(message, vo));
        }
      } else {
        logger.error("[delete:{}] {}", dto.sessionId, dto);
        return Mono.error(UserException.getUserNotFound(message, dto));
      }
    }).map(vo -> {
      userRepository.delete(vo);
      return vo;
    }).map(vo -> vo.getId());
  }
}
