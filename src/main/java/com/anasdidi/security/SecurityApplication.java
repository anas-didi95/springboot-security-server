package com.anasdidi.security;

import java.time.Instant;
import com.anasdidi.security.common.ApplicationUtils;
import com.anasdidi.security.repository.UserRepository;
import com.anasdidi.security.vo.UserVO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SecurityApplication {

  public static void main(String[] args) {
    SpringApplication.run(SecurityApplication.class, args);
  }

  @Bean
  public CommandLineRunner seed(PasswordEncoder passwordEncoder, UserRepository userRepository) {
    return (args) -> {
      userRepository.count().flatMap(count -> {
        if (count == 0) {
          String id = ApplicationUtils.getFormattedUUID();
          String username = "superadmin";
          String password = passwordEncoder.encode("password");
          String fullName = "Super Admin";
          String email = "superadmin@email.com";
          Instant lastModifiedDate = Instant.now();
          Integer version = null;
          UserVO vo =
              new UserVO(id, username, password, fullName, email, lastModifiedDate, version);
          return userRepository.save(vo);
        } else {
          return Mono.empty();
        }
      }).subscribe();
    };
  }
}
