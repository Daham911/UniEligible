package lk.ac.ou.unieligible.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "eligibility_checks")
public class EligibilityCheck {

    public enum EligibilityStatus {
        ELIGIBLE,
        NOT_ELIGIBLE,
        PENDING_INFORMATION,
        CURRENTLY_NOT_OFFERED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false, length = 50)
    private String studentId;

    @Column(name = "course_code", nullable = false, length = 20)
    private String courseCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private EligibilityStatus status;

    @Column(name = "checked_at")
    private LocalDateTime checkedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "eligibilityCheck", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("eligibilityCheck")
    private List<EligibilityCheckReason> reasons = new ArrayList<>();

    public EligibilityCheck() {}

    public EligibilityCheck(String studentId, String courseCode, EligibilityStatus status) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.status = status;
        this.checkedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public EligibilityStatus getStatus() { return status; }
    public void setStatus(EligibilityStatus status) { this.status = status; }

    public LocalDateTime getCheckedAt() { return checkedAt; }
    public void setCheckedAt(LocalDateTime checkedAt) { this.checkedAt = checkedAt; }

    public List<EligibilityCheckReason> getReasons() { return reasons; }
    public void setReasons(List<EligibilityCheckReason> reasons) { this.reasons = reasons; }

    public void addReason(EligibilityCheckReason reason) {
        reasons.add(reason);
        reason.setEligibilityCheck(this);
    }
}

