package com.anasdidi.security.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.anasdidi.security.filter.RequestTraceIdFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
  private final String GRAPHIQL_ENABLE;

  @Autowired
  public SecurityConfig(AuthenticationManager authenticationManager,
      SecurityContextRepository securityContextRepository,
      RequestTraceIdFilter requestTraceIdFilter,
      @Value("${graphiql.enabled}") String GRAPHIQL_ENABLE) {
    this.authenticationManager = authenticationManager;
    this.securityContextRepository = securityContextRepository;
    this.requestTraceIdFilter = requestTraceIdFilter;
    this.GRAPHIQL_ENABLE = GRAPHIQL_ENABLE;
  }

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    List<String> patterns = new ArrayList<>(Arrays.asList("/auth/login/**"));

    if (Boolean.parseBoolean(GRAPHIQL_ENABLE)) {
      patterns.addAll(
          Arrays.asList("/graphiql**", "/vendor/graphiql/**", "/graphql**", "/subscriptions**"));
    }

    return http.csrf().disable().cors().disable().exceptionHandling()
        .authenticationEntryPoint((exchange, exception) -> Mono.fromRunnable(() -> {
          exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        })).accessDeniedHandler((exchange, exception) -> Mono.fromRunnable(() -> {
          exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        })).and().csrf().disable().authenticationManager(authenticationManager)
        .securityContextRepository(securityContextRepository).authorizeExchange()
        .pathMatchers(patterns.toArray(new String[] {})).permitAll().anyExchange().authenticated()
        .and().addFilterBefore(requestTraceIdFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        .build();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
