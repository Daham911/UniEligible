package lk.ac.ou.unieligible.dto;

public class CategoryCreditProgressDto {
    private String categoryCode;
    private String categoryName;
    private Integer earnedCredits;
    private Integer exemptedCredits;
    private Integer totalRecognizedCredits;
    private Integer minRequired;
    private Integer maxAllowed;
    private Boolean satisfied;
    private Integer remainingCredits;

    public CategoryCreditProgressDto() {}

    public CategoryCreditProgressDto(String categoryCode, String categoryName, Integer earnedCredits,
                                    Integer exemptedCredits, Integer totalRecognizedCredits,
                                    Integer minRequired, Integer maxAllowed, Boolean satisfied, Integer remainingCredits) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.earnedCredits = earnedCredits;
        this.exemptedCredits = exemptedCredits;
        this.totalRecognizedCredits = totalRecognizedCredits;
        this.minRequired = minRequired;
        this.maxAllowed = maxAllowed;
        this.satisfied = satisfied;
        this.remainingCredits = remainingCredits;
    }

    public String getCategoryCode() { return categoryCode; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public Integer getEarnedCredits() { return earnedCredits; }
    public void setEarnedCredits(Integer earnedCredits) { this.earnedCredits = earnedCredits; }

    public Integer getExemptedCredits() { return exemptedCredits; }
    public void setExemptedCredits(Integer exemptedCredits) { this.exemptedCredits = exemptedCredits; }

    public Integer getTotalRecognizedCredits() { return totalRecognizedCredits; }
    public void setTotalRecognizedCredits(Integer totalRecognizedCredits) { this.totalRecognizedCredits = totalRecognizedCredits; }

    public Integer getMinRequired() { return minRequired; }
    public void setMinRequired(Integer minRequired) { this.minRequired = minRequired; }

    public Integer getMaxAllowed() { return maxAllowed; }
    public void setMaxAllowed(Integer maxAllowed) { this.maxAllowed = maxAllowed; }

    public Boolean getSatisfied() { return satisfied; }
    public void setSatisfied(Boolean satisfied) { this.satisfied = satisfied; }

    public Integer getRemainingCredits() { return remainingCredits; }
    public void setRemainingCredits(Integer remainingCredits) { this.remainingCredits = remainingCredits; }
}

