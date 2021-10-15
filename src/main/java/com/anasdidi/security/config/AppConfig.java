package com.anasdidi.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.PathMatchConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableR2dbcRepositories
@EnableR2dbcAuditing
public class AppConfig implements WebFluxConfigurer {

  @Override
  public void configurePathMatching(PathMatchConfigurer configurer) {
    WebFluxConfigurer.super.configurePathMatching(configurer);
  }
}
