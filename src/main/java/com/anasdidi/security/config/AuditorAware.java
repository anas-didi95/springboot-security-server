package com.anasdidi.security.config;

import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Mono;

public class AuditorAware implements ReactiveAuditorAware<String> {

  @Override
  public Mono<String> getCurrentAuditor() {
    return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
        .filter(Authentication::isAuthenticated).map(Authentication::getPrincipal)
        .map(String.class::cast);
  }
}
