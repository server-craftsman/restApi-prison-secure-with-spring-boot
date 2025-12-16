package vn.gov.prison.secure.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * JPA Entity for Court Dates
 */
@Entity
@Table(name = "court_dates")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourtDateJpaEntity extends BaseJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "schedule_id", nullable = false)
    private UUID scheduleId;

    @Column(name = "prisoner_id", nullable = false, length = 36)
    private String prisonerId;

    @Column(name = "court_name", nullable = false)
    private String courtName;

    @Column(name = "court_address", columnDefinition = "TEXT")
    private String courtAddress;

    @Column(name = "case_number", length = 100)
    private String caseNumber;

    @Column(name = "hearing_type", nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private HearingTypeEnum hearingType;

    @Column(name = "judge_name")
    private String judgeName;

    @Column(name = "attorney_name")
    private String attorneyName;

    @Column(name = "attorney_contact")
    private String attorneyContact;

    @Column(name = "charges", columnDefinition = "TEXT")
    private String charges;

    @Column(name = "outcome", columnDefinition = "TEXT")
    private String outcome;

    @Column(name = "verdict", length = 100)
    @Enumerated(EnumType.STRING)
    private VerdictEnum verdict;

    @Column(name = "sentence", columnDefinition = "TEXT")
    private String sentence;

    @Column(name = "next_court_date")
    private LocalDate nextCourtDate;

    @Column(name = "transport_arranged")
    private Boolean transportArranged;

    @Column(name = "transport_notes", columnDefinition = "TEXT")
    private String transportNotes;

    public enum HearingTypeEnum {
        ARRAIGNMENT, PRELIMINARY, TRIAL, SENTENCING, APPEAL, PAROLE, OTHER
    }

    public enum VerdictEnum {
        GUILTY, NOT_GUILTY, PENDING, DISMISSED, PLEA_BARGAIN
    }
}
