package com.example.custom;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.HttpHeaders;

public final class MakeDecodedJwtFromAuthorizationHeader extends MakeTokenFromHeader<DecodedJWT> {

  public MakeDecodedJwtFromAuthorizationHeader() {
    super(HttpHeaders.AUTHORIZATION, "Bearer ", JWT::decode);
  }
}
