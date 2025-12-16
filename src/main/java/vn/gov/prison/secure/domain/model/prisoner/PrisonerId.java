package vn.gov.prison.secure.domain.model.prisoner;

import vn.gov.prison.secure.domain.model.common.ValueObject;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object representing Prisoner's unique identifier
 * Immutable and self-validating
 */
public final class PrisonerId implements ValueObject {

    private final String value;

    private PrisonerId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("PrisonerId cannot be null or empty");
        }
        this.value = value;
    }

    public static PrisonerId of(String value) {
        return new PrisonerId(value);
    }

    public static PrisonerId generate() {
        return new PrisonerId(UUID.randomUUID().toString());
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
        PrisonerId that = (PrisonerId) o;
        return Objects.equals(value, that.value);
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
