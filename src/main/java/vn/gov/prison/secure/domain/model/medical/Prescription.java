package vn.gov.prison.secure.domain.model.medical;

import lombok.Builder;
import lombok.Getter;
import vn.gov.prison.secure.domain.model.common.BaseEntity;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class Prescription extends BaseEntity<UUID> {
    private final PrisonerId prisonerId;
    private final String medicationName;
    private final String dosage;
    private final String frequency;
    private final String route;
    private final LocalDate startDate;
    private LocalDate endDate;
    private final String prescribingDoctor;
    private String pharmacyNotes;
    private PrescriptionStatus status;
    private String discontinueReason;

    @Builder
    private Prescription(UUID id, PrisonerId prisonerId, String medicationName,
                        String dosage, String frequency, String route,
                        LocalDate startDate, LocalDate endDate,
                        String prescribingDoctor, String pharmacyNotes,
                        PrescriptionStatus status) {
        this.id = id != null ? id : UUID.randomUUID();
        this.prisonerId = prisonerId;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.route = route;
        this.startDate = startDate != null ? startDate : LocalDate.now();
        this.endDate = endDate;
        this.prescribingDoctor = prescribingDoctor;
        this.pharmacyNotes = pharmacyNotes;
        this.status = status != null ? status : PrescriptionStatus.ACTIVE;
    }

    public void discontinue(String reason) {
        this.status = PrescriptionStatus.DISCONTINUED;
        this.discontinueReason = reason;
        this.endDate = LocalDate.now();
    }

    public void putOnHold() {
        this.status = PrescriptionStatus.ON_HOLD;
    }

    public void reactivate() {
        if (this.status == PrescriptionStatus.DISCONTINUED) {
            throw new IllegalStateException("Cannot reactivate discontinued prescription");
        }
        this.status = PrescriptionStatus.ACTIVE;
    }

    public void complete() {
        this.status = PrescriptionStatus.COMPLETED;
        if (this.endDate == null) {
            this.endDate = LocalDate.now();
        }
    }

    public void addPharmacyNotes(String notes) {
        if (this.pharmacyNotes == null || this.pharmacyNotes.isEmpty()) {
            this.pharmacyNotes = notes;
        } else {
            this.pharmacyNotes = this.pharmacyNotes + "\n" + notes;
        }
    }

    public boolean isActive() {
        return this.status == PrescriptionStatus.ACTIVE;
    }
}
