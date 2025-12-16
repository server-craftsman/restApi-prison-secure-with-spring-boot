package vn.gov.prison.secure.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.gov.prison.secure.application.dto.scheduling.CreateScheduleRequest;
import vn.gov.prison.secure.application.dto.scheduling.ScheduleResponse;
import vn.gov.prison.secure.application.dto.scheduling.UpdateScheduleRequest;
import vn.gov.prison.secure.application.usecase.scheduling.CancelScheduleUseCase;
import vn.gov.prison.secure.application.usecase.scheduling.CreateScheduleUseCase;
import vn.gov.prison.secure.application.usecase.scheduling.GetPrisonerScheduleUseCase;
import vn.gov.prison.secure.application.usecase.scheduling.UpdateScheduleUseCase;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "Scheduling - Quản lý Lịch trình", description = "API quản lý lịch trình tù nhân: lịch hầu tòa, lịch khám bệnh, lịch hoạt động")
public class SchedulingController {

    private final CreateScheduleUseCase createScheduleUseCase;
    private final UpdateScheduleUseCase updateScheduleUseCase;
    private final GetPrisonerScheduleUseCase getPrisonerScheduleUseCase;
    private final CancelScheduleUseCase cancelScheduleUseCase;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(summary = "[SCH-001] Tạo lịch trình mới", description = "Tạo lịch trình mới cho tù nhân bao gồm: lịch hầu tòa, lịch khám bệnh, lịch hoạt động. "
            +
            "Hệ thống tự động kiểm tra xung đột lịch trình.")
    public ResponseEntity<ScheduleResponse> createSchedule(@Valid @RequestBody CreateScheduleRequest request) {
        ScheduleResponse response = createScheduleUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{scheduleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(summary = "[SCH-002] Cập nhật lịch trình", description = "Cập nhật thông tin lịch trình hiện có: thời gian, địa điểm, ghi chú. "
            +
            "Tự động thông báo cho các bên liên quan khi có thay đổi.")
    public ResponseEntity<ScheduleResponse> updateSchedule(
            @PathVariable UUID scheduleId,
            @Valid @RequestBody UpdateScheduleRequest request) {
        ScheduleResponse response = updateScheduleUseCase.execute(scheduleId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/prisoners/{prisonerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(summary = "[SCH-003] Xem lịch trình của tù nhân", description = "Lấy tất cả lịch trình của tù nhân theo thời gian. "
            +
            "Bao gồm lịch sắp tới, lịch đã hoàn thành và lịch bị hủy.")
    public ResponseEntity<List<ScheduleResponse>> getPrisonerSchedules(@PathVariable UUID prisonerId) {
        List<ScheduleResponse> schedules = getPrisonerScheduleUseCase.execute(prisonerId);
        return ResponseEntity.ok(schedules);
    }

    @DeleteMapping("/{scheduleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(summary = "[SCH-004] Hủy lịch trình", description = "Hủy lịch trình đã đặt. Cập nhật trạng thái và gửi thông báo hủy cho các bên liên quan.")
    public ResponseEntity<Void> cancelSchedule(@PathVariable UUID scheduleId) {
        cancelScheduleUseCase.execute(scheduleId);
        return ResponseEntity.noContent().build();
    }
}
