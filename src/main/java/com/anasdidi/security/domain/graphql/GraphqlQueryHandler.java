package com.anasdidi.security.domain.graphql;

import com.anasdidi.security.common.ApplicationUtils;
import com.anasdidi.security.domain.graphql.mapper.UserMapper;
import com.anasdidi.security.repository.UserRepository;
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

  Mono<UserMapper> user(String id, DataFetchingEnvironment env) {
    String executionId = getExecutionId(env);

    if (logger.isDebugEnabled()) {
      logger.debug("[user:{}] id={}", executionId, id);
    }

    return userRepository.findById(id)
        .map(result -> UserMapper.builder().id(result.getId()).username(result.getUsername())
            .email(result.getEmail()).fullName(result.getFullName()).build());
  }

  private String getExecutionId(DataFetchingEnvironment env) {
    return ApplicationUtils.getFormattedUUID(env.getExecutionId().toString());
  }
}
