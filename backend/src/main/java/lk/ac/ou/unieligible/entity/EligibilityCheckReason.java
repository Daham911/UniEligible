package lk.ac.ou.unieligible.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "eligibility_check_reasons")
public class EligibilityCheckReason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eligibility_check_id", nullable = false)
    @JsonIgnoreProperties("reasons")
    private EligibilityCheck eligibilityCheck;

    @Column(name = "rule_description", length = 255)
    private String ruleDescription;

    @Column(name = "reason_message", nullable = false, length = 500)
    private String reasonMessage;

    @Column(name = "is_satisfied", nullable = false)
    private Boolean satisfied;

    public EligibilityCheckReason() {}

    public EligibilityCheckReason(String ruleDescription, String reasonMessage, Boolean satisfied) {
        this.ruleDescription = ruleDescription;
        this.reasonMessage = reasonMessage;
        this.satisfied = satisfied;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EligibilityCheck getEligibilityCheck() { return eligibilityCheck; }
    public void setEligibilityCheck(EligibilityCheck eligibilityCheck) { this.eligibilityCheck = eligibilityCheck; }

    public String getRuleDescription() { return ruleDescription; }
    public void setRuleDescription(String ruleDescription) { this.ruleDescription = ruleDescription; }

    public String getReasonMessage() { return reasonMessage; }
    public void setReasonMessage(String reasonMessage) { this.reasonMessage = reasonMessage; }

    public Boolean getSatisfied() { return satisfied; }
    public void setSatisfied(Boolean satisfied) { this.satisfied = satisfied; }
}

