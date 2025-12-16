package vn.gov.prison.secure.domain.escape;

import lombok.Value;
import java.util.UUID;

@Value
public class EscapeRequestId {
    UUID value;

    public static EscapeRequestId of(UUID value) {
        return new EscapeRequestId(value);
    }

    public static EscapeRequestId of(String value) {
        return new EscapeRequestId(UUID.fromString(value));
    }

    public static EscapeRequestId generate() {
        return new EscapeRequestId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
