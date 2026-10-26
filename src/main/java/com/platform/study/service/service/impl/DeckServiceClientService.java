package com.platform.study.service.service.impl;

import com.platform.study.service.exception.ForbiddenException;
import com.platform.study.service.exception.ResourceNotFoundException;
import com.platform.study.service.exception.ServiceUnavailableException;
import com.platform.study.service.model.response.DeckOwnerResponse;
import com.platform.study.service.model.response.DueCard;
import com.platform.study.service.service.DeckServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.util.List;


@Service
@Slf4j
public class DeckServiceClientService implements DeckServiceClient {
    private final WebClient webClient;

    private static final String getOwnerUrl = "/internal/decks/{deckId}/owner";
    private static final String getDueCardsUrl = "/internal/decks/{deckId}/due-cards";

    public DeckServiceClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public DeckOwnerResponse getDeckOwner(String deckId) {

        DeckOwnerResponse deckOwnerResponse = webClient.get()
                .uri(getOwnerUrl, deckId)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        resp -> Mono.error(new ResourceNotFoundException("Deck not found: " + deckId)))
                .onStatus(HttpStatusCode::is5xxServerError,
                        resp -> Mono.error(new ServiceUnavailableException("5xx error has been thrown")
                ))
                .bodyToMono(DeckOwnerResponse.class)
                .block();

        return deckOwnerResponse;
    }

    @Override
    public List<DueCard> getDueCards(String deckId, int limit) {

        List<DueCard> dueCardList = webClient.get()
                .uri(getDueCardsUrl, deckId)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        resp -> Mono.error(new ResourceNotFoundException("No cards found")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        resp -> Mono.error(new ServiceUnavailableException("5xx error has been thrown while fetcing cards")))
                .bodyToMono(new ParameterizedTypeReference<List<DueCard>>() {})
                .block();


        return dueCardList;
    }
}
