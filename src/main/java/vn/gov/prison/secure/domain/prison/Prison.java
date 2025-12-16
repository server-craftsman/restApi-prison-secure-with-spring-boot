package vn.gov.prison.secure.domain.prison;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import vn.gov.prison.secure.domain.user.UserId;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class Prison {
    private PrisonId id;
    private String prisonCode;
    private String prisonName;
    private String location;
    private String address;
    private Integer capacity;
    private Integer currentPopulation;
    private UserId wardenId;
    private PrisonStatus status;
    private LocalDate establishedDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Prison create(
            String prisonCode,
            String prisonName,
            String location,
            Integer capacity,
            UserId wardenId) {
        return Prison.builder()
                .id(PrisonId.generate())
                .prisonCode(prisonCode)
                .prisonName(prisonName)
                .location(location)
                .capacity(capacity)
                .currentPopulation(0)
                .wardenId(wardenId)
                .status(PrisonStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public boolean canAcceptPrisoner() {
        return status == PrisonStatus.ACTIVE && currentPopulation < capacity;
    }

    public void incrementPopulation() {
        if (!canAcceptPrisoner()) {
            throw new IllegalStateException("Prison is at capacity or not active");
        }
        this.currentPopulation++;
        this.updatedAt = LocalDateTime.now();
    }

    public void decrementPopulation() {
        if (currentPopulation <= 0) {
            throw new IllegalStateException("Population cannot be negative");
        }
        this.currentPopulation--;
        this.updatedAt = LocalDateTime.now();
    }

    public double getOccupancyRate() {
        if (capacity == 0)
            return 0.0;
        return (double) currentPopulation / capacity * 100;
    }

    public Integer getAvailableSpace() {
        return capacity - currentPopulation;
    }
}
