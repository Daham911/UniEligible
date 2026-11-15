package lk.ac.ou.unieligible.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_academic_records", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "course_code"})
})
public class StudentAcademicRecord {

    public enum ResultStatus {
        PA,           // Passed
        FA,           // Failed
        CA,           // Continuous Assessment Pass
        NOT_ATTEMPTED // Not Attempted
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnoreProperties("academicRecords")
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_code", nullable = false)
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(name = "result_status", nullable = false, length = 20)
    private ResultStatus resultStatus = ResultStatus.NOT_ATTEMPTED;

    @Column(name = "academic_year_taken", length = 20)
    private String academicYearTaken;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public StudentAcademicRecord() {}

    public StudentAcademicRecord(Student student, Course course, ResultStatus resultStatus, String academicYearTaken) {
        this.student = student;
        this.course = course;
        this.resultStatus = resultStatus;
        this.academicYearTaken = academicYearTaken;
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public ResultStatus getResultStatus() { return resultStatus; }
    public void setResultStatus(ResultStatus resultStatus) { this.resultStatus = resultStatus; }

    public String getAcademicYearTaken() { return academicYearTaken; }
    public void setAcademicYearTaken(String academicYearTaken) { this.academicYearTaken = academicYearTaken; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

