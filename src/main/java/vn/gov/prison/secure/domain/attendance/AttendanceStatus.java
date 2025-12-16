package vn.gov.prison.secure.domain.attendance;

public enum AttendanceStatus {
    PENDING, // Chưa check-in
    ON_TIME, // Check-in đúng giờ
    LATE, // Check-in trễ
    MISSED, // Không check-in
    COMPLETED // Đã hoàn thành (check-in và check-out)
}
