package com.anasdidi.security.repository;

import com.anasdidi.security.vo.UserVO;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserVO, String> {

  Flux<UserVO> findByUsername(String username);
}
