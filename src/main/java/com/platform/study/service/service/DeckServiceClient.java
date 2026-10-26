package com.platform.study.service.service;

import com.platform.study.service.model.response.DeckOwnerResponse;
import com.platform.study.service.model.response.DueCard;

import java.util.List;

public interface DeckServiceClient {

    DeckOwnerResponse getDeckOwner(String deckId);

    List<DueCard> getDueCards(String deckId, int limit);
}
