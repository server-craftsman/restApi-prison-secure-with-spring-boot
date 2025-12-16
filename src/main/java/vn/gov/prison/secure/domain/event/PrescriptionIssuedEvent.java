package vn.gov.prison.secure.domain.event;

import lombok.Getter;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class PrescriptionIssuedEvent implements DomainEvent {
    private final UUID prescriptionId;
    private final PrisonerId prisonerId;
    private final String medicationName;
    private final String prescribingDoctor;
    private final LocalDateTime occurredAt;
    
    public PrescriptionIssuedEvent(UUID prescriptionId, PrisonerId prisonerId,
                                   String medicationName, String prescribingDoctor) {
        this.prescriptionId = prescriptionId;
        this.prisonerId = prisonerId;
        this.medicationName = medicationName;
        this.prescribingDoctor = prescribingDoctor;
        this.occurredAt = LocalDateTime.now();
    }
    
    @Override
    public String eventId() {
        return UUID.randomUUID().toString();
    }
    
    @Override
    public LocalDateTime occurredAt() {
        return occurredAt;
    }
    
    @Override
    public String eventType() {
        return "PrescriptionIssued";
    }
    
    @Override
    public String aggregateId() {
        return prescriptionId.toString();
    }
}
