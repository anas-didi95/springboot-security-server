package com.anasdidi.security.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
public class RequestTraceIdFilter implements WebFilter {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    return exchange.getSession().flatMap(session -> {
      session.getAttributes().putIfAbsent("traceId", exchange.getLogPrefix().trim());
      return chain.filter(exchange);
    });
  }
}
