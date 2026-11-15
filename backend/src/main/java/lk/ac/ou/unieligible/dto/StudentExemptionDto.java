package lk.ac.ou.unieligible.dto;

import java.time.LocalDate;

public class StudentExemptionDto {
    private Long id;
    private String courseCode;
    private String courseName;
    private String categoryCode;
    private Integer level;
    private Integer creditValue;
    private String exemptionReference;
    private LocalDate grantedDate;
    private Boolean verified;

    public StudentExemptionDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

