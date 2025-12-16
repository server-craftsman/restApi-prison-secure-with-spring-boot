package vn.gov.prison.secure.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.gov.prison.secure.application.dto.report.GenerateReportRequest;
import vn.gov.prison.secure.application.dto.report.ReportResponse;
import vn.gov.prison.secure.application.usecase.report.GenerateMedicalReportUseCase;
import vn.gov.prison.secure.application.usecase.report.GenerateOccupancyReportUseCase;
import vn.gov.prison.secure.application.usecase.report.GeneratePrisonerStatisticsUseCase;
import vn.gov.prison.secure.application.usecase.report.GenerateVisitorReportUseCase;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Tag(name = "Reporting", description = "APIs for generating prison reports")
public class ReportController {

    private final GenerateOccupancyReportUseCase generateOccupancyReportUseCase;
    private final GeneratePrisonerStatisticsUseCase generatePrisonerStatisticsUseCase;
    private final GenerateMedicalReportUseCase generateMedicalReportUseCase;
    private final GenerateVisitorReportUseCase generateVisitorReportUseCase;

    @PostMapping("/occupancy")
    @Operation(summary = "Generate occupancy report")
    public ResponseEntity<ReportResponse> generateOccupancyReport(@RequestBody GenerateReportRequest request) {
        ReportResponse response = generateOccupancyReportUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/prisoner-statistics")
    @Operation(summary = "Generate prisoner statistics report")
    public ResponseEntity<ReportResponse> generatePrisonerStatistics(@RequestBody GenerateReportRequest request) {
        ReportResponse response = generatePrisonerStatisticsUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/medical-summary")
    @Operation(summary = "Generate medical summary report")
    public ResponseEntity<ReportResponse> generateMedicalReport(@RequestBody GenerateReportRequest request) {
        ReportResponse response = generateMedicalReportUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/visitor-logs")
    @Operation(summary = "Generate visitor logs report")
    public ResponseEntity<ReportResponse> generateVisitorReport(@RequestBody GenerateReportRequest request) {
        ReportResponse response = generateVisitorReportUseCase.execute(request);
        return ResponseEntity.ok(response);
    }
}
