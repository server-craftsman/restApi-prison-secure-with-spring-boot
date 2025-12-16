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
@Table(name = "attendance_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRecordJpaEntity {

    @Id
    private UUID id;

    @Column(name = "prisoner_id", nullable = false, length = 36)
    private String prisonerId;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    @Column(name = "expected_check_in_time", nullable = false)
    private LocalDateTime expectedCheckInTime;

    @Column(name = "expected_check_out_time", nullable = false)
    private LocalDateTime expectedCheckOutTime;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "late_minutes")
    private Integer lateMinutes;

    @Column(name = "vibration_level")
    private Integer vibrationLevel;

    @Column(name = "vibration_triggered")
    private Boolean vibrationTriggered;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private Integer version;
}
