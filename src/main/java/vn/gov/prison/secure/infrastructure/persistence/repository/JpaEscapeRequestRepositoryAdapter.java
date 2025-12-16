package vn.gov.prison.secure.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.gov.prison.secure.domain.escape.EscapeRequest;
import vn.gov.prison.secure.domain.escape.EscapeRequestId;
import vn.gov.prison.secure.domain.escape.EscapeRequestRepository;
import vn.gov.prison.secure.domain.escape.EscapeRequestStatus;
import vn.gov.prison.secure.domain.prisoner.PrisonerId;
import vn.gov.prison.secure.domain.user.UserId;
import vn.gov.prison.secure.infrastructure.persistence.entity.EscapeRequestJpaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaEscapeRequestRepositoryAdapter implements EscapeRequestRepository {

    private final SpringDataEscapeRequestRepository jpaRepository;

    @Override
    public EscapeRequest save(EscapeRequest escapeRequest) {
        EscapeRequestJpaEntity entity = toEntity(escapeRequest);
        EscapeRequestJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<EscapeRequest> findById(EscapeRequestId id) {
        return jpaRepository.findById(id.getValue())
                .map(this::toDomain);
    }

    @Override
    public List<EscapeRequest> findByPrisoner(PrisonerId prisonerId) {
        return jpaRepository.findByPrisonerId(prisonerId.getValue().toString())
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<EscapeRequest> findPendingRequests() {
        return jpaRepository.findByStatus(EscapeRequestStatus.PENDING.name())
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<EscapeRequest> findGuardApprovedRequests() {
        return jpaRepository.findByStatus(EscapeRequestStatus.GUARD_APPROVED.name())
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<EscapeRequest> findByStatus(EscapeRequestStatus status) {
        return jpaRepository.findByStatus(status.name())
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private EscapeRequestJpaEntity toEntity(EscapeRequest domain) {
        return EscapeRequestJpaEntity.builder()
                .id(domain.getId().getValue())
                .prisonerId(domain.getPrisonerId().getValue().toString())
                .requestDate(domain.getRequestDate())
                .reason(domain.getReason())
                .plannedEscapeDate(domain.getPlannedEscapeDate())
                .status(domain.getStatus().name())
                .guardApprovalDate(domain.getGuardApprovalDate())
                .guardApprovalNotes(domain.getGuardApprovalNotes())
                .approvedByGuard(domain.getApprovedByGuard() != null ? domain.getApprovedByGuard().getValue() : null)
                .wardenApprovalDate(domain.getWardenApprovalDate())
                .wardenApprovalNotes(domain.getWardenApprovalNotes())
                .approvedByWarden(domain.getApprovedByWarden() != null ? domain.getApprovedByWarden().getValue() : null)
                .executionDate(domain.getExecutionDate())
                .executionNotes(domain.getExecutionNotes())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

    private EscapeRequest toDomain(EscapeRequestJpaEntity entity) {
        return EscapeRequest.builder()
                .id(EscapeRequestId.of(entity.getId()))
                .prisonerId(PrisonerId.of(entity.getPrisonerId()))
                .requestDate(entity.getRequestDate())
                .reason(entity.getReason())
                .plannedEscapeDate(entity.getPlannedEscapeDate())
                .status(EscapeRequestStatus.valueOf(entity.getStatus()))
                .guardApprovalDate(entity.getGuardApprovalDate())
                .guardApprovalNotes(entity.getGuardApprovalNotes())
                .approvedByGuard(entity.getApprovedByGuard() != null ? UserId.of(entity.getApprovedByGuard()) : null)
                .wardenApprovalDate(entity.getWardenApprovalDate())
                .wardenApprovalNotes(entity.getWardenApprovalNotes())
                .approvedByWarden(entity.getApprovedByWarden() != null ? UserId.of(entity.getApprovedByWarden()) : null)
                .executionDate(entity.getExecutionDate())
                .executionNotes(entity.getExecutionNotes())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
