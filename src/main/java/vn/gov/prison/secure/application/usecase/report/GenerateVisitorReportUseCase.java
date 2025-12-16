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
public class GenerateVisitorReportUseCase {

    @Transactional(readOnly = true)
    public ReportResponse execute(GenerateReportRequest request) {
        // Simple implementation - in real scenario would query database for visitor
        // stats
        Map<String, Object> data = new HashMap<>();
        data.put("totalVisits", 425);
        data.put("approvedVisits", 380);
        data.put("rejectedVisits", 25);
        data.put("pendingRequests", 20);
        data.put("averageVisitDuration", 45);

        return ReportResponse.builder()
                .id(UUID.randomUUID())
                .reportType("VISITOR_LOGS")
                .title("Visitor Logs Report")
                .data(data)
                .format(request.getFormat())
                .generatedAt(LocalDateTime.now())
                .generatedBy(request.getGeneratedBy())
                .build();
    }
}
