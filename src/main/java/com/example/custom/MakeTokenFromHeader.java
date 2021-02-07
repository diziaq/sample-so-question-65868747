package com.example.custom;

import java.util.Optional;
import java.util.function.Function;
import org.springframework.http.HttpHeaders;

abstract class MakeTokenFromHeader<T> implements Function<HttpHeaders, T> {

  private final String headerName;
  private final String tokenPrefix;
  private final Function<String, T> parseToken;

  /* default */ MakeTokenFromHeader(
      String headerName, String tokenPrefix, Function<String, T> parseToken
  ) {
    this.headerName = headerName;
    this.tokenPrefix = tokenPrefix;
    this.parseToken = parseToken;
  }

  public T apply(HttpHeaders headers) {
    return Optional.of(headers)
                   .map(x -> x.getFirst(headerName))
                   .filter(x -> x.startsWith(tokenPrefix))
                   .map(x -> x.substring(tokenPrefix.length()))
                   .map(parseToken)
                   .orElseThrow(() -> new RuntimeException("Unable to parse token"));
  }
}
