package vn.gov.prison.secure.application.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BiometricLoginRequest {
    private String fingerprintData;
    private String prisonerNumber; // Optional: for additional verification
}
