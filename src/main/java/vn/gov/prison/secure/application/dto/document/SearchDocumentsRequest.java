package vn.gov.prison.secure.application.dto.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchDocumentsRequest {

    private UUID prisonerId;
    private String documentType;
    private String category;
    private String keyword;
    private Integer page;
    private Integer size;
}
