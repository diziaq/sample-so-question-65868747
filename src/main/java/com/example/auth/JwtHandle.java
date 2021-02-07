package com.example.auth;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public interface JwtHandle {

  JwtPayload payload();

  boolean isValid();

  Collection<GrantedAuthority> authorities();
}
