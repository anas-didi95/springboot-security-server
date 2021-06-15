package com.anasdidi.security.domain.auth;

import reactor.core.publisher.Mono;

interface AuthService {

  Mono<String> login(AuthDTO dto);
}
