package vn.gov.prison.secure.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * JPA Entity for Prescriptions
 */
@Entity
@Table(name = "prescriptions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionJpaEntity extends BaseJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "medical_record_id")
    private UUID medicalRecordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_record_id", insertable = false, updatable = false)
    private MedicalRecordJpaEntity medicalRecord;

    @Column(name = "prisoner_id", nullable = false)
    private String prisonerId;

    @Column(name = "medication_name", nullable = false)
    private String medicationName;

    @Column(name = "dosage", nullable = false, length = 100)
    private String dosage;

    @Column(name = "frequency", nullable = false, length = 100)
    private String frequency;

    @Column(name = "route", length = 50)
    @Enumerated(EnumType.STRING)
    private RouteEnum route;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "prescribing_doctor", nullable = false)
    private String prescribingDoctor;

    @Column(name = "pharmacy_notes", columnDefinition = "TEXT")
    private String pharmacyNotes;

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private PrescriptionStatusEnum status;

    @Column(name = "discontinue_reason", columnDefinition = "TEXT")
    private String discontinueReason;

    public enum RouteEnum {
        ORAL, INJECTION, TOPICAL, INTRAVENOUS, OTHER
    }

    public enum PrescriptionStatusEnum {
        ACTIVE, COMPLETED, DISCONTINUED, ON_HOLD
    }
}
