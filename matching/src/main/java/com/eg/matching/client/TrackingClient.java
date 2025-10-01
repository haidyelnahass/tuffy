package com.eg.matching.client;

import com.eg.matching.client.model.response.NearbyLocationsResponse;
import com.eg.matching.exception.BadRequestException;
import com.eg.matching.exception.model.ErrorCode;
import com.eg.matching.exception.model.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class TrackingClient {

  private final WebClient webClient;

  public TrackingClient(WebClient.Builder webBuilder,
                        @Value("${tracking-client.url:http://localhost:8082}") String trackingClientUrl) {
    this.webClient = webBuilder.baseUrl(trackingClientUrl).build();
  }


  public NearbyLocationsResponse getNearbyDriverLocations(double lat,
                                                          double lon,
                                                          int radius,
                                                          int limit) {
    return webClient.get().uri("/v1/tracking/drivers/location?lat=" + lat +
        "&lon=" + lon + "&radius=" + radius + "&limit=" + limit)
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
        response -> response.bodyToMono(ErrorMessage.class) // <-- your error class
          .flatMap(errorBody ->
            Mono.error(new BadRequestException(errorBody.getMessage().toString(),
              ErrorCode.UNKNOWN_EXCEPTION)))
      )
      .bodyToMono(NearbyLocationsResponse.class)
      .block();
  }

}
