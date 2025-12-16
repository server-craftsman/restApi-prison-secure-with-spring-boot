package vn.gov.prison.secure.application.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateReportRequest {

    private String reportType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String format; // PDF, EXCEL, CSV
    private String generatedBy;
}
