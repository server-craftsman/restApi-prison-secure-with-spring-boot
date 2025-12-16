package vn.gov.prison.secure.domain.model.medical;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.gov.prison.secure.domain.model.common.ValueObject;

/**
 * Value object representing medical staff information
 */
@Getter
@Builder
@EqualsAndHashCode
public class MedicalStaff implements ValueObject {
    private final String name;
    private final String role;
    private final String licenseNumber;
    private final String specialization;

    public MedicalStaff(String name, String role, String licenseNumber, String specialization) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Medical staff name cannot be null or empty");
        }
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Medical staff role cannot be null or empty");
        }

        this.name = name.trim();
        this.role = role.trim();
        this.licenseNumber = licenseNumber != null ? licenseNumber.trim() : null;
        this.specialization = specialization != null ? specialization.trim() : null;
    }

    public static MedicalStaff of(String name, String role) {
        return new MedicalStaff(name, role, null, null);
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, role);
    }
}
