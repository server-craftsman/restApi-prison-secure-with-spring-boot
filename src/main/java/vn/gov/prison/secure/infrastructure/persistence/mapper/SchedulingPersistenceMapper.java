package vn.gov.prison.secure.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;
import vn.gov.prison.secure.domain.model.scheduling.*;
import vn.gov.prison.secure.infrastructure.persistence.entity.CourtDateJpaEntity;
import vn.gov.prison.secure.infrastructure.persistence.entity.ScheduleJpaEntity;

@Component
public class SchedulingPersistenceMapper {

    public ScheduleJpaEntity toJpaEntity(Schedule domain) {
        if (domain == null)
            return null;

        return ScheduleJpaEntity.builder()
                .id(domain.getId().getValue())
                .prisonerId(domain.getPrisonerId() != null ? domain.getPrisonerId().getValue() : null)
                .scheduleType(mapScheduleType(domain.getScheduleType()))
                .title(domain.getTitle())
                .description(domain.getDescription())
                .scheduledDate(domain.getScheduledDate())
                .endDate(domain.getEndDate())
                .durationMinutes(domain.getDurationMinutes())
                .location(domain.getLocation())
                .status(mapScheduleStatus(domain.getStatus()))
                .priority(mapSchedulePriority(domain.getPriority()))
                .reminderSent(domain.isReminderSent())
                .reminderDate(domain.getReminderDate())
                .build();
    }

    public Schedule toDomain(ScheduleJpaEntity jpa) {
        if (jpa == null)
            return null;

        return Schedule.builder()
                .id(ScheduleId.of(jpa.getId()))
                .prisonerId(jpa.getPrisonerId() != null ? PrisonerId.of(jpa.getPrisonerId()) : null)
                .scheduleType(mapScheduleTypeEnum(jpa.getScheduleType()))
                .title(jpa.getTitle())
                .description(jpa.getDescription())
                .scheduledDate(jpa.getScheduledDate())
                .endDate(jpa.getEndDate())
                .durationMinutes(jpa.getDurationMinutes())
                .location(jpa.getLocation())
                .status(mapScheduleStatusEnum(jpa.getStatus()))
                .priority(mapSchedulePriorityEnum(jpa.getPriority()))
                .reminderSent(jpa.getReminderSent() != null && jpa.getReminderSent())
                .reminderDate(jpa.getReminderDate())
                .createdBy(jpa.getCreatedBy())
                .build();
    }

    public CourtDateJpaEntity toJpaEntity(CourtDate domain) {
        if (domain == null)
            return null;

        return CourtDateJpaEntity.builder()
                .id(domain.getId())
                .scheduleId(domain.getScheduleId())
                .prisonerId(domain.getPrisonerId())
                .courtName(domain.getCourtName())
                .courtAddress(domain.getCourtAddress())
                .caseNumber(domain.getCaseNumber())
                .hearingType(mapHearingType(domain.getHearingType()))
                .judgeName(domain.getJudgeName())
                .attorneyName(domain.getAttorneyName())
                .attorneyContact(domain.getAttorneyContact())
                .charges(domain.getCharges())
                .outcome(domain.getOutcome())
                .verdict(mapVerdict(domain.getVerdict()))
                .sentence(domain.getSentence())
                .nextCourtDate(domain.getNextCourtDate())
                .transportArranged(domain.isTransportArranged())
                .transportNotes(domain.getTransportNotes())
                .build();
    }

    public CourtDate toDomain(CourtDateJpaEntity jpa) {
        if (jpa == null)
            return null;

        return CourtDate.builder()
                .id(jpa.getId())
                .scheduleId(jpa.getScheduleId())
                .prisonerId(jpa.getPrisonerId())
                .courtName(jpa.getCourtName())
                .courtAddress(jpa.getCourtAddress())
                .caseNumber(jpa.getCaseNumber())
                .hearingType(mapHearingTypeEnum(jpa.getHearingType()))
                .judgeName(jpa.getJudgeName())
                .attorneyName(jpa.getAttorneyName())
                .attorneyContact(jpa.getAttorneyContact())
                .charges(jpa.getCharges())
                .outcome(jpa.getOutcome())
                .verdict(mapVerdictEnum(jpa.getVerdict()))
                .sentence(jpa.getSentence())
                .nextCourtDate(jpa.getNextCourtDate())
                .transportArranged(jpa.getTransportArranged() != null && jpa.getTransportArranged())
                .transportNotes(jpa.getTransportNotes())
                .build();
    }

    // Mapping methods
    private ScheduleJpaEntity.ScheduleTypeEnum mapScheduleType(ScheduleType type) {
        return type != null ? ScheduleJpaEntity.ScheduleTypeEnum.valueOf(type.name()) : null;
    }

    private ScheduleType mapScheduleTypeEnum(ScheduleJpaEntity.ScheduleTypeEnum type) {
        return type != null ? ScheduleType.valueOf(type.name()) : null;
    }

    private ScheduleJpaEntity.ScheduleStatusEnum mapScheduleStatus(ScheduleStatus status) {
        return status != null ? ScheduleJpaEntity.ScheduleStatusEnum.valueOf(status.name()) : null;
    }

    private ScheduleStatus mapScheduleStatusEnum(ScheduleJpaEntity.ScheduleStatusEnum status) {
        return status != null ? ScheduleStatus.valueOf(status.name()) : null;
    }

    private ScheduleJpaEntity.SchedulePriorityEnum mapSchedulePriority(SchedulePriority priority) {
        return priority != null ? ScheduleJpaEntity.SchedulePriorityEnum.valueOf(priority.name()) : null;
    }

    private SchedulePriority mapSchedulePriorityEnum(ScheduleJpaEntity.SchedulePriorityEnum priority) {
        return priority != null ? SchedulePriority.valueOf(priority.name()) : null;
    }

    private CourtDateJpaEntity.HearingTypeEnum mapHearingType(CourtDate.HearingType type) {
        return type != null ? CourtDateJpaEntity.HearingTypeEnum.valueOf(type.name()) : null;
    }

    private CourtDate.HearingType mapHearingTypeEnum(CourtDateJpaEntity.HearingTypeEnum type) {
        return type != null ? CourtDate.HearingType.valueOf(type.name()) : null;
    }

    private CourtDateJpaEntity.VerdictEnum mapVerdict(CourtDate.Verdict verdict) {
        return verdict != null ? CourtDateJpaEntity.VerdictEnum.valueOf(verdict.name()) : null;
    }

    private CourtDate.Verdict mapVerdictEnum(CourtDateJpaEntity.VerdictEnum verdict) {
        return verdict != null ? CourtDate.Verdict.valueOf(verdict.name()) : null;
    }
}
