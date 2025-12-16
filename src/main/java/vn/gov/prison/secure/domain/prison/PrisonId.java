package vn.gov.prison.secure.domain.prison;

import lombok.Value;
import java.util.UUID;

@Value
public class PrisonId {
    UUID value;

    public static PrisonId of(UUID value) {
        return new PrisonId(value);
    }

    public static PrisonId of(String value) {
        return new PrisonId(UUID.fromString(value));
    }

    public static PrisonId generate() {
        return new PrisonId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
