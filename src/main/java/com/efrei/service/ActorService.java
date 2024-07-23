package com.efrei.service;

import java.util.List;

import com.efrei.dto.Actor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ActorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActorService.class);

    private final CircuitBreakerFactory circuitBreakerFactory;

    public List<Actor> getMovieActors(String movieTitle) {
        var restTemplate = new RestTemplate();

        return circuitBreakerFactory.create("getMovieActors").run(
            () -> restTemplate.exchange(
                "http://localhost:8081/actors/" + movieTitle,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Actor>>() {
                }).getBody(),
            t -> {
                LOGGER.error("getMovieActors call failed", t);
                return defaultActors();
            }
        );
    }

    private static List<Actor> defaultActors() {
        return List.of(new Actor("Doe", "John", null));
    }

}
