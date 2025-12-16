package vn.gov.prison.secure.application.usecase.visitor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.visitor.ApproveVisitRequest;
import vn.gov.prison.secure.application.dto.visitor.VisitRequestResponse;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApproveVisitRequestUseCase {

    @Transactional
    public VisitRequestResponse execute(UUID visitRequestId, ApproveVisitRequest request) {
        // Simple implementation - in real scenario would update database
        return VisitRequestResponse.builder()
                .id(visitRequestId)
                .status(request.getApproved() ? "APPROVED" : "REJECTED")
                .approvedBy(request.getApprovedBy())
                .approvedAt(LocalDateTime.now())
                .rejectionReason(request.getRejectionReason())
                .build();
    }
}
