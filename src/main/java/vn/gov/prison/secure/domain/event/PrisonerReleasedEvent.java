package vn.gov.prison.secure.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain Event: Prisoner Released
 */
public record PrisonerReleasedEvent(
        String eventId,
        LocalDateTime occurredAt,
        String prisonerId,
        String prisonerNumber,
        LocalDateTime releaseDate,
        String releaseReason) implements DomainEvent {

    public PrisonerReleasedEvent(String prisonerId, String prisonerNumber,
            String releaseReason) {
        this(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                prisonerId,
                prisonerNumber,
                LocalDateTime.now(),
                releaseReason);
    }

    @Override
    public String eventType() {
        return "PrisonerReleased";
    }

    @Override
    public String aggregateId() {
        return prisonerId;
    }
}
