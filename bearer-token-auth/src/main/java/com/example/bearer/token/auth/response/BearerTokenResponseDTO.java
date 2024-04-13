package com.example.bearer.token.auth.response;

import lombok.Data;

@Data
public class BearerTokenResponseDTO {

	private String access_token;
	
	private String expires_in;
	
	// ... put other field as needed
}
