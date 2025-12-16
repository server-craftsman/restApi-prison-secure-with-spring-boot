package vn.gov.prison.secure.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * JPA Entity for Biometric Data
 * Stores biometric information captured for prisoners
 */
@Entity
@Table(name = "biometric_data", indexes = {
        @Index(name = "idx_biometric_prisoner", columnList = "prisoner_id"),
        @Index(name = "idx_biometric_type", columnList = "biometric_type"),
        @Index(name = "idx_biometric_captured", columnList = "captured_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BiometricDataJpaEntity extends BaseJpaEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "prisoner_id", nullable = false, length = 36)
    private String prisonerId;

    @Column(name = "biometric_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private BiometricType biometricType;

    // Use bytea instead of @Lob for PostgreSQL compatibility
    @Column(name = "template_data", nullable = false, columnDefinition = "bytea")
    private byte[] templateData;

    @Column(name = "format", nullable = false, length = 50)
    private String format;

    @Column(name = "quality_score")
    private Integer qualityScore;

    @Column(name = "quality", length = 20)
    @Enumerated(EnumType.STRING)
    private BiometricQuality quality;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata; // JSON

    @Column(name = "captured_at", nullable = false)
    private LocalDateTime capturedAt;

    @Column(name = "captured_by", nullable = false, length = 100)
    private String capturedBy;

    public enum BiometricType {
        FINGERPRINT, PALM_VEIN, FINGER_VEIN, IRIS, FACIAL, DNA
    }

    public enum BiometricQuality {
        EXCELLENT, GOOD, FAIR, POOR, UNUSABLE
    }
}
