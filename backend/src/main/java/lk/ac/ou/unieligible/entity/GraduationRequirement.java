package lk.ac.ou.unieligible.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "graduation_requirements")
public class GraduationRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "programme_id", nullable = false, length = 20)
    private String programmeId = "BSEHONS";

    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "target_value")
    private Integer targetValue;

    @Column(name = "category_code", length = 5)
    private String categoryCode;

    @Column(name = "target_level")
    private Integer targetLevel;

    @Column(name = "applies_to_exemptions", nullable = false)
    private Boolean appliesToExemptions = true;

    @Column(name = "rule_reference", length = 100)
    private String ruleReference;

    public GraduationRequirement() {}

    public GraduationRequirement(String programmeId, String code, String title, String description,
                                 Integer targetValue, String categoryCode, Integer targetLevel,
                                 Boolean appliesToExemptions, String ruleReference) {
        this.programmeId = programmeId;
        this.code = code;
        this.title = title;
        this.description = description;
        this.targetValue = targetValue;
        this.categoryCode = categoryCode;
        this.targetLevel = targetLevel;
        this.appliesToExemptions = appliesToExemptions != null ? appliesToExemptions : true;
        this.ruleReference = ruleReference;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProgrammeId() { return programmeId; }
    public void setProgrammeId(String programmeId) { this.programmeId = programmeId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getTargetValue() { return targetValue; }
    public void setTargetValue(Integer targetValue) { this.targetValue = targetValue; }

    public String getCategoryCode() { return categoryCode; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }

    public Integer getTargetLevel() { return targetLevel; }
    public void setTargetLevel(Integer targetLevel) { this.targetLevel = targetLevel; }

    public Boolean getAppliesToExemptions() { return appliesToExemptions; }
    public void setAppliesToExemptions(Boolean appliesToExemptions) { this.appliesToExemptions = appliesToExemptions; }

    public String getRuleReference() { return ruleReference; }
    public void setRuleReference(String ruleReference) { this.ruleReference = ruleReference; }
}

