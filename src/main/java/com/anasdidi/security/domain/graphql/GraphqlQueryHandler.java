package com.anasdidi.security.domain.graphql;

import com.anasdidi.security.common.ApplicationUtils;
import com.anasdidi.security.repository.UserRepository;
import com.anasdidi.security.vo.UserVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import reactor.core.publisher.Mono;

@Component
class GraphqlQueryHandler implements GraphQLQueryResolver {

  private static final Logger logger = LogManager.getLogger(GraphqlQueryHandler.class);
  private final UserRepository userRepository;

  @Autowired
  GraphqlQueryHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  Mono<String> hello(DataFetchingEnvironment env) {
    String executionId = env.getExecutionId().toString();

    if (logger.isDebugEnabled()) {
      logger.debug("[user:{}] hello", executionId);
    }

    return Mono.just("Hello world");
  }

  Mono<UserVO> user(String id, DataFetchingEnvironment env) {
    String executionId = getExecutionId(env);

    if (logger.isDebugEnabled()) {
      logger.debug("[user:{}] id={}", executionId, id);
    }

    return userRepository.findById(id);
  }

  private String getExecutionId(DataFetchingEnvironment env) {
    return ApplicationUtils.getFormattedUUID(env.getExecutionId().toString());
  }
}
