package vn.gov.prison.secure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main Application Class for Prison-Secure System
 * Following Clean Architecture with 4 layers:
 * - Domain (core business logic)
 * - Application (use cases)
 * - Infrastructure (persistence, security, external services)
 * - Presentation (REST API controllers)
 * 
 * Design Patterns Implemented:
 * - Repository Pattern
 * - Use Case Pattern
 * - DTO Pattern
 * - Mapper Pattern
 * - Adapter Pattern
 * - Domain Event Pattern
 * - Factory Pattern
 * - Builder Pattern
 * 
 * SOLID Principles:
 * - Single Responsibility: Each class has one reason to change
 * - Open/Closed: Extensible without modification
 * - Liskov Substitution: Proper inheritance hierarchy
 * - Interface Segregation: Focused interfaces
 * - Dependency Inversion: Depends on abstractions
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "vn.gov.prison.secure.infrastructure.persistence.repository")
public class PrisonSecureApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrisonSecureApplication.class, args);
	}

}
