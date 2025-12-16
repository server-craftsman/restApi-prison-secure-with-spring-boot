package vn.gov.prison.secure.domain.escape;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import vn.gov.prison.secure.domain.prisoner.PrisonerId;
import vn.gov.prison.secure.domain.user.UserId;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class EscapeRequest {
    private EscapeRequestId id;
    private PrisonerId prisonerId;
    private LocalDateTime requestDate;
    private String reason;
    private LocalDate plannedEscapeDate;
    private EscapeRequestStatus status;

    // Guard approval
    private LocalDateTime guardApprovalDate;
    private String guardApprovalNotes;
    private UserId approvedByGuard;

    // Warden approval
    private LocalDateTime wardenApprovalDate;
    private String wardenApprovalNotes;
    private UserId approvedByWarden;

    // Execution
    private LocalDateTime executionDate;
    private String executionNotes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static EscapeRequest create(
            PrisonerId prisonerId,
            String reason,
            LocalDate plannedEscapeDate) {
        return EscapeRequest.builder()
                .id(EscapeRequestId.generate())
                .prisonerId(prisonerId)
                .reason(reason)
                .plannedEscapeDate(plannedEscapeDate)
                .requestDate(LocalDateTime.now())
                .status(EscapeRequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void approveByGuard(UserId guardId, String notes) {
        if (status != EscapeRequestStatus.PENDING) {
            throw new IllegalStateException("Can only approve pending requests");
        }

        this.approvedByGuard = guardId;
        this.guardApprovalDate = LocalDateTime.now();
        this.guardApprovalNotes = notes;
        this.status = EscapeRequestStatus.GUARD_APPROVED;
        this.updatedAt = LocalDateTime.now();
    }

    public void rejectByGuard(UserId guardId, String notes) {
        if (status != EscapeRequestStatus.PENDING) {
            throw new IllegalStateException("Can only reject pending requests");
        }

        this.approvedByGuard = guardId;
        this.guardApprovalDate = LocalDateTime.now();
        this.guardApprovalNotes = notes;
        this.status = EscapeRequestStatus.GUARD_REJECTED;
        this.updatedAt = LocalDateTime.now();
    }

    public void approveByWarden(UserId wardenId, String notes) {
        if (status != EscapeRequestStatus.GUARD_APPROVED) {
            throw new IllegalStateException("Guard must approve first");
        }

        this.approvedByWarden = wardenId;
        this.wardenApprovalDate = LocalDateTime.now();
        this.wardenApprovalNotes = notes;
        this.status = EscapeRequestStatus.WARDEN_APPROVED;
        this.updatedAt = LocalDateTime.now();
    }

    public void rejectByWarden(UserId wardenId, String notes) {
        if (status != EscapeRequestStatus.GUARD_APPROVED) {
            throw new IllegalStateException("Guard must approve first");
        }

        this.approvedByWarden = wardenId;
        this.wardenApprovalDate = LocalDateTime.now();
        this.wardenApprovalNotes = notes;
        this.status = EscapeRequestStatus.WARDEN_REJECTED;
        this.updatedAt = LocalDateTime.now();
    }

    public void execute(String executionNotes) {
        if (status != EscapeRequestStatus.WARDEN_APPROVED) {
            throw new IllegalStateException("Warden must approve before execution");
        }

        this.executionDate = LocalDateTime.now();
        this.executionNotes = executionNotes;
        this.status = EscapeRequestStatus.EXECUTED;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isPending() {
        return status == EscapeRequestStatus.PENDING;
    }

    public boolean isApprovedByGuard() {
        return status == EscapeRequestStatus.GUARD_APPROVED;
    }

    public boolean isFullyApproved() {
        return status == EscapeRequestStatus.WARDEN_APPROVED;
    }

    public boolean isRejected() {
        return status == EscapeRequestStatus.GUARD_REJECTED ||
                status == EscapeRequestStatus.WARDEN_REJECTED;
    }
}
