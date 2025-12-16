package vn.gov.prison.secure.domain.prisoner;

import lombok.Value;

import java.util.UUID;

@Value
public class PrisonerId {
    UUID value;

    public static PrisonerId of(UUID value) {
        return new PrisonerId(value);
    }

    public static PrisonerId of(String value) {
        return new PrisonerId(UUID.fromString(value));
    }

    public static PrisonerId generate() {
        return new PrisonerId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
