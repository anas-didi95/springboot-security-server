package com.anasdidi.security.repository;

import java.util.List;

import com.anasdidi.security.vo.UserVO;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserVO, String> {

  List<UserVO> findByUsername(String username);
}
