package vn.gov.prison.secure.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "escape_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EscapeRequestJpaEntity {

    @Id
    private UUID id;

    @Column(name = "prisoner_id", nullable = false, length = 36)
    private String prisonerId;

    @Column(name = "request_date")
    private LocalDateTime requestDate;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(name = "planned_escape_date")
    private LocalDate plannedEscapeDate;

    @Column(length = 30)
    private String status;

    // Guard approval
    @Column(name = "guard_approval_date")
    private LocalDateTime guardApprovalDate;

    @Column(name = "guard_approval_notes", columnDefinition = "TEXT")
    private String guardApprovalNotes;

    @Column(name = "approved_by_guard")
    private UUID approvedByGuard;

    // Warden approval
    @Column(name = "warden_approval_date")
    private LocalDateTime wardenApprovalDate;

    @Column(name = "warden_approval_notes", columnDefinition = "TEXT")
    private String wardenApprovalNotes;

    @Column(name = "approved_by_warden")
    private UUID approvedByWarden;

    // Execution
    @Column(name = "execution_date")
    private LocalDateTime executionDate;

    @Column(name = "execution_notes", columnDefinition = "TEXT")
    private String executionNotes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private Integer version;
}
