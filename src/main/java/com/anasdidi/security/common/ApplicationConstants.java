package com.anasdidi.security.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = "classpath:resources.properties")
public class ApplicationConstants {

  private final Environment env;

  @Autowired
  public ApplicationConstants(Environment env) {
    this.env = env;
  }

  public String getProperty(String key) {
    return env.getProperty(key);
  }
}
