package com.anasdidi.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.PathMatchConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class AppConfig implements WebFluxConfigurer {

  @Override
  public void configurePathMatching(PathMatchConfigurer configurer) {
    WebFluxConfigurer.super.configurePathMatching(configurer);
  }
}
