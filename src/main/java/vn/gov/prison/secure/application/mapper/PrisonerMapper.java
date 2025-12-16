package vn.gov.prison.secure.application.mapper;

import vn.gov.prison.secure.application.dto.response.PrisonerResponse;
import vn.gov.prison.secure.domain.model.prisoner.BiometricData;
import vn.gov.prison.secure.domain.model.prisoner.Prisoner;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Prisoner domain model and DTOs
 * Following Mapper Pattern and SRP
 */
public class PrisonerMapper {

    /**
     * Convert Prisoner domain entity to PrisonerResponse DTO
     */
    public PrisonerResponse toResponse(Prisoner prisoner) {
        if (prisoner == null) {
            return null;
        }

        var demographicInfo = prisoner.getDemographicInfo();
        var physicalDesc = prisoner.getPhysicalDescription();

        return new PrisonerResponse(
                // ID Info
                prisoner.getId().getValue(),
                prisoner.getPrisonerNumber(),

                // Demographic
                demographicInfo.getFirstName(),
                demographicInfo.getLastName(),
                demographicInfo.getMiddleName(),
                demographicInfo.getFullName(),
                demographicInfo.getDateOfBirth(),
                demographicInfo.getAge(),
                demographicInfo.getGender().name(),
                demographicInfo.getNationality(),
                demographicInfo.getPlaceOfBirth(),
                demographicInfo.getNationalIdNumber(),

                // Physical
                physicalDesc != null ? physicalDesc.getHeightCm() : null,
                physicalDesc != null ? physicalDesc.getWeightKg() : null,
                physicalDesc != null ? physicalDesc.getEyeColor() : null,
                physicalDesc != null ? physicalDesc.getHairColor() : null,
                physicalDesc != null ? physicalDesc.getBloodType() : null,
                physicalDesc != null ? physicalDesc.getDistinctiveMarks() : List.of(),
                physicalDesc != null ? physicalDesc.getScars() : List.of(),
                physicalDesc != null ? physicalDesc.getTattoos() : List.of(),

                // Status
                prisoner.getStatus().name(),
                prisoner.getAdmissionDate(),
                prisoner.getReleaseDate(),
                prisoner.getDaysInCustody(),

                // Location
                prisoner.getAssignedFacility(),
                prisoner.getCurrentBlock(),
                prisoner.getCurrentCell(),

                // Biometric
                prisoner.hasHighQualityBiometrics(),
                prisoner.getLatestBiometricData()
                        .map(BiometricData::getAvailableTypes)
                        .orElse(java.util.Set.of())
                        .stream()
                        .map(Enum::name)
                        .collect(Collectors.toList()),

                // Metadata
                prisoner.getCreatedAt(),
                prisoner.getUpdatedAt(),
                prisoner.getCreatedBy(),
                prisoner.getUpdatedBy());
    }

    /**
     * Convert list of Prisoners to list of PrisonerResponse DTOs
     */
    public List<PrisonerResponse> toResponseList(List<Prisoner> prisoners) {
        if (prisoners == null) {
            return List.of();
        }

        return prisoners.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
