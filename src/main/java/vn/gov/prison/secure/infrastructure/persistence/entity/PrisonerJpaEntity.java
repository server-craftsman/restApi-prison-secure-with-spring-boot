package vn.gov.prison.secure.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * JPA Entity for Prisoner
 * Maps domain Prisoner aggregate to database table
 * 
 * Following Adapter Pattern - adapts domain model to persistence layer
 */
@Entity
@Table(name = "prisoners", indexes = {
        @Index(name = "idx_prisoner_number", columnList = "prisoner_number", unique = true),
        @Index(name = "idx_national_id", columnList = "national_id_number"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_facility", columnList = "assigned_facility")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PrisonerJpaEntity extends BaseJpaEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "prisoner_number", nullable = false, unique = true, length = 50)
    private String prisonerNumber;

    // Demographic Information
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "middle_name", length = 100)
    private String middleName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "gender", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "nationality", length = 100)
    private String nationality;

    @Column(name = "place_of_birth", length = 200)
    private String placeOfBirth;

    @Column(name = "national_id_number", length = 50)
    private String nationalIdNumber;

    // Physical Description
    @Column(name = "height_cm")
    private Integer heightCm;

    @Column(name = "weight_kg")
    private Integer weightKg;

    @Column(name = "eye_color", length = 50)
    private String eyeColor;

    @Column(name = "hair_color", length = 50)
    private String hairColor;

    @Column(name = "blood_type", length = 10)
    private String bloodType;

    @Column(name = "distinctive_marks", columnDefinition = "TEXT")
    private String distinctiveMarks; // JSON array

    @Column(name = "scars", columnDefinition = "TEXT")
    private String scars; // JSON array

    @Column(name = "tattoos", columnDefinition = "TEXT")
    private String tattoos; // JSON array

    // Status Information
    @Column(name = "status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private PrisonerStatus status;

    @Column(name = "admission_date")
    private LocalDateTime admissionDate;

    @Column(name = "release_date")
    private LocalDateTime releaseDate;

    // Location Information
    @Column(name = "assigned_facility", length = 100)
    private String assignedFacility;

    @Column(name = "current_block", length = 50)
    private String currentBlock;

    @Column(name = "current_cell", length = 50)
    private String currentCell;

    public enum Gender {
        MALE, FEMALE, OTHER, UNKNOWN
    }

    public enum PrisonerStatus {
        PENDING_ADMISSION, ACTIVE, TEMPORARY_RELEASE,
        ON_PAROLE, RELEASED, TRANSFERRED, DECEASED, ESCAPED
    }
}
