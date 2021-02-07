package com.example.custom;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.auth.JwtPayload;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

final class JwtPayloadAuth0 implements JwtPayload {

  private final DecodedJWT decodedJWT;

  /* default */ JwtPayloadAuth0(DecodedJWT decodedJWT) {
    this.decodedJWT = Objects.requireNonNull(decodedJWT, "decoded jwt");
  }

  @Override
  public <T> T claim(String claimName, Class<T> claimType) {
    return decodedJWT.getClaim(claimName).as(claimType);
  }

  @Override
  public Map<String, Object> claims() {
    return decodedJWT.getClaims().entrySet().stream()
                     .collect(
                         Collectors.toMap(
                             e -> e.getKey(),
                             e -> e.getValue().as(Object.class)
                         ));
  }
}
