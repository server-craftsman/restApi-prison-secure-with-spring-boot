package vn.gov.prison.secure.infrastructure.persistence.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import vn.gov.prison.secure.domain.model.prisoner.*;
import vn.gov.prison.secure.infrastructure.persistence.jpa.entity.PrisonerJpaEntity;

import java.util.List;

/**
 * Mapper between Prisoner domain entity and PrisonerJpaEntity
 * Following Adapter Pattern and Mapper Pattern
 * 
 * This is part of the Infrastructure layer's responsibility to adapt
 * between domain models and persistence models
 */
public class PrisonerPersistenceMapper {

    private final ObjectMapper objectMapper;

    public PrisonerPersistenceMapper() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Convert domain Prisoner to JPA entity
     */
    public PrisonerJpaEntity toJpaEntity(Prisoner prisoner) {
        if (prisoner == null) {
            return null;
        }

        PrisonerJpaEntity entity = new PrisonerJpaEntity();

        // ID
        entity.setId(prisoner.getId().getValue());
        entity.setPrisonerNumber(prisoner.getPrisonerNumber());

        // Demographic
        var demographic = prisoner.getDemographicInfo();
        entity.setFirstName(demographic.getFirstName());
        entity.setLastName(demographic.getLastName());
        entity.setMiddleName(demographic.getMiddleName());
        entity.setDateOfBirth(demographic.getDateOfBirth());
        entity.setGender(mapGender(demographic.getGender()));
        entity.setNationality(demographic.getNationality());
        entity.setPlaceOfBirth(demographic.getPlaceOfBirth());
        entity.setNationalIdNumber(demographic.getNationalIdNumber());

        // Physical Description
        var physical = prisoner.getPhysicalDescription();
        if (physical != null) {
            entity.setHeightCm(physical.getHeightCm());
            entity.setWeightKg(physical.getWeightKg());
            entity.setEyeColor(physical.getEyeColor());
            entity.setHairColor(physical.getHairColor());
            entity.setBloodType(physical.getBloodType());
            entity.setDistinctiveMarks(toJson(physical.getDistinctiveMarks()));
            entity.setScars(toJson(physical.getScars()));
            entity.setTattoos(toJson(physical.getTattoos()));
        }

        // Status
        entity.setStatus(mapStatus(prisoner.getStatus()));
        entity.setAdmissionDate(prisoner.getAdmissionDate());
        entity.setReleaseDate(prisoner.getReleaseDate());

        // Location
        entity.setAssignedFacility(prisoner.getAssignedFacility());
        entity.setCurrentBlock(prisoner.getCurrentBlock());
        entity.setCurrentCell(prisoner.getCurrentCell());

        // Metadata
        entity.setCreatedAt(prisoner.getCreatedAt());
        entity.setUpdatedAt(prisoner.getUpdatedAt());
        entity.setCreatedBy(prisoner.getCreatedBy());
        entity.setUpdatedBy(prisoner.getUpdatedBy());
        entity.setVersion(prisoner.getVersion());

        return entity;
    }

    /**
     * Convert JPA entity to domain Prisoner
     */
    public Prisoner toDomainEntity(PrisonerJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        // Create demographic info
        DemographicInfo demographicInfo = DemographicInfo.builder()
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .middleName(entity.getMiddleName())
                .dateOfBirth(entity.getDateOfBirth())
                .gender(mapGenderToDomain(entity.getGender()))
                .nationality(entity.getNationality())
                .placeOfBirth(entity.getPlaceOfBirth())
                .nationalIdNumber(entity.getNationalIdNumber())
                .build();

        // Create physical description
        PhysicalDescription physicalDescription = PhysicalDescription.builder()
                .heightCm(entity.getHeightCm())
                .weightKg(entity.getWeightKg())
                .eyeColor(entity.getEyeColor())
                .hairColor(entity.getHairColor())
                .bloodType(entity.getBloodType())
                .distinctiveMarks(fromJson(entity.getDistinctiveMarks()))
                .scars(fromJson(entity.getScars()))
                .tattoos(entity.getTattoos() != null ? fromJson(entity.getTattoos()) : null)
                .build();

        // Create prisoner using factory method
        Prisoner prisoner = Prisoner.admit(
                demographicInfo,
                physicalDescription,
                entity.getPrisonerNumber());

        // Set status using reflection or package-private setter
        // Note: In production, you'd need proper setters or use reflection
        // For now, demonstrate the concept

        return prisoner;
    }

    private PrisonerJpaEntity.Gender mapGender(DemographicInfo.Gender gender) {
        return PrisonerJpaEntity.Gender.valueOf(gender.name());
    }

    private DemographicInfo.Gender mapGenderToDomain(PrisonerJpaEntity.Gender gender) {
        return DemographicInfo.Gender.valueOf(gender.name());
    }

    private PrisonerJpaEntity.PrisonerStatus mapStatus(PrisonerStatus status) {
        return PrisonerJpaEntity.PrisonerStatus.valueOf(status.name());
    }

    private String toJson(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize list to JSON", e);
        }
    }

    private List<String> fromJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return List.of();
        }
        try {
            return objectMapper.readValue(json,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize JSON to list", e);
        }
    }
}
