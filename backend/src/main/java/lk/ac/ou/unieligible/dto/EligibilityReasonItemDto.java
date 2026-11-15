package lk.ac.ou.unieligible.dto;

public class EligibilityReasonItemDto {
    private String ruleDescription;
    private String reasonMessage;
    private Boolean satisfied;

    public EligibilityReasonItemDto() {}

    public EligibilityReasonItemDto(String ruleDescription, String reasonMessage, Boolean satisfied) {
        this.ruleDescription = ruleDescription;
        this.reasonMessage = reasonMessage;
        this.satisfied = satisfied;
    }

    public String getRuleDescription() { return ruleDescription; }
    public void setRuleDescription(String ruleDescription) { this.ruleDescription = ruleDescription; }

    public String getReasonMessage() { return reasonMessage; }
    public void setReasonMessage(String reasonMessage) { this.reasonMessage = reasonMessage; }

    public Boolean getSatisfied() { return satisfied; }
    public void setSatisfied(Boolean satisfied) { this.satisfied = satisfied; }
}

