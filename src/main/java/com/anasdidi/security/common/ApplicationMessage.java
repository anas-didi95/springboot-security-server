package com.anasdidi.security.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = "classpath:message.properties")
public class ApplicationMessage {

  private final Environment env;

  @Autowired
  public ApplicationMessage(Environment env) {
    this.env = env;
  }

  public String getMessage(String key) {
    return env.getProperty(key);
  }

  public String getErrorMessage(String errorCode) {
    return env.getProperty("error." + errorCode);
  }
}
