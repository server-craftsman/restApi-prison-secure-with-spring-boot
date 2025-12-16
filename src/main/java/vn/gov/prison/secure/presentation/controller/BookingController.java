package vn.gov.prison.secure.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.gov.prison.secure.application.dto.booking.*;
import vn.gov.prison.secure.application.usecase.booking.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking & Reception - Quản lý Tiếp nhận", description = "API quản lý tiếp nhận tù nhân, thả tù và lịch sử tiếp nhận")
public class BookingController {

    private final CreateBookingUseCase createBookingUseCase;
    private final GetBookingByIdUseCase getBookingByIdUseCase;
    private final GetPrisonerBookingsUseCase getPrisonerBookingsUseCase;
    private final ReleaseBookingUseCase releaseBookingUseCase;

    @PostMapping
    @Operation(summary = "[BOOK-001] Tạo hồ sơ tiếp nhận tù nhân mới", description = "Tạo hồ sơ tiếp nhận cho tù nhân mới bao gồm thông tin bắt giữ, loại tiếp nhận, cơ quan bắt giữ. "
            +
            "Hồ sơ sẽ được lưu với trạng thái PENDING và cần được xử lý tiếp.")
    public ResponseEntity<BookingResponse> createBooking(@RequestBody CreateBookingRequest request) {
        BookingResponse response = createBookingUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "[BOOK-002] Xem chi tiết hồ sơ tiếp nhận", description = "Lấy thông tin chi tiết của một hồ sơ tiếp nhận theo ID bao gồm trạng thái, thông tin bắt giữ, ngày thả dự kiến")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable UUID id) {
        BookingResponse response = getBookingByIdUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/prisoners/{prisonerId}")
    @Operation(summary = "[BOOK-003] Xem lịch sử tiếp nhận của tù nhân", description = "Lấy toàn bộ lịch sử tiếp nhận của một tù nhân bao gồm các lần vào tù, thả và tái phạm")
    public ResponseEntity<List<BookingResponse>> getPrisonerBookings(@PathVariable UUID prisonerId) {
        List<BookingResponse> bookings = getPrisonerBookingsUseCase.execute(prisonerId);
        return ResponseEntity.ok(bookings);
    }

    @PostMapping("/{id}/release")
    @Operation(summary = "[BOOK-004] Thả tù nhân", description = "Xử lý thả tù nhân bao gồm cập nhật trạng thái, ghi nhận lý do thả và ngày thả thực tế. "
            +
            "Hệ thống sẽ tự động hoàn trả tài sản và cập nhật hồ sơ.")
    public ResponseEntity<BookingResponse> releaseBooking(
            @PathVariable UUID id,
            @RequestBody ReleaseBookingRequest request) {
        BookingResponse response = releaseBookingUseCase.execute(id, request);
        return ResponseEntity.ok(response);
    }
}
