package vn.gov.prison.secure.domain.model.workflow;

import lombok.Builder;
import lombok.Getter;
import vn.gov.prison.secure.domain.model.common.BaseEntity;

/**
 * Workflow aggregate root
 * Represents a workflow definition/template
 */
@Getter
@Builder
public class Workflow extends BaseEntity<WorkflowId> {
    private final WorkflowId workflowId;
    private final WorkflowType workflowType;
    private final String workflowName;
    private final String description;
    private boolean isActive;

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }
}
