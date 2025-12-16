package vn.gov.prison.secure.application.dto.visitor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitLogRequest {

    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;
    private String notes;
    private String loggedBy;
    private Boolean completed;
}
