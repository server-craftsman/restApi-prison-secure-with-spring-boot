package vn.gov.prison.secure.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain Event: Prisoner Admitted
 * Published when a new prisoner is admitted to the facility
 */
public record PrisonerAdmittedEvent(
        String eventId,
        LocalDateTime occurredAt,
        String prisonerId,
        String prisonerNumber,
        String fullName,
        LocalDateTime admissionDate,
        String facility) implements DomainEvent {

    public PrisonerAdmittedEvent(String prisonerId, String prisonerNumber,
            String fullName, String facility) {
        this(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                prisonerId,
                prisonerNumber,
                fullName,
                LocalDateTime.now(),
                facility);
    }

    @Override
    public String eventType() {
        return "PrisonerAdmitted";
    }

    @Override
    public String aggregateId() {
        return prisonerId;
    }
}
