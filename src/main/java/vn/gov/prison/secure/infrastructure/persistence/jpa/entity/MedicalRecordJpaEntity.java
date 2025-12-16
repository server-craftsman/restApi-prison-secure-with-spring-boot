package vn.gov.prison.secure.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JPA Entity for Medical Records
 */
@Entity
@Table(name = "medical_records")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordJpaEntity extends BaseJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "prisoner_id", nullable = false)
    private String prisonerId;

    @Column(name = "record_date", nullable = false)
    private LocalDateTime recordDate;

    @Column(name = "record_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private RecordTypeEnum recordType;

    @Column(name = "diagnosis", columnDefinition = "TEXT")
    private String diagnosis;

    @Column(name = "treatment", columnDefinition = "TEXT")
    private String treatment;

    @Column(name = "medical_staff", nullable = false)
    private String medicalStaff;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "severity", length = 20)
    @Enumerated(EnumType.STRING)
    private SeverityEnum severity;

    @Column(name = "follow_up_required")
    private Boolean followUpRequired;

    @Column(name = "follow_up_date")
    private LocalDate followUpDate;

    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PrescriptionJpaEntity> prescriptions = new ArrayList<>();

    public enum RecordTypeEnum {
        GENERAL, EMERGENCY, ROUTINE_CHECKUP, SPECIALIST, MENTAL_HEALTH, DENTAL, OTHER
    }

    public enum SeverityEnum {
        LOW, MEDIUM, HIGH, CRITICAL
    }
}
