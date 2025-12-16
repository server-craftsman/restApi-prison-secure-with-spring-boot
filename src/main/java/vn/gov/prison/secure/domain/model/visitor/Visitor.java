package vn.gov.prison.secure.domain.model.visitor;

import vn.gov.prison.secure.domain.model.common.BaseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Visitor Aggregate Root
 * Manages visitor registration and visit scheduling
 */
public class Visitor extends BaseEntity<VisitorId> {

    private String firstName;
    private String lastName;
    private String nationalIdNumber;
    private String phoneNumber;
    private String email;
    private String address;
    private Relationship relationship;
    private VisitorStatus status;
    private List<Visit> visits;
    private String photoUrl;
    private boolean backgroundCheckCompleted;
    private String backgroundCheckNotes;

    private Visitor() {
        super();
        this.visits = new ArrayList<>();
        this.status = VisitorStatus.PENDING_APPROVAL;
        this.backgroundCheckCompleted = false;
    }

    /**
     * Factory method for visitor registration
     */
    public static Visitor register(String firstName,
            String lastName,
            String nationalIdNumber,
            Relationship relationship) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (nationalIdNumber == null || nationalIdNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("National ID number is required");
        }

        Visitor visitor = new Visitor();
        visitor.id = VisitorId.generate();
        visitor.firstName = firstName;
        visitor.lastName = lastName;
        visitor.nationalIdNumber = nationalIdNumber;
        visitor.relationship = relationship;

        return visitor;
    }

    /**
     * Business logic: Approve visitor after background check
     */
    public void approve() {
        if (!backgroundCheckCompleted) {
            throw new IllegalStateException("Background check must be completed before approval");
        }
        if (status != VisitorStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Can only approve pending visitors");
        }

        this.status = VisitorStatus.APPROVED;
        updateTimestamp();
    }

    /**
     * Business logic: Reject visitor
     */
    public void reject(String reason) {
        if (status == VisitorStatus.APPROVED) {
            throw new IllegalStateException("Cannot reject approved visitor");
        }

        this.status = VisitorStatus.REJECTED;
        this.backgroundCheckNotes = reason;
        updateTimestamp();
    }

    /**
     * Business logic: Suspend visitor privileges
     */
    public void suspend(String reason) {
        if (status != VisitorStatus.APPROVED) {
            throw new IllegalStateException("Can only suspend approved visitors");
        }

        this.status = VisitorStatus.SUSPENDED;
        this.backgroundCheckNotes = reason;
        updateTimestamp();
    }

    /**
     * Business logic: Complete background check
     */
    public void completeBackgroundCheck(boolean passed, String notes) {
        this.backgroundCheckCompleted = true;
        this.backgroundCheckNotes = notes;

        if (!passed) {
            this.status = VisitorStatus.REJECTED;
        }
        updateTimestamp();
    }

    /**
     * Business logic: Schedule visit
     */
    public void scheduleVisit(Visit visit) {
        if (visit == null) {
            throw new IllegalArgumentException("Visit cannot be null");
        }
        if (status != VisitorStatus.APPROVED) {
            throw new IllegalStateException("Only approved visitors can schedule visits");
        }

        this.visits.add(visit);
        updateTimestamp();
    }

    /**
     * Get full name
     */
    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }

    /**
     * Check if can schedule visit
     */
    public boolean canScheduleVisit() {
        return status == VisitorStatus.APPROVED && backgroundCheckCompleted;
    }

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNationalIdNumber() {
        return nationalIdNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        updateTimestamp();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        updateTimestamp();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        updateTimestamp();
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public VisitorStatus getStatus() {
        return status;
    }

    public List<Visit> getVisits() {
        return Collections.unmodifiableList(visits);
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        updateTimestamp();
    }

    public boolean isBackgroundCheckCompleted() {
        return backgroundCheckCompleted;
    }

    public String getBackgroundCheckNotes() {
        return backgroundCheckNotes;
    }

    public enum Relationship {
        SPOUSE("Spouse"),
        PARENT("Parent"),
        CHILD("Child"),
        SIBLING("Sibling"),
        RELATIVE("Relative"),
        FRIEND("Friend"),
        ATTORNEY("Attorney"),
        CLERGY("Clergy"),
        OTHER("Other");

        private final String displayName;

        Relationship(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum VisitorStatus {
        PENDING_APPROVAL, APPROVED, REJECTED, SUSPENDED, BANNED
    }
}
