package vn.gov.prison.secure.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;
import vn.gov.prison.secure.domain.model.booking.Booking;
import vn.gov.prison.secure.domain.model.booking.BookingId;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;
import vn.gov.prison.secure.infrastructure.persistence.jpa.entity.BookingJpaEntity;

@Component
public class BookingPersistenceMapper {

    public BookingJpaEntity toEntity(Booking domain) {
        if (domain == null) {
            return null;
        }

        return BookingJpaEntity.builder()
                .id(domain.getId().getValue())
                .prisonerId(domain.getPrisonerId().getValue())
                .bookingNumber(domain.getBookingNumber())
                .bookingDate(domain.getBookingDate())
                .type(domain.getType().name())
                .status(domain.getStatus().name())
                .arrestingAgency(domain.getArrestingAgency())
                .arrestLocation(domain.getArrestLocation())
                .arrestDate(domain.getArrestDate())
                .bailAmount(domain.getBailAmount())
                .bailStatus(domain.getBailStatus() != null ? domain.getBailStatus().name() : null)
                .expectedReleaseDate(domain.getExpectedReleaseDate())
                .actualReleaseDate(domain.getActualReleaseDate())
                .releaseReason(domain.getReleaseReason())
                .build();
    }

    public Booking toDomain(BookingJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return Booking.create(
                PrisonerId.of(entity.getPrisonerId()),
                entity.getBookingNumber(),
                Booking.BookingType.valueOf(entity.getType()),
                entity.getArrestingAgency());
    }
}
