package com.eg.ride.client;

import com.eg.ride.client.model.response.DriverLocationResponse;
import com.eg.ride.exception.BadRequestException;
import com.eg.ride.exception.model.ErrorCode;
import com.eg.ride.exception.model.ErrorMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class TrackingClient {

  private final WebClient webClient;

  public TrackingClient(WebClient.Builder webBuilder,
                        @Value("${tracking-client.url:http://localhost:8082}") String trackingClientUrl) {
    this.webClient = webBuilder.baseUrl(trackingClientUrl).build();
  }


  public Mono<DriverLocationResponse> getDriverLocation(String authorization,
                                                  Long userId,
                                                  Long driverId) {
    return webClient.get().uri("/v1/tracking/drivers/"+ driverId + "/location")
      .header(AUTHORIZATION, authorization)
      .header("UserId", userId.toString())
      .retrieve()
      .onStatus(
        HttpStatusCode::is4xxClientError,
        response -> response.bodyToMono(ErrorMessage.class) // <-- your error class
          .flatMap(errorBody ->
            Mono.error(new BadRequestException(errorBody.getMessage().toString(),
              ErrorCode.UNKNOWN_EXCEPTION)))
      )
      .onStatus(
        HttpStatusCode::is5xxServerError,
        response -> Mono.error(new RuntimeException("Server error!"))
      )
      .bodyToMono(DriverLocationResponse.class);
  }

}
