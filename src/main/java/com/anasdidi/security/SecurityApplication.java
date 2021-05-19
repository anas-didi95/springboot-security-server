package com.anasdidi.security;

import com.anasdidi.security.domain.greeting.GreetingWebClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecurityApplication {

  public static void main(String[] args) {
    SpringApplication.run(SecurityApplication.class, args);

    GreetingWebClient greetingWebClient = new GreetingWebClient();
    System.out.println(greetingWebClient.getResult());
  }
}
