package vn.gov.prison.secure.domain.model.booking;

import vn.gov.prison.secure.domain.model.common.BaseEntity;

import java.time.LocalDateTime;

/**
 * Charge Entity representing individual criminal charges
 * Part of Booking aggregate
 */
public class Charge extends BaseEntity<String> {

    private String chargeCode;
    private String description;
    private ChargeSeverity severity;
    private String statute;
    private LocalDateTime chargeDate;
    private String arrestingOfficer;
    private String jurisdiction;
    private ChargeStatus status;

    protected Charge() {
        super();
        this.status = ChargeStatus.PENDING;
    }

    public static Charge create(String chargeCode, String description,
            ChargeSeverity severity, String statute) {
        if (chargeCode == null || chargeCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Charge code is required");
        }

        Charge charge = new Charge();
        charge.id = java.util.UUID.randomUUID().toString();
        charge.chargeCode = chargeCode;
        charge.description = description;
        charge.severity = severity;
        charge.statute = statute;
        charge.chargeDate = LocalDateTime.now();

        return charge;
    }

    public void dismiss() {
        this.status = ChargeStatus.DISMISSED;
        updateTimestamp();
    }

    public void convict() {
        this.status = ChargeStatus.CONVICTED;
        updateTimestamp();
    }

    public void acquit() {
        this.status = ChargeStatus.ACQUITTED;
        updateTimestamp();
    }

    // Getters and setters
    public String getChargeCode() {
        return chargeCode;
    }

    public String getDescription() {
        return description;
    }

    public ChargeSeverity getSeverity() {
        return severity;
    }

    public String getStatute() {
        return statute;
    }

    public LocalDateTime getChargeDate() {
        return chargeDate;
    }

    public String getArrestingOfficer() {
        return arrestingOfficer;
    }

    public void setArrestingOfficer(String arrestingOfficer) {
        this.arrestingOfficer = arrestingOfficer;
        updateTimestamp();
    }

    public String getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction;
        updateTimestamp();
    }

    public ChargeStatus getStatus() {
        return status;
    }

    public enum ChargeSeverity {
        INFRACTION("Infraction", 1),
        MISDEMEANOR("Misdemeanor", 2),
        FELONY("Felony", 3),
        CAPITAL("Capital Offense", 4);

        private final String displayName;
        private final int level;

        ChargeSeverity(String displayName, int level) {
            this.displayName = displayName;
            this.level = level;
        }

        public String getDisplayName() {
            return displayName;
        }

        public int getLevel() {
            return level;
        }
    }

    public enum ChargeStatus {
        PENDING, CONVICTED, ACQUITTED, DISMISSED, REDUCED
    }
}
