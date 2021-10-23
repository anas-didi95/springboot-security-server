package com.anasdidi.security.domain.graphql;

import com.anasdidi.security.common.ApplicationUtils;
import com.anasdidi.security.domain.graphql.mapper.UserMapper;
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

  Mono<UserMapper> user(String id, String username, DataFetchingEnvironment env) {
    String executionId = getExecutionId(env);
    int searchBy = getSearchBy(id, username);

    if (logger.isDebugEnabled()) {
      logger.debug("[user:{}] id={}, username={}, searchBy={}", executionId, id, username,
          searchBy);
    }

    Mono<UserVO> responseBody = Mono.empty();
    switch (searchBy) {
      case 0:
        responseBody = responseBody.switchIfEmpty(userRepository.findById(id));
        break;
      case 1:
        responseBody = responseBody.switchIfEmpty(userRepository.findByUsername(username));
        break;
    }

    return responseBody
        .map(result -> UserMapper.builder().id(result.getId()).username(result.getUsername())
            .email(result.getEmail()).fullName(result.getFullName()).build());
  }

  private String getExecutionId(DataFetchingEnvironment env) {
    return ApplicationUtils.getFormattedUUID(env.getExecutionId().toString());
  }

  private int getSearchBy(String... values) {
    for (int i = 0; i < values.length; i++) {
      if (!values[i].isBlank()) {
        return i;
      }
    }
    return -1;
  }
}
