package vn.gov.prison.secure.application.dto.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponse {

    private UUID id;
    private UUID prisonerId;
    private String documentType;
    private String category;
    private String title;
    private String description;
    private String fileName;
    private String fileUrl;
    private Long fileSize;
    private LocalDateTime uploadedAt;
    private String uploadedBy;
    private String status;
}
