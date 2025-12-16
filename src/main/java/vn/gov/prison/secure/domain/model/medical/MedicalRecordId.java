package vn.gov.prison.secure.domain.model.medical;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.gov.prison.secure.domain.model.common.ValueObject;

import java.util.UUID;

/**
 * Value object representing a medical record identifier
 */
@Getter
@EqualsAndHashCode
public class MedicalRecordId implements ValueObject {
    private final UUID value;

    private MedicalRecordId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("Medical record ID cannot be null");
        }
        this.value = value;
    }

    public static MedicalRecordId of(UUID value) {
        return new MedicalRecordId(value);
    }

    public static MedicalRecordId generate() {
        return new MedicalRecordId(UUID.randomUUID());
    }

    public static MedicalRecordId fromString(String value) {
        return new MedicalRecordId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
