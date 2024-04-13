# oauth-2.0-bearer-token-authentication-and-authorization-using-spring-boot-webflux
How to implement OAuth 2.0 Bearer Token authentication and authorization using Spring Boot WebFlux

## Please visit the blog to more details
https://www.google.com](https://blog.niteshapte.com/2024-04-13-how-to-implement-oauth-2-0-bearer-token-authentication-and-authorization-using-spring-boot-webflux.htm

---
In modern web applications, securing APIs with OAuth 2.0 is a common practice. But before we proceed further, let's see what is meant by OAuth.

## What is OAuth?
OAuth which stands for “Open Authorization”, is a standard designed to allow a website or application to access resources hosted by other web apps on behalf of a user. It replaced OAuth 1.0 in 2012 and is now the de facto industry standard for online authorization. OAuth 2.0 provides consented access and restricts actions of what the client app can perform on resources on behalf of the user, without ever sharing the user's credentials.

auth0.com

## What is authentication?

The process of verify an identity based on attached credentials with it is called authentication.

## What is authorization?

The process of granting access to a associated and protected resource to an authenticated identity is called authorization.

Now, what is ## Bearer Authentication & Authorization?

First we need to know what does "bearer" means. The term "Bearer" signifies that whoever possesses the authority (credentials) can use it to access the protected resources, similar to how a person who bears a physical key can use it to unlock a door. It's important to handle bearer tokens securely, as they can provide unrestricted access to the associated resources. Now, simply replace "identity" with "bearer" in above definitions of authentication and authorization, that's what bearer authentication & authorization means. Bearer authentication & authorization are also called token authentication & authorization, because a token is used in this process which is nothing but a long alphanumeric string - be it opaque or jwt string.

```java
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
```
