package vn.gov.prison.secure.domain.model.workflow;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.gov.prison.secure.domain.model.common.ValueObject;

import java.util.UUID;

/**
 * Value object representing a workflow identifier
 */
@Getter
@EqualsAndHashCode
public class WorkflowId implements ValueObject {
    private final UUID value;

    private WorkflowId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("Workflow ID cannot be null");
        }
        this.value = value;
    }

    public static WorkflowId of(UUID value) {
        return new WorkflowId(value);
    }

    public static WorkflowId generate() {
        return new WorkflowId(UUID.randomUUID());
    }

    public static WorkflowId fromString(String value) {
        return new WorkflowId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
