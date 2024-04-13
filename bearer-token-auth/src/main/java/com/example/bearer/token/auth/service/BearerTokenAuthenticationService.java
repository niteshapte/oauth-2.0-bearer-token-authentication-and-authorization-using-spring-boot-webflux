package com.example.bearer.token.auth.service;

import org.springframework.stereotype.Service;

import com.example.bearer.token.auth.response.BearerTokenResponseDTO;

import reactor.core.publisher.Mono;

@Service
public interface BearerTokenAuthenticationService {
	
	public Mono<BearerTokenResponseDTO> bearerToken();

}
