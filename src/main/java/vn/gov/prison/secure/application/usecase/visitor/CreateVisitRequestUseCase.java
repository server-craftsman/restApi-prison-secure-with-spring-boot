package vn.gov.prison.secure.application.usecase.visitor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.visitor.CreateVisitRequestRequest;
import vn.gov.prison.secure.application.dto.visitor.VisitRequestResponse;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateVisitRequestUseCase {

    @Transactional
    public VisitRequestResponse execute(CreateVisitRequestRequest request) {
        // Simple implementation - in real scenario would save to database
        return VisitRequestResponse.builder()
                .id(UUID.randomUUID())
                .prisonerId(request.getPrisonerId())
                .visitorId(request.getVisitorId())
                .requestedDate(request.getRequestedDate())
                .durationMinutes(request.getDurationMinutes())
                .status("PENDING")
                .purpose(request.getPurpose())
                .relationship(request.getRelationship())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
