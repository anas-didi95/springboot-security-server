package com.anasdidi.security.config;

import com.anasdidi.security.filter.RequestTraceIdFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  private final AuthenticationManager authenticationManager;
  private final SecurityContextRepository securityContextRepository;
  private final RequestTraceIdFilter requestTraceIdFilter;

  @Autowired
  public SecurityConfig(AuthenticationManager authenticationManager,
      SecurityContextRepository securityContextRepository, RequestTraceIdFilter requestTraceIdFilter) {
    this.authenticationManager = authenticationManager;
    this.securityContextRepository = securityContextRepository;
    this.requestTraceIdFilter = requestTraceIdFilter;
  }

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    String[] patterns = new String[] { "/auth/**" };

    return http.cors().disable().exceptionHandling()
        .authenticationEntryPoint((exchange, exception) -> Mono.fromRunnable(() -> {
          exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        })).accessDeniedHandler((exchange, exception) -> Mono.fromRunnable(() -> {
          exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        })).and().csrf().disable().authenticationManager(authenticationManager)
        .securityContextRepository(securityContextRepository).authorizeExchange().pathMatchers(patterns).permitAll()
        .anyExchange().authenticated().and().addFilterAfter(requestTraceIdFilter, SecurityWebFiltersOrder.FIRST)
        .build();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
