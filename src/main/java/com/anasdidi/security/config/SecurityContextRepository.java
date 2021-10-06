package com.anasdidi.security.config;

import com.anasdidi.security.common.ApplicationUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {

  private static final Logger logger = LogManager.getLogger(SecurityContextRepository.class);
  private static final String TOKEN_PREFIX = "Bearer ";
  private final AuthenticationManager authenticationManager;

  @Autowired
  public SecurityContextRepository(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
    throw new UnsupportedOperationException("Not supported yet!");
  }

  @Override
  public Mono<SecurityContext> load(ServerWebExchange exchange) {
    return exchange.getSession().flatMap(session -> {
      String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
      String traceId = session.getAttribute("traceId");
      String authToken = null;

      if (logger.isDebugEnabled()) {
        logger.debug("[load:{}] authHeader={}", traceId, ApplicationUtils.hideValue(authHeader));
      }

      if (StringUtils.hasText(authHeader) && authHeader.startsWith(TOKEN_PREFIX)) {
        authToken = authHeader.replace(TOKEN_PREFIX, "").trim();
      } else {
        if (logger.isDebugEnabled()) {
          logger.debug("[save] Could not find token, will ignore the header");
        }
      }

      if (StringUtils.hasText(authToken)) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(authToken, authToken);
        return authenticationManager.authenticate(authentication).map(auth -> new SecurityContextImpl(auth));
      } else {
        return Mono.empty();
      }
    });
  }
}
