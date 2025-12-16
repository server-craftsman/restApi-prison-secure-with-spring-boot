package vn.gov.prison.secure.application.usecase.attendance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.attendance.CheckInRequest;
import vn.gov.prison.secure.application.dto.attendance.AttendanceResponse;
import vn.gov.prison.secure.domain.attendance.AttendanceRecord;
import vn.gov.prison.secure.domain.attendance.AttendanceRepository;
import vn.gov.prison.secure.domain.prisoner.PrisonerId;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CheckInUseCase {

    private final AttendanceRepository attendanceRepository;

    @Transactional
    public AttendanceResponse execute(CheckInRequest request) {
        PrisonerId prisonerId = PrisonerId.of(request.getPrisonerId());
        LocalDate today = LocalDate.now();
        LocalDateTime checkInTime = request.getCheckInTime() != null ? request.getCheckInTime() : LocalDateTime.now();

        // Find today's attendance record
        AttendanceRecord record = attendanceRepository
                .findByPrisonerAndDate(prisonerId, today)
                .orElseThrow(() -> new RuntimeException("No attendance record found for today"));

        // Perform check-in
        record.checkIn(checkInTime);

        // Trigger vibration if late
        if (record.needsVibration()) {
            record.triggerVibration();
            // TODO: Send vibration signal to tablet
        }

        // Save updated record
        AttendanceRecord saved = attendanceRepository.save(record);

        // Build response
        return buildResponse(saved);
    }

    private AttendanceResponse buildResponse(AttendanceRecord record) {
        String vibrationMessage = getVibrationMessage(record.getVibrationLevel());

        return AttendanceResponse.builder()
                .id(record.getId().getValue())
                .prisonerId(record.getPrisonerId().getValue())
                .attendanceDate(record.getAttendanceDate())
                .checkInTime(record.getCheckInTime())
                .expectedCheckInTime(record.getExpectedCheckInTime())
                .status(record.getStatus().name())
                .lateMinutes(record.getLateMinutes())
                .vibrationLevel(record.getVibrationLevel())
                .vibrationTriggered(record.getVibrationTriggered())
                .vibrationMessage(vibrationMessage)
                .build();
    }

    private String getVibrationMessage(Integer level) {
        return switch (level) {
            case 0 -> "Check-in đúng giờ! Tốt lắm!";
            case 1 -> "Trễ 5-15 phút. Rung nhẹ.";
            case 2 -> "Trễ 15-30 phút. Rung trung bình.";
            case 3 -> "Trễ hơn 30 phút. Rung mạnh!";
            default -> "";
        };
    }
}
