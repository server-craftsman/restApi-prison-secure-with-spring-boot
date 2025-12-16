package vn.gov.prison.secure.domain.model.scheduling;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.gov.prison.secure.domain.model.common.ValueObject;

import java.util.UUID;

/**
 * Value object representing a schedule identifier
 */
@Getter
@EqualsAndHashCode
public class ScheduleId implements ValueObject {
    private final UUID value;

    private ScheduleId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("Schedule ID cannot be null");
        }
        this.value = value;
    }

    public static ScheduleId of(UUID value) {
        return new ScheduleId(value);
    }

    public static ScheduleId generate() {
        return new ScheduleId(UUID.randomUUID());
    }

    public static ScheduleId fromString(String value) {
        return new ScheduleId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
