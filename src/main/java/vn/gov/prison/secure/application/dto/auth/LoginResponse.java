package vn.gov.prison.secure.application.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String tokenType;
    private Long expiresIn;
    private UserInfo user;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private String id;
        private String username;
        private String fullName;
        private String userType; // PRISONER, GUARD, WARDEN
        private String email;
        private String prisonId;
        private String prisonName;
        private String zoneId;
        private String zoneName;
        private String tabletId;
        private List<String> permissions;
    }
}
