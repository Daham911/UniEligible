package lk.ac.ou.unieligible.dto;

public class CourseDto {
    private String code;
    private String name;
    private String departmentCode;
    private String departmentName;
    private String categoryCode;
    private String categoryName;
    private Integer level;
    private Integer academicYear;
    private Integer credits;
    private String creditRatingNote;
    private String semester;
    private Boolean compulsory;
    private Boolean degreeCredit;
    private Boolean gpaContributing;
    private Boolean virtual;
    private String prerequisiteDisplayText;
    private String remarks;
    private Boolean offered;
    private String deliveryCenter;
    private String specialRestriction;
    private String discrepancyFlag;

    public CourseDto() {}

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartmentCode() { return departmentCode; }
    public void setDepartmentCode(String departmentCode) { this.departmentCode = departmentCode; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String getCategoryCode() { return categoryCode; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public Integer getAcademicYear() { return academicYear; }
    public void setAcademicYear(Integer academicYear) { this.academicYear = academicYear; }

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }

    public String getCreditRatingNote() { return creditRatingNote; }
    public void setCreditRatingNote(String creditRatingNote) { this.creditRatingNote = creditRatingNote; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public Boolean getCompulsory() { return compulsory; }
    public void setCompulsory(Boolean compulsory) { this.compulsory = compulsory; }

    public Boolean getDegreeCredit() { return degreeCredit; }
    public void setDegreeCredit(Boolean degreeCredit) { this.degreeCredit = degreeCredit; }

    public Boolean getGpaContributing() { return gpaContributing; }
    public void setGpaContributing(Boolean gpaContributing) { this.gpaContributing = gpaContributing; }

    public Boolean getVirtual() { return virtual; }
    public void setVirtual(Boolean virtual) { this.virtual = virtual; }

    public String getPrerequisiteDisplayText() { return prerequisiteDisplayText; }
    public void setPrerequisiteDisplayText(String prerequisiteDisplayText) { this.prerequisiteDisplayText = prerequisiteDisplayText; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public Boolean getOffered() { return offered; }
    public void setOffered(Boolean offered) { this.offered = offered; }

    public String getDeliveryCenter() { return deliveryCenter; }
    public void setDeliveryCenter(String deliveryCenter) { this.deliveryCenter = deliveryCenter; }

    public String getSpecialRestriction() { return specialRestriction; }
    public void setSpecialRestriction(String specialRestriction) { this.specialRestriction = specialRestriction; }

    public String getDiscrepancyFlag() { return discrepancyFlag; }
    public void setDiscrepancyFlag(String discrepancyFlag) { this.discrepancyFlag = discrepancyFlag; }
}

