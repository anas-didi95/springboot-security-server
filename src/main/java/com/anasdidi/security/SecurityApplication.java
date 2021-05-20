package com.anasdidi.security;

import com.anasdidi.security.domain.greeting.GreetingWebClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecurityApplication {

  private static final Logger logger = LogManager.getLogger(SecurityApplication.class);

  public static void main(String[] args) {
    final String TAG = "main";
    SpringApplication.run(SecurityApplication.class, args);

    GreetingWebClient greetingWebClient = new GreetingWebClient();
    logger.info("[{}] {}", TAG, greetingWebClient.getResult());
  }
}
