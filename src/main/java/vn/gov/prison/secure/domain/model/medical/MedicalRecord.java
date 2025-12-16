package vn.gov.prison.secure.domain.model.medical;

import lombok.Getter;
import vn.gov.prison.secure.domain.model.common.BaseEntity;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Medical Record aggregate root
 */
@Getter
public class MedicalRecord extends BaseEntity<MedicalRecordId> {
    private final MedicalRecordId medicalRecordId;
    private final PrisonerId prisonerId;
    private final LocalDateTime recordDate;
    private final RecordType recordType;
    private String diagnosis;
    private String treatment;
    private final MedicalStaff medicalStaff;
    private String notes;
    private Severity severity;
    private boolean followUpRequired;
    private LocalDate followUpDate;
    private final List<Prescription> prescriptions;
    
    private MedicalRecord(Builder builder) {
        this.id = builder.id;
        this.medicalRecordId = builder.id;
        this.prisonerId = builder.prisonerId;
        this.recordDate = builder.recordDate;
        this.recordType = builder.recordType;
        this.diagnosis = builder.diagnosis;
        this.treatment = builder.treatment;
        this.medicalStaff = builder.medicalStaff;
        this.notes = builder.notes;
        this.severity = builder.severity;
        this.followUpRequired = builder.followUpRequired;
        this.followUpDate = builder.followUpDate;
        this.prescriptions = new ArrayList<>();
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public void addPrescription(Prescription prescription) {
        if (prescription == null) {
            throw new IllegalArgumentException("Prescription cannot be null");
        }
        this.prescriptions.add(prescription);
    }
    
    public void updateDiagnosisAndTreatment(String diagnosis, String treatment) {
        this.diagnosis = diagnosis;
        this.treatment = treatment;
    }
    
    public void updateSeverity(Severity severity) {
        this.severity = severity;
    }
    
    public void addNotes(String additionalNotes) {
        if (this.notes == null || this.notes.isEmpty()) {
            this.notes = additionalNotes;
        } else {
            this.notes = this.notes + "\n" + additionalNotes;
        }
    }
    
    public void scheduleFollowUp(LocalDate followUpDate) {
        this.followUpRequired = true;
        this.followUpDate = followUpDate;
    }
    
    public List<Prescription> getPrescriptions() {
        return Collections.unmodifiableList(prescriptions);
    }
    
    public static class Builder {
        private MedicalRecordId id;
        private PrisonerId prisonerId;
        private LocalDateTime recordDate;
        private RecordType recordType;
        private String diagnosis;
        private String treatment;
        private MedicalStaff medicalStaff;
        private String notes;
        private Severity severity;
        private boolean followUpRequired;
        private LocalDate followUpDate;
        
        public Builder id(MedicalRecordId id) {
            this.id = id;
            return this;
        }
        
        public Builder prisonerId(PrisonerId prisonerId) {
            this.prisonerId = prisonerId;
            return this;
        }
        
        public Builder recordDate(LocalDateTime recordDate) {
            this.recordDate = recordDate;
            return this;
        }
        
        public Builder recordType(RecordType recordType) {
            this.recordType = recordType;
            return this;
        }
        
        public Builder diagnosis(String diagnosis) {
            this.diagnosis = diagnosis;
            return this;
        }
        
        public Builder treatment(String treatment) {
            this.treatment = treatment;
            return this;
        }
        
        public Builder medicalStaff(MedicalStaff medicalStaff) {
            this.medicalStaff = medicalStaff;
            return this;
        }
        
        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }
        
        public Builder severity(Severity severity) {
            this.severity = severity;
            return this;
        }
        
        public Builder followUpRequired(boolean followUpRequired) {
            this.followUpRequired = followUpRequired;
            return this;
        }
        
        public Builder followUpDate(LocalDate followUpDate) {
            this.followUpDate = followUpDate;
            return this;
        }
        
        public MedicalRecord build() {
            if (prisonerId == null) {
                throw new IllegalStateException("Prisoner ID is required");
            }
            if (recordType == null) {
                throw new IllegalStateException("Record type is required");
            }
            if (medicalStaff == null) {
                throw new IllegalStateException("Medical staff is required");
            }
            
            if (id == null) {
                id = MedicalRecordId.generate();
            }
            if (recordDate == null) {
                recordDate = LocalDateTime.now();
            }
            
            return new MedicalRecord(this);
        }
    }
}
