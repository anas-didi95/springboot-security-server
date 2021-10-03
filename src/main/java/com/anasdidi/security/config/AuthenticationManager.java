package com.anasdidi.security.config;

import java.util.List;

import com.anasdidi.security.common.AuthenticatedUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

  private final TokenProvider tokenProvider;

  @Autowired
  public AuthenticationManager(TokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {
    String token = authentication.getCredentials().toString();
    String userId = null;

    try {
      userId = tokenProvider.getUserId(token);
    } catch (Exception e) {
      userId = null;
    }

    if (tokenProvider.validateToken(token)) {
      List<SimpleGrantedAuthority> permissionList = tokenProvider.getPermissionList(token);
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, token, permissionList);
      SecurityContextHolder.getContext().setAuthentication(new AuthenticatedUser(userId, permissionList));
      return Mono.just(auth);
    } else {
      return Mono.empty();
    }
  }
}
