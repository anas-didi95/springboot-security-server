package com.anasdidi.security.repository;

import com.anasdidi.security.vo.UserVO;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserVO, String> {

  Mono<UserVO> findByUsername(String username);
}
