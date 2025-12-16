package vn.gov.prison.secure.domain.model.scheduling;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Court Date entity
 * Represents detailed court appearance information
 */
@Getter
@Builder
public class CourtDate {
    private final UUID id;
    private final UUID scheduleId;
    private final String prisonerId;
    private final String courtName;
    private final String courtAddress;
    private final String caseNumber;
    private final HearingType hearingType;
    private final String judgeName;
    private final String attorneyName;
    private final String attorneyContact;
    private final String charges;
    private String outcome;
    private Verdict verdict;
    private String sentence;
    private LocalDate nextCourtDate;
    private boolean transportArranged;
    private String transportNotes;

    public enum HearingType {
        ARRAIGNMENT,
        PRELIMINARY,
        TRIAL,
        SENTENCING,
        APPEAL,
        PAROLE,
        OTHER
    }

    public enum Verdict {
        GUILTY,
        NOT_GUILTY,
        PENDING,
        DISMISSED,
        PLEA_BARGAIN
    }

    public void recordOutcome(String outcome, Verdict verdict, String sentence) {
        this.outcome = outcome;
        this.verdict = verdict;
        this.sentence = sentence;
    }

    public void scheduleNextCourtDate(LocalDate nextDate) {
        this.nextCourtDate = nextDate;
    }

    public void arrangeTransport(String notes) {
        this.transportArranged = true;
        this.transportNotes = notes;
    }
}
