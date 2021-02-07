package com.example.config;

import com.example.auth.JwtAuthenticationProvider;
import com.example.custom.MakeDecodedJwtFromAuthorizationHeader;
import com.example.custom.MakeJwtHandleFromDecodedJwt;
import java.util.function.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
public class SecurityConfig {

  @Bean
  SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    final var repo = createContextRepository();

    return http
               .authorizeExchange(e -> e.anyExchange().authenticated())
               .securityContextRepository(repo)
               .httpBasic().disable()
               .build();
  }

  private ServerSecurityContextRepository createContextRepository() {
    final var authProvider = JwtAuthenticationProvider.with(
        new MakeDecodedJwtFromAuthorizationHeader(),
        new MakeJwtHandleFromDecodedJwt()
    );

    return new StatelessServerSecurityContextRepository(authProvider::fromRequest);
  }

  private static class StatelessServerSecurityContextRepository
      implements ServerSecurityContextRepository {

    private final Function<ServerHttpRequest, Authentication> authFromRequest;

    public StatelessServerSecurityContextRepository(
        Function<ServerHttpRequest, Authentication> authFromRequest
    ) {
      this.authFromRequest = authFromRequest;
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
      return Mono.error(new RuntimeException("unexpected call of save(SecurityContext)"));
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
      return Mono.just(exchange.getRequest())
                 .map(authFromRequest)
                 .<SecurityContext>map(SecurityContextImpl::new)
                 .cache();
    }
  }
}
