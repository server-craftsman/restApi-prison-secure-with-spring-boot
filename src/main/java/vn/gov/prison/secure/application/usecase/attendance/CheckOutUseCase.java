package vn.gov.prison.secure.application.usecase.attendance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.attendance.AttendanceResponse;
import vn.gov.prison.secure.domain.attendance.AttendanceRecord;
import vn.gov.prison.secure.domain.attendance.AttendanceRepository;
import vn.gov.prison.secure.domain.prisoner.PrisonerId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckOutUseCase {

    private final AttendanceRepository attendanceRepository;

    @Transactional
    public AttendanceResponse execute(UUID prisonerId) {
        PrisonerId id = PrisonerId.of(prisonerId);
        LocalDate today = LocalDate.now();

        // Find today's attendance record
        AttendanceRecord record = attendanceRepository
                .findByPrisonerAndDate(id, today)
                .orElseThrow(() -> new RuntimeException("No attendance record found for today"));

        // Perform check-out
        record.checkOut(LocalDateTime.now());

        // Save updated record
        AttendanceRecord saved = attendanceRepository.save(record);

        // Build response
        return AttendanceResponse.builder()
                .id(saved.getId().getValue())
                .prisonerId(saved.getPrisonerId().getValue())
                .attendanceDate(saved.getAttendanceDate())
                .checkInTime(saved.getCheckInTime())
                .checkOutTime(saved.getCheckOutTime())
                .expectedCheckInTime(saved.getExpectedCheckInTime())
                .expectedCheckOutTime(saved.getExpectedCheckOutTime())
                .status(saved.getStatus().name())
                .lateMinutes(saved.getLateMinutes())
                .vibrationLevel(saved.getVibrationLevel())
                .build();
    }
}
