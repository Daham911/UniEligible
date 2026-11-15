package lk.ac.ou.unieligible.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @Column(name = "code", length = 20)
    private String code;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_code", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_code", nullable = false)
    private CourseCategory category;

    @Column(name = "course_level", nullable = false)
    private Integer level;

    @Column(name = "academic_year", nullable = false)
    private Integer academicYear;

    @Column(name = "credits", nullable = false)
    private Integer credits;

    @Column(name = "credit_rating_note", length = 20)
    private String creditRatingNote;

    @Column(name = "semester", length = 20)
    private String semester;

    @Column(name = "is_compulsory", nullable = false)
    private Boolean compulsory = true;

    @Column(name = "is_degree_credit", nullable = false)
    private Boolean degreeCredit = true;

    @Column(name = "is_gpa_contributing", nullable = false)
    private Boolean gpaContributing = true;

    @Column(name = "is_virtual", nullable = false)
    private Boolean virtual = false;

    @Column(name = "prerequisite_display_text", length = 300)
    private String prerequisiteDisplayText;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "is_offered", nullable = false)
    private Boolean offered = true;

    @Column(name = "delivery_center", length = 100)
    private String deliveryCenter = "ALL";

    @Column(name = "special_restriction", columnDefinition = "TEXT")
    private String specialRestriction;

    @Column(name = "discrepancy_flag", columnDefinition = "TEXT")
    private String discrepancyFlag;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("groupOrder ASC")
    @JsonIgnoreProperties("course")
    private List<PrerequisiteRuleGroup> ruleGroups = new ArrayList<>();

    public Course() {}

    public Course(String code, String name, Department department, CourseCategory category,
                  Integer level, Integer academicYear, Integer credits, String creditRatingNote,
                  String semester, Boolean compulsory, Boolean degreeCredit, Boolean gpaContributing,
                  String prerequisiteDisplayText, String remarks) {
        this.code = code;
        this.name = name;
        this.department = department;
        this.category = category;
        this.level = level;
        this.academicYear = academicYear;
        this.credits = credits;
        this.creditRatingNote = creditRatingNote;
        this.semester = semester;
        this.compulsory = compulsory;
        this.degreeCredit = degreeCredit;
        this.gpaContributing = gpaContributing;
        this.prerequisiteDisplayText = prerequisiteDisplayText;
        this.remarks = remarks;
        this.offered = true;
        this.deliveryCenter = "ALL";
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public CourseCategory getCategory() { return category; }
    public void setCategory(CourseCategory category) { this.category = category; }

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

    public List<PrerequisiteRuleGroup> getRuleGroups() { return ruleGroups; }
    public void setRuleGroups(List<PrerequisiteRuleGroup> ruleGroups) { this.ruleGroups = ruleGroups; }
}

