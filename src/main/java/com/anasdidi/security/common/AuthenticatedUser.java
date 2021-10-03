package com.anasdidi.security.common;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class AuthenticatedUser implements Authentication {

  private final String userId;
  private final List<SimpleGrantedAuthority> roles;
  private boolean isAuthenticated;

  public AuthenticatedUser(String userId, List<SimpleGrantedAuthority> roles) {
    this(userId, roles, true);
  }

  public AuthenticatedUser(String userId, List<SimpleGrantedAuthority> roles, boolean isAuthenticated) {
    this.userId = userId;
    this.roles = roles;
    this.isAuthenticated = isAuthenticated;
  }

  @Override
  public String getName() {
    return this.userId;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles;
  }

  @Override
  public Object getCredentials() {
    return userId;
  }

  @Override
  public Object getDetails() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return this.userId;
  }

  @Override
  public boolean isAuthenticated() {
    return this.isAuthenticated;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    this.isAuthenticated = isAuthenticated;
  }
}
