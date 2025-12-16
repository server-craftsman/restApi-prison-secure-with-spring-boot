package vn.gov.prison.secure.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.gov.prison.secure.application.dto.attendance.AttendanceResponse;
import vn.gov.prison.secure.application.dto.attendance.CheckInRequest;
import vn.gov.prison.secure.application.usecase.attendance.CheckInUseCase;
import vn.gov.prison.secure.application.usecase.attendance.CheckOutUseCase;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Attendance - Điểm danh", description = "API điểm danh hàng ngày cho tù nhân: Check-in/Check-out với máy rung")
public class AttendanceController {

    private final CheckInUseCase checkInUseCase;
    private final CheckOutUseCase checkOutUseCase;

    @PostMapping("/check-in")
    @PreAuthorize("hasAuthority('ATTENDANCE_CHECKIN')")
    @Operation(summary = "[ATT-001] Check-in hàng ngày", description = "Tù nhân check-in hàng ngày từ tablet. " +
            "Hệ thống tự động tính toán thời gian trễ và kích hoạt máy rung nếu cần. " +
            "Vibration Level: 0=đúng giờ, 1=trễ 5-15p, 2=trễ 15-30p, 3=trễ 30p+")
    public ResponseEntity<AttendanceResponse> checkIn(@Valid @RequestBody CheckInRequest request) {
        AttendanceResponse response = checkInUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/check-out/{prisonerId}")
    @PreAuthorize("hasAuthority('ATTENDANCE_CHECKOUT')")
    @Operation(summary = "[ATT-002] Check-out hàng ngày", description = "Tù nhân check-out khi kết thúc ngày. " +
            "Hoàn tất bản ghi điểm danh cho ngày hôm nay.")
    public ResponseEntity<AttendanceResponse> checkOut(@PathVariable UUID prisonerId) {
        AttendanceResponse response = checkOutUseCase.execute(prisonerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/prisoner/{prisonerId}/today")
    @PreAuthorize("hasAnyAuthority('ATTENDANCE_VIEW', 'ATTENDANCE_CHECKIN')")
    @Operation(summary = "[ATT-003] Xem điểm danh hôm nay", description = "Xem trạng thái điểm danh của tù nhân trong ngày hôm nay")
    public ResponseEntity<AttendanceResponse> getTodayAttendance(@PathVariable UUID prisonerId) {
        // TODO: Implement GetTodayAttendanceUseCase
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/missed")
    @PreAuthorize("hasAuthority('ATTENDANCE_VIEW')")
    @Operation(summary = "[ATT-004] Danh sách không điểm danh", description = "Lấy danh sách tù nhân chưa check-in trong ngày. "
            +
            "Dùng cho Quản giáo theo dõi.")
    public ResponseEntity<?> getMissedAttendance() {
        // TODO: Implement GetMissedAttendanceUseCase
        return ResponseEntity.ok().build();
    }
}
