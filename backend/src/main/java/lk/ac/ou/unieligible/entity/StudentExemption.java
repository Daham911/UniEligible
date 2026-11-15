package lk.ac.ou.unieligible.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "student_exemptions")
public class StudentExemption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnoreProperties("exemptions")
    private Student student;

    @Column(name = "course_code", nullable = false, length = 20)
    private String courseCode;

    @Column(name = "course_name", length = 200)
    private String courseName;

    @Column(name = "category_code", length = 5)
    private String categoryCode; // e.g. "I", "M", "Z", etc.

    @Column(name = "course_level")
    private Integer level;

    @Column(name = "credit_value", nullable = false)
    private Integer creditValue;

    @Column(name = "exemption_reference", length = 150)
    private String exemptionReference;

    @Column(name = "granted_date")
    private LocalDate grantedDate = LocalDate.now();

    @Column(name = "is_verified", nullable = false)
    private Boolean verified = true;

    public StudentExemption() {}

    public StudentExemption(Student student, String courseCode, String courseName, String categoryCode,
                            Integer level, Integer creditValue, String exemptionReference, Boolean verified) {
        this.student = student;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.categoryCode = categoryCode;
        this.level = level;
        this.creditValue = creditValue;
        this.exemptionReference = exemptionReference;
        this.grantedDate = LocalDate.now();
        this.verified = verified != null ? verified : true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getCategoryCode() { return categoryCode; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public Integer getCreditValue() { return creditValue; }
    public void setCreditValue(Integer creditValue) { this.creditValue = creditValue; }

    public String getExemptionReference() { return exemptionReference; }
    public void setExemptionReference(String exemptionReference) { this.exemptionReference = exemptionReference; }

    public LocalDate getGrantedDate() { return grantedDate; }
    public void setGrantedDate(LocalDate grantedDate) { this.grantedDate = grantedDate; }

    public Boolean getVerified() { return verified; }
    public void setVerified(Boolean verified) { this.verified = verified; }
}

