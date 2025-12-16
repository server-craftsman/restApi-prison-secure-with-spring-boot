package vn.gov.prison.secure.domain.repository;

import vn.gov.prison.secure.domain.model.visitor.Visitor;
import vn.gov.prison.secure.domain.model.visitor.VisitorId;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Visitor aggregate
 */
public interface VisitorRepository {

    Visitor save(Visitor visitor);

    Optional<Visitor> findById(VisitorId id);

    Optional<Visitor> findByNationalIdNumber(String nationalIdNumber);

    List<Visitor> findByStatus(Visitor.VisitorStatus status);

    List<Visitor> searchByName(String firstName, String lastName);

    List<Visitor> findPendingApproval();

    List<Visitor> findAll(int page, int size);

    void delete(VisitorId id);

    long count();
}
