package lk.ac.ou.unieligible.dto;

import java.util.ArrayList;
import java.util.List;

public class EligibilityResultDto {
    private String courseCode;
    private String courseName;
    private Integer academicYear;
    private Integer level;
    private Integer credits;
    private String semester;
    private Boolean compulsory;
    private Boolean offered;
    private String status; // "ELIGIBLE", "NOT_ELIGIBLE", "PENDING_INFORMATION", "CURRENTLY_NOT_OFFERED"
    private Boolean eligible;
    private String prerequisiteDisplayText;
    private String deliveryCenter;
    private String specialRestriction;
    private String discrepancyFlag;
    private List<String> reasons = new ArrayList<>();
    private List<EligibilityReasonItemDto> detailedReasons = new ArrayList<>();

    public EligibilityResultDto() {}

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public Integer getAcademicYear() { return academicYear; }
    public void setAcademicYear(Integer academicYear) { this.academicYear = academicYear; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public Boolean getCompulsory() { return compulsory; }
    public void setCompulsory(Boolean compulsory) { this.compulsory = compulsory; }

    public Boolean getOffered() { return offered; }
    public void setOffered(Boolean offered) { this.offered = offered; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Boolean getEligible() { return eligible; }
    public void setEligible(Boolean eligible) { this.eligible = eligible; }

    public String getPrerequisiteDisplayText() { return prerequisiteDisplayText; }
    public void setPrerequisiteDisplayText(String prerequisiteDisplayText) { this.prerequisiteDisplayText = prerequisiteDisplayText; }

    public String getDeliveryCenter() { return deliveryCenter; }
    public void setDeliveryCenter(String deliveryCenter) { this.deliveryCenter = deliveryCenter; }

    public String getSpecialRestriction() { return specialRestriction; }
    public void setSpecialRestriction(String specialRestriction) { this.specialRestriction = specialRestriction; }

    public String getDiscrepancyFlag() { return discrepancyFlag; }
    public void setDiscrepancyFlag(String discrepancyFlag) { this.discrepancyFlag = discrepancyFlag; }

    public List<String> getReasons() { return reasons; }
    public void setReasons(List<String> reasons) { this.reasons = reasons; }

    public List<EligibilityReasonItemDto> getDetailedReasons() { return detailedReasons; }
    public void setDetailedReasons(List<EligibilityReasonItemDto> detailedReasons) { this.detailedReasons = detailedReasons; }
}

