package lk.ac.ou.unieligible.dto;

import java.time.LocalDateTime;

public class ConcurrentRegistrationDto {
    private Long id;
    private String courseCode;
    private String courseName;
    private String academicSession;
    private Boolean active;
    private LocalDateTime registeredAt;

    public ConcurrentRegistrationDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getAcademicSession() { return academicSession; }
    public void setAcademicSession(String academicSession) { this.academicSession = academicSession; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public LocalDateTime getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(LocalDateTime registeredAt) { this.registeredAt = registeredAt; }
}

