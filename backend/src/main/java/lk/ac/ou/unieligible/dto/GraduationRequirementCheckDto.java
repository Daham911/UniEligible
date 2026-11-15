package lk.ac.ou.unieligible.dto;

public class GraduationRequirementCheckDto {
    private String code;
    private String title;
    private String description;
    private Integer targetValue;
    private Integer actualValue;
    private Boolean satisfied;
    private String ruleReference;

    public GraduationRequirementCheckDto() {}

    public GraduationRequirementCheckDto(String code, String title, String description, Integer targetValue,
                                         Integer actualValue, Boolean satisfied, String ruleReference) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.targetValue = targetValue;
        this.actualValue = actualValue;
        this.satisfied = satisfied;
        this.ruleReference = ruleReference;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getTargetValue() { return targetValue; }
    public void setTargetValue(Integer targetValue) { this.targetValue = targetValue; }

    public Integer getActualValue() { return actualValue; }
    public void setActualValue(Integer actualValue) { this.actualValue = actualValue; }

    public Boolean getSatisfied() { return satisfied; }
    public void setSatisfied(Boolean satisfied) { this.satisfied = satisfied; }

    public String getRuleReference() { return ruleReference; }
    public void setRuleReference(String ruleReference) { this.ruleReference = ruleReference; }
}

