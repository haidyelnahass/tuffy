package com.eg.ride.client;

import com.eg.ride.client.model.response.ProfileDetailsResponse;
import com.eg.ride.exception.BadRequestException;
import com.eg.ride.exception.model.ErrorCode;
import com.eg.ride.exception.model.ErrorMessage;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.eg.ride.util.Constants.USER_ID_HEADER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class UserClient {

  private final WebClient webClient;

  public UserClient(WebClient.Builder webBuilder) {
    this.webClient = webBuilder.baseUrl("http://localhost:8080").build();
  }

  public ProfileDetailsResponse getUserDetails(String authorization, Long userId) {
      return webClient.get().uri("/v1/users/profile")
        .header(AUTHORIZATION, authorization)
        .header(USER_ID_HEADER, userId.toString())
        .retrieve()
        .onStatus(
          HttpStatusCode::is4xxClientError,
          response -> response.bodyToMono(ErrorMessage.class) // <-- your error class
              .flatMap(errorBody ->
                Mono.error(new BadRequestException(errorBody.getMessage().toString(), ErrorCode.UNKNOWN_EXCEPTION)))
        )
        .onStatus(
          HttpStatusCode::is5xxServerError,
          response -> Mono.error(new RuntimeException("Server error!"))
        )
        .bodyToMono(ProfileDetailsResponse.class)
        .block();

  }
}
