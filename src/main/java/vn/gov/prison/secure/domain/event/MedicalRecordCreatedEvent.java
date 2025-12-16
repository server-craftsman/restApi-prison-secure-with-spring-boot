package vn.gov.prison.secure.domain.event;

import lombok.Getter;
import vn.gov.prison.secure.domain.model.medical.MedicalRecordId;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class MedicalRecordCreatedEvent implements DomainEvent {
    private final MedicalRecordId medicalRecordId;
    private final PrisonerId prisonerId;
    private final String recordType;
    private final String severity;
    private final LocalDateTime occurredAt;
    
    public MedicalRecordCreatedEvent(MedicalRecordId medicalRecordId, PrisonerId prisonerId,
                                     String recordType, String severity) {
        this.medicalRecordId = medicalRecordId;
        this.prisonerId = prisonerId;
        this.recordType = recordType;
        this.severity = severity;
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
        return "MedicalRecordCreated";
    }
    
    @Override
    public String aggregateId() {
        return medicalRecordId.getValue().toString();
    }
}
