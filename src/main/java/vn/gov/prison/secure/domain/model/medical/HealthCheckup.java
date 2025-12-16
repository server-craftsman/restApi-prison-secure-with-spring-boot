package vn.gov.prison.secure.domain.model.medical;

import lombok.Builder;
import lombok.Getter;
import vn.gov.prison.secure.domain.model.common.BaseEntity;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class HealthCheckup extends BaseEntity<UUID> {
    private final PrisonerId prisonerId;
    private final LocalDateTime checkupDate;
    private final CheckupType checkupType;
    private String bloodPressure;
    private Integer pulseRate;
    private BigDecimal temperature;
    private BigDecimal weight;
    private BigDecimal height;
    private BigDecimal bmi;
    private BigDecimal bloodSugar;
    private Integer oxygenSaturation;
    private Integer respiratoryRate;
    private GeneralCondition generalCondition;
    private String notes;
    private LocalDate nextCheckupDate;
    private final String performedBy;
    private String location;

    @Builder
    private HealthCheckup(UUID id, PrisonerId prisonerId, LocalDateTime checkupDate,
                         CheckupType checkupType, String bloodPressure, Integer pulseRate,
                         BigDecimal temperature, BigDecimal weight, BigDecimal height,
                         BigDecimal bmi, BigDecimal bloodSugar, Integer oxygenSaturation,
                         Integer respiratoryRate, GeneralCondition generalCondition,
                         String notes, LocalDate nextCheckupDate, 
                         String performedBy, String location) {
        this.id = id != null ? id : UUID.randomUUID();
        this.prisonerId = prisonerId;
        this.checkupDate = checkupDate != null ? checkupDate : LocalDateTime.now();
        this.checkupType = checkupType;
        this.bloodPressure = bloodPressure;
        this.pulseRate = pulseRate;
        this.temperature = temperature;
        this.weight = weight;
        this.height = height;
        this.bmi = bmi != null ? bmi : calculateBMI(weight, height);
        this.bloodSugar = bloodSugar;
        this.oxygenSaturation = oxygenSaturation;
        this.respiratoryRate = respiratoryRate;
        this.generalCondition = generalCondition;
        this.notes = notes;
        this.nextCheckupDate = nextCheckupDate;
        this.performedBy = performedBy;
        this.location = location;
    }

    public void updateVitalSigns(String bloodPressure, Integer pulseRate,
                                 BigDecimal temperature, Integer oxygenSaturation) {
        this.bloodPressure = bloodPressure;
        this.pulseRate = pulseRate;
        this.temperature = temperature;
        this.oxygenSaturation = oxygenSaturation;
    }

    public void updateWeightAndHeight(BigDecimal weight, BigDecimal height) {
        this.weight = weight;
        this.height = height;
        this.bmi = calculateBMI(weight, height);
    }

    public void updateGeneralCondition(GeneralCondition condition) {
        this.generalCondition = condition;
    }

    public void addNotes(String additionalNotes) {
        if (this.notes == null || this.notes.isEmpty()) {
            this.notes = additionalNotes;
        } else {
            this.notes = this.notes + "\n" + additionalNotes;
        }
    }

    public void scheduleNextCheckup(LocalDate nextDate) {
        this.nextCheckupDate = nextDate;
    }

    private static BigDecimal calculateBMI(BigDecimal weight, BigDecimal height) {
        if (weight == null || height == null) {
            return null;
        }
        if (weight.compareTo(BigDecimal.ZERO) <= 0 || height.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        BigDecimal heightInMeters = height.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal heightSquared = heightInMeters.multiply(heightInMeters);
        return weight.divide(heightSquared, 2, RoundingMode.HALF_UP);
    }

    public boolean hasNormalVitalSigns() {
        return (pulseRate == null || (pulseRate >= 60 && pulseRate <= 100)) &&
               (temperature == null || (temperature.compareTo(new BigDecimal("36.5")) >= 0 && 
                                       temperature.compareTo(new BigDecimal("37.5")) <= 0)) &&
               (oxygenSaturation == null || oxygenSaturation >= 95);
    }
}
