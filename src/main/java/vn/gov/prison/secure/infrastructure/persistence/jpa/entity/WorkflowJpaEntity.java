package vn.gov.prison.secure.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * JPA Entity for Workflows (workflow definitions)
 */
@Entity
@Table(name = "workflows")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowJpaEntity extends BaseJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "workflow_type", nullable = false, unique = true, length = 50)
    @Enumerated(EnumType.STRING)
    private WorkflowTypeEnum workflowType;

    @Column(name = "workflow_name", nullable = false)
    private String workflowName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    public enum WorkflowTypeEnum {
        ADMISSION, RELEASE, TRANSFER, CLASSIFICATION, DISCIPLINARY, MEDICAL_EMERGENCY, PAROLE, OTHER
    }
}
