package com.example.custom;

import com.example.auth.JwtHandle;
import com.example.auth.JwtPayload;
import java.time.Instant;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

final class JwtHandleSimple implements JwtHandle {

  private final JwtPayload payload;
  private final String[] authorities;

  /* default */ JwtHandleSimple(JwtPayload payload) {
    this.payload = payload;
    this.authorities = payload.claim("authorities", String[].class);
  }

  @Override
  public JwtPayload payload() {
    return payload;
  }

  @Override
  public boolean isValid() {
    return authorities.length != 0;
  }

  @Override
  public Collection<GrantedAuthority> authorities() {
    return Stream.of(authorities)
                 .filter(Objects::nonNull)
                 .map(SimpleGrantedAuthority::new)
                 .collect(Collectors.toUnmodifiableList());
  }
}
