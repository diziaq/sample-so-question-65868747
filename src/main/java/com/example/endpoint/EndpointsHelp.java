package com.example.endpoint;

import com.example.auth.JwtPayload;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class EndpointsHelp {

  @GetMapping("dummy")
  @ResponseStatus(HttpStatus.OK)
  public Mono<?> getDummyA(@AuthenticationPrincipal JwtPayload jwt) {
    return Mono.just(Map.of("myClaims", jwt.claims()));
  }
}
