package vn.gov.prison.secure.domain.model.prisoner;

import vn.gov.prison.secure.domain.model.common.ValueObject;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Value Object representing prisoner's demographic information
 * Immutable and self-validating following DDD principles
 */
public final class DemographicInfo implements ValueObject {

    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final LocalDate dateOfBirth;
    private final Gender gender;
    private final String nationality;
    private final String placeOfBirth;
    private final String nationalIdNumber;

    private DemographicInfo(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.middleName = builder.middleName;
        this.dateOfBirth = builder.dateOfBirth;
        this.gender = builder.gender;
        this.nationality = builder.nationality;
        this.placeOfBirth = builder.placeOfBirth;
        this.nationalIdNumber = builder.nationalIdNumber;

        validate();
    }

    private void validate() {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth is required");
        }
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        }
        if (gender == null) {
            throw new IllegalArgumentException("Gender is required");
        }
    }

    public String getFullName() {
        return middleName != null
                ? String.format("%s %s %s", firstName, middleName, lastName)
                : String.format("%s %s", firstName, lastName);
    }

    public int getAge() {
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public String getNationality() {
        return nationality;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public String getNationalIdNumber() {
        return nationalIdNumber;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String firstName;
        private String lastName;
        private String middleName;
        private LocalDate dateOfBirth;
        private Gender gender;
        private String nationality;
        private String placeOfBirth;
        private String nationalIdNumber;

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder middleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public Builder dateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public Builder gender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public Builder nationality(String nationality) {
            this.nationality = nationality;
            return this;
        }

        public Builder placeOfBirth(String placeOfBirth) {
            this.placeOfBirth = placeOfBirth;
            return this;
        }

        public Builder nationalIdNumber(String nationalIdNumber) {
            this.nationalIdNumber = nationalIdNumber;
            return this;
        }

        public DemographicInfo build() {
            return new DemographicInfo(this);
        }
    }

    public enum Gender {
        MALE("Male"),
        FEMALE("Female"),
        OTHER("Other"),
        UNKNOWN("Unknown");

        private final String displayName;

        Gender(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DemographicInfo that = (DemographicInfo) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(middleName, that.middleName) &&
                Objects.equals(dateOfBirth, that.dateOfBirth) &&
                gender == that.gender &&
                Objects.equals(nationality, that.nationality) &&
                Objects.equals(placeOfBirth, that.placeOfBirth) &&
                Objects.equals(nationalIdNumber, that.nationalIdNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, middleName, dateOfBirth,
                gender, nationality, placeOfBirth, nationalIdNumber);
    }

    @Override
    public String toString() {
        return getFullName();
    }
}
