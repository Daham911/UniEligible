package lk.ac.ou.unieligible.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "prerequisite_rules")
public class PrerequisiteRule {

    public enum RuleType {
        COURSE_RESULT,
        CONCURRENT_REGISTRATION,
        PASSED_CREDITS_TOTAL,
        PASSED_CREDITS_AT_LEVEL,
        CA_CREDITS_AT_LEVEL,
        EXEMPTION_CONDITION,
        SPECIAL_RESTRICTION,
        ADMIN_VERIFICATION_REQUIRED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    @JsonIgnoreProperties("rules")
    private PrerequisiteRuleGroup group;

    @Enumerated(EnumType.STRING)
    @Column(name = "rule_type", nullable = false, length = 35)
    private RuleType ruleType;

    @Column(name = "prerequisite_course_code", length = 20)
    private String prerequisiteCourseCode;

    @Column(name = "required_status", length = 10)
    private String requiredStatus; // "P", "CA", "CR"

    @Column(name = "min_credits")
    private Integer minCredits;

    @Column(name = "target_levels", length = 20)
    private String targetLevels; // e.g. "3", "4", "3,4", "5"

    @Column(name = "rule_note", length = 255)
    private String ruleNote;

    @Column(name = "is_verified", nullable = false)
    private Boolean verified = true;

    @Column(name = "description", length = 255)
    private String description;

    public PrerequisiteRule() {}

    public PrerequisiteRule(PrerequisiteRuleGroup group, RuleType ruleType, String prerequisiteCourseCode,
                            String requiredStatus, Integer minCredits, String targetLevels,
                            String ruleNote, Boolean verified, String description) {
        this.group = group;
        this.ruleType = ruleType;
        this.prerequisiteCourseCode = prerequisiteCourseCode;
        this.requiredStatus = requiredStatus;
        this.minCredits = minCredits;
        this.targetLevels = targetLevels;
        this.ruleNote = ruleNote;
        this.verified = verified != null ? verified : true;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PrerequisiteRuleGroup getGroup() { return group; }
    public void setGroup(PrerequisiteRuleGroup group) { this.group = group; }

    public RuleType getRuleType() { return ruleType; }
    public void setRuleType(RuleType ruleType) { this.ruleType = ruleType; }

    public String getPrerequisiteCourseCode() { return prerequisiteCourseCode; }
    public void setPrerequisiteCourseCode(String prerequisiteCourseCode) { this.prerequisiteCourseCode = prerequisiteCourseCode; }

    public String getRequiredStatus() { return requiredStatus; }
    public void setRequiredStatus(String requiredStatus) { this.requiredStatus = requiredStatus; }

    public Integer getMinCredits() { return minCredits; }
    public void setMinCredits(Integer minCredits) { this.minCredits = minCredits; }

    public String getTargetLevels() { return targetLevels; }
    public void setTargetLevels(String targetLevels) { this.targetLevels = targetLevels; }

    public String getRuleNote() { return ruleNote; }
    public void setRuleNote(String ruleNote) { this.ruleNote = ruleNote; }

    public Boolean getVerified() { return verified; }
    public void setVerified(Boolean verified) { this.verified = verified; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

