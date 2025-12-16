package vn.gov.prison.secure.domain.attendance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import vn.gov.prison.secure.domain.prisoner.PrisonerId;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AttendanceRecord {
    private AttendanceId id;
    private PrisonerId prisonerId;
    private LocalDate attendanceDate;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private LocalDateTime expectedCheckInTime;
    private LocalDateTime expectedCheckOutTime;
    private AttendanceStatus status;
    private Integer lateMinutes;
    private Integer vibrationLevel; // 0-3
    private Boolean vibrationTriggered;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AttendanceRecord create(
            PrisonerId prisonerId,
            LocalDate attendanceDate,
            LocalDateTime expectedCheckInTime,
            LocalDateTime expectedCheckOutTime) {
        return AttendanceRecord.builder()
                .id(AttendanceId.generate())
                .prisonerId(prisonerId)
                .attendanceDate(attendanceDate)
                .expectedCheckInTime(expectedCheckInTime)
                .expectedCheckOutTime(expectedCheckOutTime)
                .status(AttendanceStatus.PENDING)
                .lateMinutes(0)
                .vibrationLevel(0)
                .vibrationTriggered(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void checkIn(LocalDateTime checkInTime) {
        if (this.checkInTime != null) {
            throw new IllegalStateException("Already checked in");
        }

        this.checkInTime = checkInTime;
        this.lateMinutes = calculateLateMinutes(checkInTime, expectedCheckInTime);
        this.status = determineStatus(this.lateMinutes);
        this.vibrationLevel = calculateVibrationLevel(this.lateMinutes);
        this.updatedAt = LocalDateTime.now();
    }

    public void checkOut(LocalDateTime checkOutTime) {
        if (this.checkInTime == null) {
            throw new IllegalStateException("Must check in before check out");
        }
        if (this.checkOutTime != null) {
            throw new IllegalStateException("Already checked out");
        }

        this.checkOutTime = checkOutTime;
        this.status = AttendanceStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    public void triggerVibration() {
        if (vibrationLevel > 0) {
            this.vibrationTriggered = true;
            this.updatedAt = LocalDateTime.now();
        }
    }

    private Integer calculateLateMinutes(LocalDateTime actual, LocalDateTime expected) {
        if (actual.isBefore(expected) || actual.isEqual(expected)) {
            return 0;
        }
        return (int) Duration.between(expected, actual).toMinutes();
    }

    private AttendanceStatus determineStatus(Integer lateMinutes) {
        if (lateMinutes == 0) {
            return AttendanceStatus.ON_TIME;
        }
        return AttendanceStatus.LATE;
    }

    private Integer calculateVibrationLevel(Integer lateMinutes) {
        if (lateMinutes == 0)
            return 0;
        if (lateMinutes <= 15)
            return 1; // 0-15 phút: Level 1 (nhẹ)
        if (lateMinutes <= 30)
            return 2; // 15-30 phút: Level 2 (trung bình)
        return 3; // 30+ phút: Level 3 (mạnh)
    }

    public void markAsMissed() {
        if (this.checkInTime == null && LocalDateTime.now().isAfter(expectedCheckInTime.plusHours(1))) {
            this.status = AttendanceStatus.MISSED;
            this.vibrationLevel = 3; // Maximum vibration for missed check-in
            this.updatedAt = LocalDateTime.now();
        }
    }

    public boolean needsVibration() {
        return vibrationLevel > 0 && !vibrationTriggered;
    }
}
