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
@Tag(name = "Reporting - Báo cáo & Thống kê", description = "API tạo các loại báo cáo thống kê về nhà tù, tù nhân, y tế và thăm nuôi")
public class ReportController {

    private final GenerateOccupancyReportUseCase generateOccupancyReportUseCase;
    private final GeneratePrisonerStatisticsUseCase generatePrisonerStatisticsUseCase;
    private final GenerateMedicalReportUseCase generateMedicalReportUseCase;
    private final GenerateVisitorReportUseCase generateVisitorReportUseCase;

    @PostMapping("/occupancy")
    @Operation(summary = "[RPT-001] Tạo báo cáo công suất nhà tù", description = "Tạo báo cáo về công suất sử dụng nhà tù bao gồm: tổng sức chứa, số tù nhân hiện tại, "
            +
            "tỷ lệ lấp đầy, số chỗ trống. Hỗ trợ xuất file PDF, Excel, CSV.")
    public ResponseEntity<ReportResponse> generateOccupancyReport(@RequestBody GenerateReportRequest request) {
        ReportResponse response = generateOccupancyReportUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/prisoner-statistics")
    @Operation(summary = "[RPT-002] Tạo báo cáo thống kê tù nhân", description = "Tạo báo cáo thống kê chi tiết về tù nhân: tổng số, số tiếp nhận mới, số thả, "
            +
            "phân loại theo giới tính, độ tuổi, mức độ nguy hiểm. Hỗ trợ lọc theo khoảng thời gian.")
    public ResponseEntity<ReportResponse> generatePrisonerStatistics(@RequestBody GenerateReportRequest request) {
        ReportResponse response = generatePrisonerStatisticsUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/medical-summary")
    @Operation(summary = "[RPT-003] Tạo báo cáo tổng hợp y tế", description = "Tạo báo cáo tổng hợp về tình hình y tế: số hồ sơ y tế, đơn thuốc đang dùng, "
            +
            "lịch khám sắp tới, ca cấp cứu, bệnh mãn tính. Giúp theo dõi sức khỏe tù nhân.")
    public ResponseEntity<ReportResponse> generateMedicalReport(@RequestBody GenerateReportRequest request) {
        ReportResponse response = generateMedicalReportUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/visitor-logs")
    @Operation(summary = "[RPT-004] Tạo báo cáo nhật ký thăm nuôi", description = "Tạo báo cáo về hoạt động thăm nuôi: tổng số buổi thăm, số yêu cầu được duyệt/từ chối, "
            +
            "thời gian thăm trung bình, thống kê theo tù nhân. Hỗ trợ xuất báo cáo định kỳ.")
    public ResponseEntity<ReportResponse> generateVisitorReport(@RequestBody GenerateReportRequest request) {
        ReportResponse response = generateVisitorReportUseCase.execute(request);
        return ResponseEntity.ok(response);
    }
}
