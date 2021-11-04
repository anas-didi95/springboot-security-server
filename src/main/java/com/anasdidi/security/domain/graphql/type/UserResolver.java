package com.anasdidi.security.domain.graphql.type;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import com.anasdidi.security.common.BaseResolver;
import com.anasdidi.security.repository.UserRepository;
import com.anasdidi.security.vo.UserVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import reactor.core.publisher.Mono;

@Component
class UserResolver extends BaseResolver implements GraphQLResolver<UserMapper> {

  private static final Logger logger = LogManager.getLogger(UserResolver.class);
  private final UserRepository userRepository;

  @Autowired
  UserResolver(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  Mono<String> getLastModifiedDate(UserMapper source, String format, DataFetchingEnvironment env) {
    return Mono.fromCallable(() -> {
      Instant lastModifiedDate = source.getLastModifiedDate();
      if (format != null) {
        if (lastModifiedDate == null) {
          return null;
        }

        Date date = Date.from(lastModifiedDate);
        return new SimpleDateFormat(format).format(date);
      }
      return lastModifiedDate.toString();
    });
  }

  Mono<UserMapper> getLastModifiedBy(UserMapper source, DataFetchingEnvironment env) {
    String executionId = getExecutionId(env);
    String id = source.getLastModifiedBy();

    if (logger.isDebugEnabled()) {
      logger.debug("[getLastModifiedBy:{}] id={}", executionId, id);
    }

    return userRepository.findById(id)
        .defaultIfEmpty(new UserVO(id, id, null, null, null, null, null, null))
        .map(UserMapper::fromVO);
  }
}
