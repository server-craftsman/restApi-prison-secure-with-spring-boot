package vn.gov.prison.secure.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.gov.prison.secure.application.mapper.PrisonerMapper;
import vn.gov.prison.secure.application.usecase.prisoner.RegisterPrisonerUseCase;
import vn.gov.prison.secure.domain.repository.PrisonerRepository;
import vn.gov.prison.secure.infrastructure.persistence.mapper.PrisonerPersistenceMapper;

/**
 * Application Configuration
 * Following Dependency Injection and Inversion of Control principles
 * 
 * This configuration class wires together all layers:
 * - Domain (repositories interfaces)
 * - Application (use cases, mappers)
 * - Infrastructure (persistence mappers, implementations)
 */
@Configuration
public class ApplicationConfig {

    /**
     * Prisoner Persistence Mapper Bean
     */
    @Bean
    public PrisonerPersistenceMapper prisonerPersistenceMapper() {
        return new PrisonerPersistenceMapper();
    }

    /**
     * Prisoner Application Mapper Bean
     */
    @Bean
    public PrisonerMapper prisonerMapper() {
        return new PrisonerMapper();
    }

    /**
     * Register Prisoner Use Case Bean
     * Demonstrates Dependency Injection of repository and mapper
     */
    @Bean
    public RegisterPrisonerUseCase registerPrisonerUseCase(
            PrisonerRepository prisonerRepository,
            PrisonerMapper prisonerMapper) {
        return new RegisterPrisonerUseCase(prisonerRepository, prisonerMapper);
    }

    // Other use case beans would be defined here
    // Following Single Responsibility Principle - one use case per method
}
