package vn.gov.prison.secure.application.usecase.prisoner;

import vn.gov.prison.secure.application.dto.request.RegisterPrisonerRequest;
import vn.gov.prison.secure.application.dto.response.PrisonerResponse;
import vn.gov.prison.secure.application.mapper.PrisonerMapper;
import vn.gov.prison.secure.domain.event.PrisonerAdmittedEvent;
import vn.gov.prison.secure.domain.model.prisoner.*;
import vn.gov.prison.secure.domain.repository.PrisonerRepository;

/**
 * Use Case: Register New Prisoner
 * Coordinates the admission process
 * 
 * Following Use Case Pattern and SRP
 * Each use case handles one specific business operation
 */
public class RegisterPrisonerUseCase {

    private final PrisonerRepository prisonerRepository;
    private final PrisonerMapper prisonerMapper;

    public RegisterPrisonerUseCase(PrisonerRepository prisonerRepository,
            PrisonerMapper prisonerMapper) {
        this.prisonerRepository = prisonerRepository;
        this.prisonerMapper = prisonerMapper;
    }

    /**
     * Execute the register prisoner use case
     */
    public PrisonerResponse execute(RegisterPrisonerRequest request) {
        // Validate prisoner number uniqueness
        if (prisonerRepository.existsByPrisonerNumber(request.prisonerNumber())) {
            throw new IllegalArgumentException(
                    "Prisoner number already exists: " + request.prisonerNumber());
        }

        // Create demographic info value object
        DemographicInfo demographicInfo = DemographicInfo.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .middleName(request.middleName())
                .dateOfBirth(request.dateOfBirth())
                .gender(DemographicInfo.Gender.valueOf(request.gender()))
                .nationality(request.nationality())
                .placeOfBirth(request.placeOfBirth())
                .nationalIdNumber(request.nationalIdNumber())
                .build();

        // Create physical description value object
        PhysicalDescription physicalDescription = PhysicalDescription.builder()
                .heightCm(request.heightCm())
                .weightKg(request.weightKg())
                .eyeColor(request.eyeColor())
                .hairColor(request.hairColor())
                .bloodType(request.bloodType())
                .distinctiveMarks(request.distinctiveMarks())
                .scars(request.scars())
                .tattoos(request.tattoos())
                .build();

        // Create prisoner aggregate using factory method
        Prisoner prisoner = Prisoner.admit(
                demographicInfo,
                physicalDescription,
                request.prisonerNumber());

        // Set facility if provided
        if (request.assignedFacility() != null) {
            // Note: In real implementation, validate facility exists
            // prisoner.transferTo(request.assignedFacility());
        }

        // Persist prisoner
        Prisoner savedPrisoner = prisonerRepository.save(prisoner);

        // Publish domain event
        publishPrisonerAdmittedEvent(savedPrisoner);

        // Map to response DTO
        return prisonerMapper.toResponse(savedPrisoner);
    }

    private void publishPrisonerAdmittedEvent(Prisoner prisoner) {
        // In real implementation, use event publisher
        PrisonerAdmittedEvent event = new PrisonerAdmittedEvent(
                prisoner.getId().getValue(),
                prisoner.getPrisonerNumber(),
                prisoner.getDemographicInfo().getFullName(),
                prisoner.getAssignedFacility());

        // EventPublisher.publish(event);
    }
}
