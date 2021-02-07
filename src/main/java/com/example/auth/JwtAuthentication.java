package com.example.auth;

import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

interface JwtAuthentication extends Authentication {

  Collection<GrantedAuthority> getAuthorities();

  @Override
  default Object getCredentials() {
    return "JwtAuthentication[hidden-credentials]";
  }

  @Override
  default Object getDetails() {
    return "JwtAuthentication[hidden-details]";
  }

  JwtPayload getPrincipal();

  boolean isAuthenticated();

  @Override
  default void setAuthenticated(boolean isAuthenticated) {
    throw new IllegalStateException("Unexpected invocation of JwtAuthentication.setAuthenticated");
  }

  @Override
  default String getName() {
    return "JwtAuthentication[hidden-name]";
  }

  class Success implements JwtAuthentication {

    private final JwtHandle jwtHandle;

    public Success(JwtHandle jwtHandle) {
      this.jwtHandle = jwtHandle;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
      return jwtHandle.authorities();
    }

    @Override
    public JwtPayload getPrincipal() {
      return jwtHandle.payload();
    }

    @Override
    public boolean isAuthenticated() {
      return jwtHandle.isValid();
    }
  }

  class Failure implements JwtAuthentication {

    public static Authentication INSTANCE = new Failure();

    private Failure() {
      //
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
      throw new IllegalStateException("Unable to get authorities for a failed auth");
    }

    @Override
    public JwtPayload getPrincipal() {
      throw new IllegalStateException("Unable to get principal for a failed auth");
    }

    @Override
    public boolean isAuthenticated() {
      return false;
    }
  }
}
