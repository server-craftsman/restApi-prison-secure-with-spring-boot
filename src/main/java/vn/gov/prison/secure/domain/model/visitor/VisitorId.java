package vn.gov.prison.secure.domain.model.visitor;

import vn.gov.prison.secure.domain.model.common.ValueObject;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object representing Visitor unique identifier
 */
public final class VisitorId implements ValueObject {

    private final String value;

    private VisitorId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("VisitorId cannot be null or empty");
        }
        this.value = value;
    }

    public static VisitorId of(String value) {
        return new VisitorId(value);
    }

    public static VisitorId generate() {
        return new VisitorId(UUID.randomUUID().toString());
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
        VisitorId visitorId = (VisitorId) o;
        return Objects.equals(value, visitorId.value);
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
