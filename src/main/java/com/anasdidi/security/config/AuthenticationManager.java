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
    String username = null;

    try {
      username = tokenProvider.getUsername(token);
    } catch (Exception e) {
      username = null;
    }

    if (username != null && !tokenProvider.isTokenExpired(token)) {
      List<SimpleGrantedAuthority> permissionList = tokenProvider.getPermissionList(token);
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, username,
          permissionList);
      SecurityContextHolder.getContext().setAuthentication(new AuthenticatedUser(username, permissionList));
      return Mono.just(auth);
    }

    return Mono.empty();
  }
}
