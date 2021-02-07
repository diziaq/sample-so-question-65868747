package com.example;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

@FullHarnessTest
final class TestDummy {

  @Autowired
  WebTestClient client;

  @Test
  void testWithAuth() {
    final String authorization =
        "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYXV0aG9yaXRpZXMiOlsiYSIsIkIiXSwianRpIjoiNTViNTQ4ZmYtODI5MS00NDdjLWIzNzctODU1OTdkNTBhYWE5IiwiaWF0IjoxNjEyNzEzOTgxLCJleHAiOjE2MTI3MTk3MDd9.RGqrhBeUp5UtZ5QY6MzbnAeLez0jzlCLGqnEtIpTs5c";

    client.get()
          .uri("/dummy")
          .header("AUTHORIZATION", authorization)
          .exchange()
          .expectStatus().isEqualTo(HttpStatus.OK)
          .expectBody()
          .jsonPath("myClaims.sub").isEqualTo(1234567890)
          .jsonPath("myClaims.name").isEqualTo("John Doe")
          .jsonPath("myClaims.authorities")
          .value(list -> assertThat((List<String>) list, containsInAnyOrder("a", "B")), List.class)
          .jsonPath("myClaims.exp").isEqualTo(1612719707)
          .jsonPath("myClaims.iat").isEqualTo(1612713981)
          .jsonPath("myClaims.jti").isEqualTo("55b548ff-8291-447c-b377-85597d50aaa9");
  }

  @Test
  void testWithoutAuth() {
    client.get()
          .uri("/dummy")
          .exchange()
          .expectStatus().isEqualTo(HttpStatus.FORBIDDEN)
          .expectBody()
          .jsonPath("$").isEqualTo("Access Denied");
  }

  @Test
  void testWithInvalidAuth() {
    client.get()
          .uri("/dummy")
          .header("AUTHORIZATION", "Bearer 1")
          .exchange()
          .expectStatus().isEqualTo(HttpStatus.FORBIDDEN)
          .expectBody()
          .jsonPath("$").isEqualTo("Access Denied");
  }
}
