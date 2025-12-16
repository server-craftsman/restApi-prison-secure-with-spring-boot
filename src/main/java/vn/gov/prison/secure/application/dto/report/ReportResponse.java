package vn.gov.prison.secure.application.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {

    private UUID id;
    private String reportType;
    private String title;
    private Map<String, Object> data;
    private String format;
    private LocalDateTime generatedAt;
    private String generatedBy;
    private String downloadUrl;
}
