package vn.gov.prison.secure.domain.model.prisoner;

import vn.gov.prison.secure.domain.model.common.ValueObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Value Object representing prisoner's physical characteristics
 * Immutable and self-validating
 */
public final class PhysicalDescription implements ValueObject {

    private final Integer heightCm;
    private final Integer weightKg;
    private final String eyeColor;
    private final String hairColor;
    private final String bloodType;
    private final List<String> distinctiveMarks;
    private final List<String> scars;
    private final List<String> tattoos;

    private PhysicalDescription(Builder builder) {
        this.heightCm = builder.heightCm;
        this.weightKg = builder.weightKg;
        this.eyeColor = builder.eyeColor;
        this.hairColor = builder.hairColor;
        this.bloodType = builder.bloodType;
        this.distinctiveMarks = builder.distinctiveMarks != null
                ? Collections.unmodifiableList(new ArrayList<>(builder.distinctiveMarks))
                : Collections.emptyList();
        this.scars = builder.scars != null
                ? Collections.unmodifiableList(new ArrayList<>(builder.scars))
                : Collections.emptyList();
        this.tattoos = builder.tattoos != null
                ? Collections.unmodifiableList(new ArrayList<>(builder.tattoos))
                : Collections.emptyList();

        validate();
    }

    private void validate() {
        if (heightCm != null && (heightCm < 50 || heightCm > 300)) {
            throw new IllegalArgumentException("Height must be between 50 and 300 cm");
        }
        if (weightKg != null && (weightKg < 10 || weightKg > 500)) {
            throw new IllegalArgumentException("Weight must be between 10 and 500 kg");
        }
    }

    // Getters
    public Integer getHeightCm() {
        return heightCm;
    }

    public Integer getWeightKg() {
        return weightKg;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public String getHairColor() {
        return hairColor;
    }

    public String getBloodType() {
        return bloodType;
    }

    public List<String> getDistinctiveMarks() {
        return distinctiveMarks;
    }

    public List<String> getScars() {
        return scars;
    }

    public List<String> getTattoos() {
        return tattoos;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer heightCm;
        private Integer weightKg;
        private String eyeColor;
        private String hairColor;
        private String bloodType;
        private List<String> distinctiveMarks;
        private List<String> scars;
        private List<String> tattoos;

        public Builder heightCm(Integer heightCm) {
            this.heightCm = heightCm;
            return this;
        }

        public Builder weightKg(Integer weightKg) {
            this.weightKg = weightKg;
            return this;
        }

        public Builder eyeColor(String eyeColor) {
            this.eyeColor = eyeColor;
            return this;
        }

        public Builder hairColor(String hairColor) {
            this.hairColor = hairColor;
            return this;
        }

        public Builder bloodType(String bloodType) {
            this.bloodType = bloodType;
            return this;
        }

        public Builder distinctiveMarks(List<String> distinctiveMarks) {
            this.distinctiveMarks = distinctiveMarks;
            return this;
        }

        public Builder scars(List<String> scars) {
            this.scars = scars;
            return this;
        }

        public Builder tattoos(List<String> tattoos) {
            this.tattoos = tattoos;
            return this;
        }

        public PhysicalDescription build() {
            return new PhysicalDescription(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PhysicalDescription that = (PhysicalDescription) o;
        return Objects.equals(heightCm, that.heightCm) &&
                Objects.equals(weightKg, that.weightKg) &&
                Objects.equals(eyeColor, that.eyeColor) &&
                Objects.equals(hairColor, that.hairColor) &&
                Objects.equals(bloodType, that.bloodType) &&
                Objects.equals(distinctiveMarks, that.distinctiveMarks) &&
                Objects.equals(scars, that.scars) &&
                Objects.equals(tattoos, that.tattoos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(heightCm, weightKg, eyeColor, hairColor, bloodType,
                distinctiveMarks, scars, tattoos);
    }
}
