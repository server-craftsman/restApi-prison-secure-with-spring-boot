package vn.gov.prison.secure.domain.model.booking;

import vn.gov.prison.secure.domain.model.common.ValueObject;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object representing Booking unique identifier
 */
public final class BookingId implements ValueObject {

    private final String value;

    private BookingId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("BookingId cannot be null or empty");
        }
        this.value = value;
    }

    public static BookingId of(String value) {
        return new BookingId(value);
    }

    public static BookingId generate() {
        return new BookingId(UUID.randomUUID().toString());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BookingId bookingId = (BookingId) o;
        return Objects.equals(value, bookingId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
