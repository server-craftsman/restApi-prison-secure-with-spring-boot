package vn.gov.prison.secure.domain.model.booking;

import vn.gov.prison.secure.domain.model.common.BaseEntity;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Booking Aggregate Root
 * Manages the intake and booking process of prisoners
 * 
 * Following SOLID principles:
 * - SRP: Manages booking lifecycle and associated charges/court appearances
 * - OCP: Extensible for new booking types
 * - DIP: Depends on PrisonerId abstraction
 */
public class Booking extends BaseEntity<BookingId> {

    private PrisonerId prisonerId;
    private String bookingNumber;
    private LocalDateTime bookingDate;
    private BookingType type;
    private BookingStatus status;
    private String arrestingAgency;
    private String arrestLocation;
    private LocalDateTime arrestDate;
    private List<Charge> charges;
    private List<CourtAppearance> courtAppearances;
    private String bailAmount;
    private BailStatus bailStatus;
    private LocalDateTime expectedReleaseDate;
    private LocalDateTime actualReleaseDate;
    private String releaseReason;

    private Booking() {
        super();
        this.charges = new ArrayList<>();
        this.courtAppearances = new ArrayList<>();
        this.status = BookingStatus.PENDING;
        this.bailStatus = BailStatus.NOT_SET;
    }

    /**
     * Factory method for creating new booking
     */
    public static Booking create(PrisonerId prisonerId,
            String bookingNumber,
            BookingType type,
            String arrestingAgency) {
        if (prisonerId == null) {
            throw new IllegalArgumentException("Prisoner ID is required");
        }
        if (bookingNumber == null || bookingNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Booking number is required");
        }

        Booking booking = new Booking();
        booking.id = BookingId.generate();
        booking.prisonerId = prisonerId;
        booking.bookingNumber = bookingNumber;
        booking.type = type;
        booking.arrestingAgency = arrestingAgency;
        booking.bookingDate = LocalDateTime.now();

        return booking;
    }

    /**
     * Business logic: Add charge to booking
     */
    public void addCharge(Charge charge) {
        if (charge == null) {
            throw new IllegalArgumentException("Charge cannot be null");
        }
        if (status == BookingStatus.RELEASED || status == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Cannot add charges to " + status + " booking");
        }

        this.charges.add(charge);
        updateTimestamp();
    }

    /**
     * Business logic: Schedule court appearance
     */
    public void scheduleCourtAppearance(CourtAppearance appearance) {
        if (appearance == null) {
            throw new IllegalArgumentException("Court appearance cannot be null");
        }
        if (status == BookingStatus.RELEASED || status == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Cannot schedule court for " + status + " booking");
        }

        this.courtAppearances.add(appearance);
        updateTimestamp();
    }

    /**
     * Business logic: Complete booking process
     */
    public void complete() {
        if (charges.isEmpty()) {
            throw new IllegalStateException("Cannot complete booking without charges");
        }
        if (status != BookingStatus.PENDING) {
            throw new IllegalStateException("Can only complete pending bookings");
        }

        this.status = BookingStatus.ACTIVE;
        updateTimestamp();
    }

    /**
     * Business logic: Set bail
     */
    public void setBail(String amount) {
        if (amount == null || amount.trim().isEmpty()) {
            throw new IllegalArgumentException("Bail amount is required");
        }
        if (status != BookingStatus.ACTIVE) {
            throw new IllegalStateException("Can only set bail for active bookings");
        }

        this.bailAmount = amount;
        this.bailStatus = BailStatus.SET;
        updateTimestamp();
    }

    /**
     * Business logic: Post bail
     */
    public void postBail() {
        if (bailStatus != BailStatus.SET) {
            throw new IllegalStateException("Bail must be set before posting");
        }

        this.bailStatus = BailStatus.POSTED;
        updateTimestamp();
    }

    /**
     * Business logic: Release prisoner from booking
     */
    public void release(String reason) {
        if (status == BookingStatus.RELEASED) {
            throw new IllegalStateException("Booking already released");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Release reason is required");
        }

        this.status = BookingStatus.RELEASED;
        this.actualReleaseDate = LocalDateTime.now();
        this.releaseReason = reason;
        updateTimestamp();
    }

    /**
     * Business logic: Cancel booking
     */
    public void cancel(String reason) {
        if (status == BookingStatus.RELEASED) {
            throw new IllegalStateException("Cannot cancel released booking");
        }

        this.status = BookingStatus.CANCELLED;
        this.releaseReason = reason;
        updateTimestamp();
    }

    /**
     * Get highest severity charge
     */
    public Charge.ChargeSeverity getHighestChargeSeverity() {
        return charges.stream()
                .map(Charge::getSeverity)
                .max((s1, s2) -> Integer.compare(s1.getLevel(), s2.getLevel()))
                .orElse(Charge.ChargeSeverity.INFRACTION);
    }

    /**
     * Check if has pending court appearances
     */
    public boolean hasPendingCourtAppearances() {
        return courtAppearances.stream()
                .anyMatch(ca -> ca.getStatus() == CourtAppearance.AppearanceStatus.SCHEDULED);
    }

    // Getters
    public PrisonerId getPrisonerId() {
        return prisonerId;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public BookingType getType() {
        return type;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public String getArrestingAgency() {
        return arrestingAgency;
    }

    public String getArrestLocation() {
        return arrestLocation;
    }

    public void setArrestLocation(String arrestLocation) {
        this.arrestLocation = arrestLocation;
        updateTimestamp();
    }

    public LocalDateTime getArrestDate() {
        return arrestDate;
    }

    public void setArrestDate(LocalDateTime arrestDate) {
        this.arrestDate = arrestDate;
        updateTimestamp();
    }

    public List<Charge> getCharges() {
        return Collections.unmodifiableList(charges);
    }

    public List<CourtAppearance> getCourtAppearances() {
        return Collections.unmodifiableList(courtAppearances);
    }

    public String getBailAmount() {
        return bailAmount;
    }

    public BailStatus getBailStatus() {
        return bailStatus;
    }

    public LocalDateTime getExpectedReleaseDate() {
        return expectedReleaseDate;
    }

    public void setExpectedReleaseDate(LocalDateTime expectedReleaseDate) {
        this.expectedReleaseDate = expectedReleaseDate;
        updateTimestamp();
    }

    public LocalDateTime getActualReleaseDate() {
        return actualReleaseDate;
    }

    public String getReleaseReason() {
        return releaseReason;
    }

    public enum BookingType {
        NEW_ARREST("New Arrest"),
        VIOLATION("Violation"),
        WARRANT("Warrant"),
        TRANSFER("Transfer"),
        COURT_ORDER("Court Order");

        private final String displayName;

        BookingType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum BookingStatus {
        PENDING, ACTIVE, RELEASED, CANCELLED, ON_HOLD
    }

    public enum BailStatus {
        NOT_SET, SET, POSTED, DENIED, REVOKED
    }
}
