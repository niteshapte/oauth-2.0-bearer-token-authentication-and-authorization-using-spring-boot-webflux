package com.example.bearer.token.auth.service;

import java.util.Base64;
import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.bearer.token.auth.constant.BearerTokenAuthConstant;
import com.example.bearer.token.auth.response.BearerTokenResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
@RequiredArgsConstructor
@Service
public class BearerTokenAuthenticationServiceImpl implements BearerTokenAuthenticationService {
	
	private final WebClient.Builder builder;

	@Override
	public Mono<BearerTokenResponseDTO> bearerToken() {
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	    
	    String authString = BearerTokenAuthConstant.CLIENT_ID + ":" + BearerTokenAuthConstant.CLIENT_SECRET;
	    headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString(authString.getBytes()));

	    MultiValueMap<String, String> bodyParamMap = new LinkedMultiValueMap<>();
	    bodyParamMap.add("grant_type", BearerTokenAuthConstant.GRANT_TYPE);
	    bodyParamMap.add("scope", BearerTokenAuthConstant.SCOPE);

	    return builder.baseUrl(BearerTokenAuthConstant.OAUTH2_BASE_URL).build()
            .post()
            .uri(BearerTokenAuthConstant.OAUTH2_URI)
            .headers(httpHeaders -> httpHeaders.addAll(headers))
            .bodyValue(bodyParamMap)
            .retrieve()
            .bodyToMono(BearerTokenResponseDTO.class)
            .doOnSuccess(response -> log.info("Status code 200, Response {}", response))
            .onErrorResume(WebClientResponseException.class, ex -> {
                log.error("Error while fetching token: {}", ex.getStatusCode(), ex);
                return Mono.empty();
            });
	}
}
