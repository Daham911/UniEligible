package lk.ac.ou.unieligible.dto;

import java.time.LocalDateTime;

public class StudentAcademicRecordDto {
    private String courseCode;
    private String courseName;
    private String resultStatus;
    private Integer level;
    private Integer credits;
    private Integer academicYear;
    private String semester;
    private Boolean compulsory;
    private String academicYearTaken;
    private LocalDateTime updatedAt;

    public StudentAcademicRecordDto() {}

    public StudentAcademicRecordDto(String courseCode, String courseName, String resultStatus, Integer level,
                                    Integer credits, Integer academicYear, String semester, Boolean compulsory,
                                    String academicYearTaken, LocalDateTime updatedAt) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.resultStatus = resultStatus;
        this.level = level;
        this.credits = credits;
        this.academicYear = academicYear;
        this.semester = semester;
        this.compulsory = compulsory;
        this.academicYearTaken = academicYearTaken;
        this.updatedAt = updatedAt;
    }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getResultStatus() { return resultStatus; }
    public void setResultStatus(String resultStatus) { this.resultStatus = resultStatus; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }

    public Integer getAcademicYear() { return academicYear; }
    public void setAcademicYear(Integer academicYear) { this.academicYear = academicYear; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public Boolean getCompulsory() { return compulsory; }
    public void setCompulsory(Boolean compulsory) { this.compulsory = compulsory; }

    public String getAcademicYearTaken() { return academicYearTaken; }
    public void setAcademicYearTaken(String academicYearTaken) { this.academicYearTaken = academicYearTaken; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

