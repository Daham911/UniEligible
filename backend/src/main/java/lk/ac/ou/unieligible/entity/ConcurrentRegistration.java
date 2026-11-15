package lk.ac.ou.unieligible.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "concurrent_registrations", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "course_code", "academic_session"})
})
public class ConcurrentRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnoreProperties("concurrentRegistrations")
    private Student student;

    @Column(name = "course_code", nullable = false, length = 20)
    private String courseCode;

    @Column(name = "academic_session", nullable = false, length = 20)
    private String academicSession = "2026/2027";

    @Column(name = "is_active", nullable = false)
    private Boolean active = true;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt = LocalDateTime.now();

    public ConcurrentRegistration() {}

    public ConcurrentRegistration(Student student, String courseCode, String academicSession, Boolean active) {
        this.student = student;
        this.courseCode = courseCode;
        this.academicSession = academicSession;
        this.active = active != null ? active : true;
        this.registeredAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getAcademicSession() { return academicSession; }
    public void setAcademicSession(String academicSession) { this.academicSession = academicSession; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public LocalDateTime getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(LocalDateTime registeredAt) { this.registeredAt = registeredAt; }
}

