package vn.gov.prison.secure.domain.model.prisoner;

import vn.gov.prison.secure.domain.model.common.BaseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Prisoner Aggregate Root
 * Central entity in the prisoner bounded context
 * Encapsulates all business rules related to prisoner management
 * 
 * Following SOLID principles:
 * - SRP: Manages prisoner lifecycle and identity
 * - OCP: Extensible through strategies
 * - LSP: Proper inheritance from BaseEntity
 * - ISP: Focused interface
 * - DIP: Depends on abstractions (value objects)
 */
public class Prisoner extends BaseEntity<PrisonerId> {

    private DemographicInfo demographicInfo;
    private PhysicalDescription physicalDescription;
    private PrisonerStatus status;
    private LocalDateTime admissionDate;
    private LocalDateTime releaseDate;
    private String prisonerNumber; // System-generated unique number
    private List<BiometricData> biometricDataHistory;
    private String currentCell;
    private String currentBlock;
    private String assignedFacility;

    // Private constructor - use factory methods or builder
    private Prisoner() {
        super();
        this.biometricDataHistory = new ArrayList<>();
        this.status = PrisonerStatus.PENDING_ADMISSION;
    }

    /**
     * Factory method for creating new prisoner during admission
     */
    public static Prisoner admit(DemographicInfo demographicInfo,
            PhysicalDescription physicalDescription,
            String prisonerNumber) {
        if (demographicInfo == null) {
            throw new IllegalArgumentException("Demographic information is required");
        }
        if (prisonerNumber == null || prisonerNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Prisoner number is required");
        }

        Prisoner prisoner = new Prisoner();
        prisoner.id = PrisonerId.generate();
        prisoner.demographicInfo = demographicInfo;
        prisoner.physicalDescription = physicalDescription;
        prisoner.prisonerNumber = prisonerNumber;
        prisoner.admissionDate = LocalDateTime.now();
        prisoner.status = PrisonerStatus.PENDING_ADMISSION;

        return prisoner;
    }

    /**
     * Business logic: Activate prisoner after processing
     */
    public void activate() {
        if (!status.canTransitionTo(PrisonerStatus.ACTIVE)) {
            throw new IllegalStateException(
                    String.format("Cannot activate prisoner in status: %s", status));
        }
        this.status = PrisonerStatus.ACTIVE;
        updateTimestamp();
    }

    /**
     * Business logic: Release prisoner
     * Following SRP - encapsulates release logic
     */
    public void release() {
        if (!status.canTransitionTo(PrisonerStatus.RELEASED)) {
            throw new IllegalStateException(
                    String.format("Cannot release prisoner in status: %s", status));
        }
        this.status = PrisonerStatus.RELEASED;
        this.releaseDate = LocalDateTime.now();
        this.currentCell = null;
        this.currentBlock = null;
        updateTimestamp();
    }

    /**
     * Business logic: Transfer prisoner to another facility
     */
    public void transferTo(String facilityId) {
        if (facilityId == null || facilityId.trim().isEmpty()) {
            throw new IllegalArgumentException("Facility ID is required");
        }
        if (status == PrisonerStatus.RELEASED || status == PrisonerStatus.DECEASED) {
            throw new IllegalStateException("Cannot transfer prisoner in terminal status");
        }

        PrisonerStatus previousStatus = this.status;
        this.status = PrisonerStatus.TRANSFERRED;
        this.assignedFacility = facilityId;
        this.currentCell = null;
        this.currentBlock = null;
        updateTimestamp();

        // After transfer processing, revert to previous active status
        this.status = previousStatus;
    }

    /**
     * Business logic: Assign to cell
     * Encapsulates cell allocation rules
     */
    public void assignToCell(String blockId, String cellId) {
        if (status != PrisonerStatus.ACTIVE) {
            throw new IllegalStateException("Can only assign active prisoners to cells");
        }
        if (blockId == null || cellId == null) {
            throw new IllegalArgumentException("Block and Cell IDs are required");
        }

        this.currentBlock = blockId;
        this.currentCell = cellId;
        updateTimestamp();
    }

    /**
     * Business logic: Add biometric data
     * Following OCP - can extend with new biometric types
     */
    public void addBiometricData(BiometricData biometricData) {
        if (biometricData == null) {
            throw new IllegalArgumentException("Biometric data is required");
        }

        this.biometricDataHistory.add(biometricData);
        updateTimestamp();
    }

    /**
     * Get latest biometric data
     */
    public Optional<BiometricData> getLatestBiometricData() {
        if (biometricDataHistory.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(biometricDataHistory.get(biometricDataHistory.size() - 1));
    }

    /**
     * Check if prisoner has high-quality biometric data
     * Business rule for identity verification
     */
    public boolean hasHighQualityBiometrics() {
        return getLatestBiometricData()
                .map(BiometricData::isHighQuality)
                .orElse(false);
    }

    /**
     * Update demographic information
     */
    public void updateDemographicInfo(DemographicInfo newInfo) {
        if (newInfo == null) {
            throw new IllegalArgumentException("Demographic information is required");
        }
        this.demographicInfo = newInfo;
        updateTimestamp();
    }

    /**
     * Update physical description
     */
    public void updatePhysicalDescription(PhysicalDescription newDescription) {
        if (newDescription == null) {
            throw new IllegalArgumentException("Physical description is required");
        }
        this.physicalDescription = newDescription;
        updateTimestamp();
    }

    /**
     * Check if prisoner is currently incarcerated
     */
    public boolean isIncarcerated() {
        return status == PrisonerStatus.ACTIVE ||
                status == PrisonerStatus.PENDING_ADMISSION;
    }

    /**
     * Get days in custody
     */
    public long getDaysInCustody() {
        if (admissionDate == null) {
            return 0;
        }
        LocalDateTime endDate = releaseDate != null ? releaseDate : LocalDateTime.now();
        return java.time.Duration.between(admissionDate, endDate).toDays();
    }

    // Getters
    public DemographicInfo getDemographicInfo() {
        return demographicInfo;
    }

    public PhysicalDescription getPhysicalDescription() {
        return physicalDescription;
    }

    public PrisonerStatus getStatus() {
        return status;
    }

    public LocalDateTime getAdmissionDate() {
        return admissionDate;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public String getPrisonerNumber() {
        return prisonerNumber;
    }

    public List<BiometricData> getBiometricDataHistory() {
        return Collections.unmodifiableList(biometricDataHistory);
    }

    public String getCurrentCell() {
        return currentCell;
    }

    public String getCurrentBlock() {
        return currentBlock;
    }

    public String getAssignedFacility() {
        return assignedFacility;
    }
}
