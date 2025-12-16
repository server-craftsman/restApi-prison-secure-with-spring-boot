package vn.gov.prison.secure.domain.model.prisoner;

import vn.gov.prison.secure.domain.model.common.ValueObject;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Value Object representing biometric data following FBI, NIST, ANSI & ISO
 * standards
 * Immutable and thread-safe
 */
public final class BiometricData implements ValueObject {

    private final Map<BiometricType, BiometricRecord> biometricRecords;
    private final LocalDateTime capturedAt;
    private final String capturedBy;
    private final BiometricQuality quality;

    private BiometricData(Builder builder) {
        this.biometricRecords = builder.biometricRecords != null
                ? Collections.unmodifiableMap(new HashMap<>(builder.biometricRecords))
                : Collections.emptyMap();
        this.capturedAt = builder.capturedAt != null ? builder.capturedAt : LocalDateTime.now();
        this.capturedBy = builder.capturedBy;
        this.quality = builder.quality;

        validate();
    }

    private void validate() {
        if (biometricRecords.isEmpty()) {
            throw new IllegalArgumentException("At least one biometric record is required");
        }
        if (capturedBy == null || capturedBy.trim().isEmpty()) {
            throw new IllegalArgumentException("Captured by information is required");
        }
    }

    public boolean hasType(BiometricType type) {
        return biometricRecords.containsKey(type);
    }

    public Optional<BiometricRecord> getRecord(BiometricType type) {
        return Optional.ofNullable(biometricRecords.get(type));
    }

    public Set<BiometricType> getAvailableTypes() {
        return Collections.unmodifiableSet(biometricRecords.keySet());
    }

    public boolean isHighQuality() {
        return quality == BiometricQuality.EXCELLENT || quality == BiometricQuality.GOOD;
    }

    // Getters
    public Map<BiometricType, BiometricRecord> getBiometricRecords() {
        return biometricRecords;
    }

    public LocalDateTime getCapturedAt() {
        return capturedAt;
    }

    public String getCapturedBy() {
        return capturedBy;
    }

    public BiometricQuality getQuality() {
        return quality;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Map<BiometricType, BiometricRecord> biometricRecords = new HashMap<>();
        private LocalDateTime capturedAt;
        private String capturedBy;
        private BiometricQuality quality;

        public Builder addRecord(BiometricType type, BiometricRecord record) {
            this.biometricRecords.put(type, record);
            return this;
        }

        public Builder capturedAt(LocalDateTime capturedAt) {
            this.capturedAt = capturedAt;
            return this;
        }

        public Builder capturedBy(String capturedBy) {
            this.capturedBy = capturedBy;
            return this;
        }

        public Builder quality(BiometricQuality quality) {
            this.quality = quality;
            return this;
        }

        public BiometricData build() {
            return new BiometricData(this);
        }
    }

    /**
     * Biometric Type following multi-modal biometric system design
     */
    public enum BiometricType {
        FINGERPRINT("Fingerprint", "10-print fingerprint capture"),
        PALM_VEIN("Palm Vein", "Palm vein pattern"),
        FINGER_VEIN("Finger Vein", "Finger vein pattern"),
        IRIS("Iris", "Iris recognition"),
        FACIAL("Facial", "Facial recognition"),
        DNA("DNA", "DNA profile");

        private final String displayName;
        private final String description;

        BiometricType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * Biometric Quality following NIST standards
     */
    public enum BiometricQuality {
        EXCELLENT(5, "Excellent quality"),
        GOOD(4, "Good quality"),
        FAIR(3, "Fair quality"),
        POOR(2, "Poor quality"),
        UNUSABLE(1, "Unusable");

        private final int score;
        private final String description;

        BiometricQuality(int score, String description) {
            this.score = score;
            this.description = description;
        }

        public int getScore() {
            return score;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * Individual biometric record
     */
    public static final class BiometricRecord implements ValueObject {
        private final byte[] template;
        private final String format; // WSQ, JPEG2000, PNG for fingerprints
        private final Integer qualityScore;
        private final Map<String, String> metadata;

        public BiometricRecord(byte[] template, String format, Integer qualityScore,
                Map<String, String> metadata) {
            if (template == null || template.length == 0) {
                throw new IllegalArgumentException("Biometric template cannot be empty");
            }
            this.template = Arrays.copyOf(template, template.length);
            this.format = format;
            this.qualityScore = qualityScore;
            this.metadata = metadata != null
                    ? Collections.unmodifiableMap(new HashMap<>(metadata))
                    : Collections.emptyMap();
        }

        public byte[] getTemplate() {
            return Arrays.copyOf(template, template.length);
        }

        public String getFormat() {
            return format;
        }

        public Integer getQualityScore() {
            return qualityScore;
        }

        public Map<String, String> getMetadata() {
            return metadata;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            BiometricRecord that = (BiometricRecord) o;
            return Arrays.equals(template, that.template) &&
                    Objects.equals(format, that.format) &&
                    Objects.equals(qualityScore, that.qualityScore);
        }

        @Override
        public int hashCode() {
            return Objects.hash(Arrays.hashCode(template), format, qualityScore);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BiometricData that = (BiometricData) o;
        return Objects.equals(biometricRecords, that.biometricRecords) &&
                Objects.equals(capturedAt, that.capturedAt) &&
                Objects.equals(capturedBy, that.capturedBy) &&
                quality == that.quality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(biometricRecords, capturedAt, capturedBy, quality);
    }
}
