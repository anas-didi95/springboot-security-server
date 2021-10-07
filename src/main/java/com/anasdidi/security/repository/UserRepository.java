package com.anasdidi.security.repository;

import java.util.List;

import com.anasdidi.security.vo.UserVO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserVO, String> {

  List<UserVO> findByUsername(String username);
}
