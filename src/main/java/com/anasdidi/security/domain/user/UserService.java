package com.anasdidi.security.domain.user;

import reactor.core.publisher.Mono;

interface UserService {

  Mono<String> create(UserDTO dto);
}
