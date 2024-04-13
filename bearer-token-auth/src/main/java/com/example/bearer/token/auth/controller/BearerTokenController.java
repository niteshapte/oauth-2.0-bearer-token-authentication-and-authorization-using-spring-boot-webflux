package com.example.bearer.token.auth.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.bearer.token.auth.response.BearerTokenResponseDTO;
import com.example.bearer.token.auth.service.BearerTokenAuthenticationService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("example")
public class BearerTokenController {
	
	private final BearerTokenAuthenticationService service;

	@PostMapping(value = "/toke/", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<BearerTokenResponseDTO> getBearerToken() {
		return service.bearerToken();
	}
}
