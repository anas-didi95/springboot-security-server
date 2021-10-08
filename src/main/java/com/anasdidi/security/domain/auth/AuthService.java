package com.anasdidi.security.domain.auth;

import reactor.core.publisher.Mono;

interface AuthService {

  Mono<String> login(AuthDTO dto);

  Mono<AuthDTO> check(AuthDTO dto);
}
