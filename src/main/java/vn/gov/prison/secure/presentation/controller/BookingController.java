package vn.gov.prison.secure.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.gov.prison.secure.application.dto.booking.BookingResponse;
import vn.gov.prison.secure.application.dto.booking.CreateBookingRequest;
import vn.gov.prison.secure.application.dto.booking.ReleaseBookingRequest;
import vn.gov.prison.secure.application.usecase.booking.CreateBookingUseCase;
import vn.gov.prison.secure.application.usecase.booking.GetBookingByIdUseCase;
import vn.gov.prison.secure.application.usecase.booking.GetPrisonerBookingsUseCase;
import vn.gov.prison.secure.application.usecase.booking.ReleaseBookingUseCase;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Booking & Reception endpoints
 */
@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final CreateBookingUseCase createBookingUseCase;
    private final GetBookingByIdUseCase getBookingByIdUseCase;
    private final GetPrisonerBookingsUseCase getPrisonerBookingsUseCase;
    private final ReleaseBookingUseCase releaseBookingUseCase;

    /**
     * Create a new booking
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody CreateBookingRequest request) {
        BookingResponse response = createBookingUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get booking by ID
     */
    @GetMapping("/{bookingId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable UUID bookingId) {
        BookingResponse response = getBookingByIdUseCase.execute(bookingId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all bookings for a prisoner
     */
    @GetMapping("/prisoners/{prisonerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<BookingResponse>> getPrisonerBookings(
            @PathVariable UUID prisonerId) {
        List<BookingResponse> bookings = getPrisonerBookingsUseCase.execute(prisonerId);
        return ResponseEntity.ok(bookings);
    }

    /**
     * Release a booking
     */
    @PostMapping("/{bookingId}/release")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BookingResponse> releaseBooking(
            @PathVariable UUID bookingId,
            @Valid @RequestBody ReleaseBookingRequest request) {
        BookingResponse response = releaseBookingUseCase.execute(bookingId, request);
        return ResponseEntity.ok(response);
    }
}
