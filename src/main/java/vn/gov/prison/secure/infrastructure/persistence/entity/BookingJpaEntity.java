package vn.gov.prison.secure.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.gov.prison.secure.infrastructure.persistence.entity.BaseJpaEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingJpaEntity extends BaseJpaEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "prisoner_id", nullable = false, length = 36)
    private String prisonerId;

    @Column(name = "booking_number", nullable = false, unique = true, length = 50)
    private String bookingNumber;

    @Column(name = "booking_date", nullable = false)
    private LocalDateTime bookingDate;

    @Column(name = "booking_type", nullable = false, length = 50)
    private String type;

    @Column(name = "booking_status", nullable = false, length = 50)
    private String status;

    @Column(name = "arresting_agency", length = 255)
    private String arrestingAgency;

    @Column(name = "arrest_location", length = 255)
    private String arrestLocation;

    @Column(name = "arrest_date")
    private LocalDateTime arrestDate;

    @Column(name = "bail_amount", length = 50)
    private String bailAmount;

    @Column(name = "bail_status", length = 50)
    private String bailStatus;

    @Column(name = "expected_release_date")
    private LocalDateTime expectedReleaseDate;

    @Column(name = "actual_release_date")
    private LocalDateTime actualReleaseDate;

    @Column(name = "release_reason", columnDefinition = "TEXT")
    private String releaseReason;
}
