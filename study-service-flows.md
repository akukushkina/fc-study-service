# Study Service — Flow Diagrams

---

## 1. Start Study Session

```mermaid
flowchart TD
    A([POST /api/v1/sessions\nBody: deckId]) --> B[Extract X-User-Id header]
    B --> C{Feign: GET /internal/decks/deckId/owner\nDoes deck exist?}
    C -- No / 404 --> D[Return HTTP 404\nDeck not found]
    C -- Yes --> E{Deck ownerId\n== X-User-Id?}
    E -- No --> F[Return HTTP 403\nForbidden]
    E -- Yes --> G[Feign: GET /internal/decks/deckId/due-cards?limit=50]
    G --> H{Due cards\nreturned > 0?}
    H -- No --> I[Return HTTP 200\nEmpty card list\nMessage: no cards due]
    H -- Yes --> J[Create StudySession document\nstatus = ACTIVE\nstartedAt = now\nstorecard IDs in session]
    J --> K[Return HTTP 201\nSession ID + ordered list\nof due cards]
```

---

## 2. Submit Card Review

```mermaid
flowchart TD
    A([POST /api/v1/sessions/sessionId/reviews\nBody: cardId, qualityRating]) --> B{Session exists\nin MongoDB?}
    B -- No --> C[Return HTTP 404\nSession not found]
    B -- Yes --> D{Session userId\n== X-User-Id?}
    D -- No --> E[Return HTTP 403\nForbidden]
    D -- Yes --> F{Session status\n== ACTIVE?}
    F -- No --> G[Return HTTP 409\nSession not active]
    F -- Yes --> H{qualityRating\nbetween 0 and 5?}
    H -- No --> I[Return HTTP 400\nInvalid quality rating]
    H -- Yes --> J{cardId belongs\nto this session?}
    J -- No --> K[Return HTTP 409\nCard not in session]
    J -- Yes --> L{Card already\nreviewed in\nthis session?}
    L -- Yes --> M[Return HTTP 409\nCard already reviewed]
    L -- No --> N{qualityRating\n< 3?}
    N -- Yes --> O[SM-2 failure branch\nnewInterval = 1\nnewEF = max 1.3 current EF]
    N -- No --> P[SM-2 success branch\nnewInterval = max 1 round interval × EF\nnewEF = max 1.3 EF + delta formula]
    O --> Q[Feign: PATCH /internal/cards/cardId/sm2\nUpdate interval, EF, nextReviewDate]
    P --> Q
    Q --> R[Persist Review document\nwith before/after SM-2 state]
    R --> S{All cards in\nsession reviewed?}
    S -- No --> T[Return HTTP 201\nReview recorded]
    S -- Yes --> U[Mark session COMPLETED\nRecord completedAt timestamp]
    U --> V[Publish study.session.completed\nevent to Kafka]
    V --> W[Publish card.reviewed event\nto Kafka for each reviewed card]
    W --> T
```

---

## 3. Get Session State

```mermaid
flowchart TD
    A([GET /api/v1/sessions/sessionId]) --> B{Session exists\nin MongoDB?}
    B -- No --> C[Return HTTP 404\nSession not found]
    B -- Yes --> D{Session userId\n== X-User-Id?}
    D -- No --> E[Return HTTP 403\nForbidden]
    D -- Yes --> F[Return HTTP 200\nSession state: status, startedAt,\ncompletedAt, totalCards,\navgQualityRating]
```

---

## 4. SM-2 Algorithm (internal decision logic)

```mermaid
flowchart TD
    A([Sm2Calculator.calculate\ninterval, easeFactor, qualityRating]) --> B{qualityRating\n∈ 0 1 2 ?}
    B -- Yes --> C[newInterval = 1\nnewEaseFactor = max 1.3 easeFactor\nEF does NOT decrease]
    B -- No --> D{qualityRating\n∈ 3 4 5 ?}
    D -- Yes --> E[newInterval = max 1 round interval × easeFactor\nΔ = 0.1 - 5-q × 0.08 + 5-q × 0.02\nnewEaseFactor = max 1.3 easeFactor + Δ]
    D -- No --> F[Throw IllegalArgumentException\nRating out of range]
    C --> G[nextReviewDate = today + newInterval days]
    E --> G
    G --> H[Return Sm2Result\nnewInterval, newEaseFactor\nnextReviewDate]
```

---

## 5. Kafka Event Publishing (on session completion)

```mermaid
flowchart TD
    A([Session marked COMPLETED]) --> B[Build study.session.completed\nevent payload with traceId]
    B --> C{Kafka broker\navailable?}
    C -- Yes --> D[Publish event\nidempotent producer]
    D --> E[Publish card.reviewed event\nfor each review in session]
    E --> F[Done]
    C -- No --> G{Retry attempt\n≤ 3?}
    G -- Yes --> H[Wait exponential backoff\n100ms → 200ms → 400ms]
    H --> C
    G -- No --> I[Log ERROR with full context\nIncrement kafka.publish.failures metric\nContinue — non-fatal]
```
