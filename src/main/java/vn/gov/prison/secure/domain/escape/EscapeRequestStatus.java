package vn.gov.prison.secure.domain.escape;

public enum EscapeRequestStatus {
    PENDING, // Chờ phê duyệt
    GUARD_APPROVED, // Quản giáo đã duyệt
    GUARD_REJECTED, // Quản giáo từ chối
    WARDEN_APPROVED, // Quản đốc đã duyệt (final approval)
    WARDEN_REJECTED, // Quản đốc từ chối
    EXECUTED // Đã thực hiện trốn tù
}
