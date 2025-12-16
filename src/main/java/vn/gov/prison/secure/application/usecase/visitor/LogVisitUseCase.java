package vn.gov.prison.secure.application.usecase.visitor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.visitor.VisitLogRequest;
import vn.gov.prison.secure.application.dto.visitor.VisitRequestResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LogVisitUseCase {

    @Transactional
    public VisitRequestResponse execute(UUID visitId, VisitLogRequest request) {
        // Simple implementation - in real scenario would log to database
        return VisitRequestResponse.builder()
                .id(visitId)
                .status(request.getCompleted() ? "COMPLETED" : "IN_PROGRESS")
                .build();
    }
}
