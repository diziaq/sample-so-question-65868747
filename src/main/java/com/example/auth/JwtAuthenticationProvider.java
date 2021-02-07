package com.example.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.function.Function;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;

public interface JwtAuthenticationProvider {

  Authentication fromHeaders(HttpHeaders headers);

  default Authentication fromRequest(ServerHttpRequest request) {
    return fromHeaders(request.getHeaders());
  }

  static <T> JwtAuthenticationProvider with(
      Function<HttpHeaders, DecodedJWT> parseToken,
      Function<DecodedJWT, JwtHandle> createHandle
  ) {
    return new JwtAuthenticationProviderDefault(parseToken, createHandle);
  }
}
