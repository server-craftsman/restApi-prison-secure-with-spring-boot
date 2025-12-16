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
public class GenerateMedicalReportUseCase {

    @Transactional(readOnly = true)
    public ReportResponse execute(GenerateReportRequest request) {
        // Simple implementation - in real scenario would query database for medical
        // stats
        Map<String, Object> data = new HashMap<>();
        data.put("totalMedicalRecords", 1250);
        data.put("activePrescriptions", 320);
        data.put("scheduledCheckups", 45);
        data.put("emergencyCases", 8);
        data.put("chronicConditions", 156);

        return ReportResponse.builder()
                .id(UUID.randomUUID())
                .reportType("MEDICAL_SUMMARY")
                .title("Medical Summary Report")
                .data(data)
                .format(request.getFormat())
                .generatedAt(LocalDateTime.now())
                .generatedBy(request.getGeneratedBy())
                .build();
    }
}
