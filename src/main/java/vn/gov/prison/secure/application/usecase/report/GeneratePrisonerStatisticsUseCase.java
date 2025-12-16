package vn.gov.prison.secure.application.usecase.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.report.GenerateReportRequest;
import vn.gov.prison.secure.application.dto.report.ReportResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GeneratePrisonerStatisticsUseCase {

    @Transactional(readOnly = true)
    public ReportResponse execute(GenerateReportRequest request) {
        // Simple implementation - in real scenario would query database for prisoner
        // stats
        Map<String, Object> data = new HashMap<>();
        data.put("totalPrisoners", 850);
        data.put("newAdmissions", 45);
        data.put("releases", 30);
        data.put("byGender", Map.of("MALE", 720, "FEMALE", 130));
        data.put("byClassification", Map.of("MINIMUM", 200, "MEDIUM", 450, "MAXIMUM", 200));

        return ReportResponse.builder()
                .id(UUID.randomUUID())
                .reportType("PRISONER_STATISTICS")
                .title("Prisoner Statistics Report")
                .data(data)
                .format(request.getFormat())
                .generatedAt(LocalDateTime.now())
                .generatedBy(request.getGeneratedBy())
                .build();
    }
}
