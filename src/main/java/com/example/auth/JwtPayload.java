package com.example.auth;

import java.util.Map;

public interface JwtPayload {

  <T> T claim(String name, Class<T> type);

  Map<String, Object> claims();
}
