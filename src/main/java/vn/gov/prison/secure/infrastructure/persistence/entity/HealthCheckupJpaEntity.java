package vn.gov.prison.secure.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity for Health Checkups
 */
@Entity
@Table(name = "health_checkups")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthCheckupJpaEntity extends BaseJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "prisoner_id", nullable = false)
    private String prisonerId;

    @Column(name = "checkup_date", nullable = false)
    private LocalDateTime checkupDate;

    @Column(name = "checkup_type", nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private CheckupTypeEnum checkupType;

    @Column(name = "blood_pressure", length = 20)
    private String bloodPressure;

    @Column(name = "pulse_rate")
    private Integer pulseRate;

    @Column(name = "temperature", precision = 4, scale = 2)
    private BigDecimal temperature;

    @Column(name = "weight", precision = 5, scale = 2)
    private BigDecimal weight;

    @Column(name = "height", precision = 5, scale = 2)
    private BigDecimal height;

    @Column(name = "bmi", precision = 4, scale = 2)
    private BigDecimal bmi;

    @Column(name = "blood_sugar", precision = 5, scale = 2)
    private BigDecimal bloodSugar;

    @Column(name = "oxygen_saturation")
    private Integer oxygenSaturation;

    @Column(name = "respiratory_rate")
    private Integer respiratoryRate;

    @Column(name = "general_condition", length = 50)
    @Enumerated(EnumType.STRING)
    private GeneralConditionEnum generalCondition;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "next_checkup_date")
    private LocalDate nextCheckupDate;

    @Column(name = "performed_by", nullable = false)
    private String performedBy;

    @Column(name = "location")
    private String location;

    public enum CheckupTypeEnum {
        ADMISSION, ROUTINE, FOLLOW_UP, EMERGENCY, PRE_RELEASE, ANNUAL
    }

    public enum GeneralConditionEnum {
        EXCELLENT, GOOD, FAIR, POOR, CRITICAL
    }
}
