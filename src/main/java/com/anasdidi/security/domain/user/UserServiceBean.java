package com.anasdidi.security.domain.user;

import java.util.Date;
import java.util.Optional;
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

  @Override
  public Mono<String> update(UserDTO dto) {
    final String TAG = "update";

    if (logger.isDebugEnabled()) {
      logger.debug("[{}:{}] {}", TAG, dto.sessionId, dto);
    }

    Mono<UserVO> db = Mono.defer(() -> {
      Optional<UserVO> result = userRepository.findById(dto.id);

      if (result.isPresent()) {
        UserVO vo = result.get();
        if (vo.getVersion() == dto.version) {
          return Mono.just(vo);
        } else {
          logger.error("[{}:{}] Requested user version not matched with current version: {}", TAG,
              dto.sessionId, vo.getVersion());
          logger.error("[{}:{}] {}", TAG, dto.sessionId, dto);
          return Mono.error(new ApplicationException(UserConstants.ERROR_VERSION_NOT_MATCHED,
              message.getErrorMessage(UserConstants.ERROR_VERSION_NOT_MATCHED),
              "Requested user version not matched with current version: " + vo.getVersion()));
        }
      } else {
        logger.error("[{}:{}] Failed to find user with id: {}", TAG, dto.sessionId, dto.id);
        logger.error("[{}:{}] {}", TAG, dto.sessionId, dto);
        return Mono.error(new ApplicationException(UserConstants.ERROR_NOT_FOUND,
            message.getErrorMessage(UserConstants.ERROR_NOT_FOUND),
            "Failed to find user with id: " + dto.id));
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
}
