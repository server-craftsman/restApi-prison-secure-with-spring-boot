package vn.gov.prison.secure.domain.attendance;

import lombok.Value;
import java.util.UUID;

@Value
public class AttendanceId {
    UUID value;

    public static AttendanceId of(UUID value) {
        return new AttendanceId(value);
    }

    public static AttendanceId of(String value) {
        return new AttendanceId(UUID.fromString(value));
    }

    public static AttendanceId generate() {
        return new AttendanceId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
