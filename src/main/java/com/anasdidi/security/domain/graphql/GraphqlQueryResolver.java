package com.anasdidi.security.domain.graphql;

import java.util.List;
import java.util.stream.Collectors;
import com.anasdidi.security.common.BaseResolver;
import com.anasdidi.security.domain.graphql.type.UserMapper;
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
class GraphqlQueryResolver extends BaseResolver implements GraphQLQueryResolver {

  private static final Logger logger = LogManager.getLogger(GraphqlQueryResolver.class);
  private final UserRepository userRepository;

  @Autowired
  GraphqlQueryResolver(UserRepository userRepository) {
    this.userRepository = userRepository;
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

    return responseBody.map(UserMapper::fromVO);
  }

  Mono<List<UserMapper>> users(DataFetchingEnvironment env) {
    String executionId = getExecutionId(env);

    if (logger.isDebugEnabled()) {
      logger.debug("[users:{}] ...", executionId);
    }

    return userRepository.findAll().map(UserMapper::fromVO).collect(Collectors.toList());
  }
}
