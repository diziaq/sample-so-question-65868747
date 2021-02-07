package com.example.custom;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.auth.JwtHandle;
import java.util.function.Function;

public final class MakeJwtHandleFromDecodedJwt implements Function<DecodedJWT, JwtHandle> {

  @Override
  public JwtHandle apply(DecodedJWT decodedJWT) {
    var payload = new JwtPayloadAuth0(decodedJWT);
    return new JwtHandleSimple(payload);
  }
}
