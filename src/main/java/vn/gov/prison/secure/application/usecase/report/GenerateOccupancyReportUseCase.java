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
public class GenerateOccupancyReportUseCase {

    @Transactional(readOnly = true)
    public ReportResponse execute(GenerateReportRequest request) {
        // Simple implementation - in real scenario would query database for occupancy
        // stats
        Map<String, Object> data = new HashMap<>();
        data.put("totalCapacity", 1000);
        data.put("currentOccupancy", 850);
        data.put("occupancyRate", 85.0);
        data.put("availableSpace", 150);

        return ReportResponse.builder()
                .id(UUID.randomUUID())
                .reportType("OCCUPANCY")
                .title("Prison Occupancy Report")
                .data(data)
                .format(request.getFormat())
                .generatedAt(LocalDateTime.now())
                .generatedBy(request.getGeneratedBy())
                .build();
    }
}
