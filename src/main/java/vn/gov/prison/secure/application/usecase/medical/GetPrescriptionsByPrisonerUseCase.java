package vn.gov.prison.secure.application.usecase.medical;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gov.prison.secure.application.dto.medical.PrescriptionResponse;
import vn.gov.prison.secure.application.mapper.MedicalMapper;
import vn.gov.prison.secure.domain.model.medical.MedicalRecord;
import vn.gov.prison.secure.domain.model.prisoner.PrisonerId;
import vn.gov.prison.secure.domain.repository.MedicalRecordRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetPrescriptionsByPrisonerUseCase {

    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalMapper medicalMapper;

    @Transactional(readOnly = true)
    public List<PrescriptionResponse> execute(UUID prisonerId) {
        List<MedicalRecord> records = medicalRecordRepository.findByPrisonerId(
                PrisonerId.of(prisonerId.toString()));

        return records.stream()
                .flatMap(record -> record.getPrescriptions().stream())
                .map(medicalMapper::toResponse)
                .collect(Collectors.toList());
    }
}
