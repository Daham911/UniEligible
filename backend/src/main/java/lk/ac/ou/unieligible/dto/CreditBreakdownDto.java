package lk.ac.ou.unieligible.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreditBreakdownDto {
    private Integer totalEarnedCredits = 0;
    private Integer totalExemptedCredits = 0;
    private Integer totalRecognizedCredits = 0;
    private Integer totalCACredits = 0;
    private Integer targetTotalCredits = 125;
    private Integer remainingDegreeCredits = 125;

    private Integer compulsoryCredits = 0;
    private Integer electiveCredits = 0;
    private Integer level6ElectiveCredits = 0;

    private Map<Integer, Integer> creditsByLevel = new HashMap<>();
    private Map<String, CategoryCreditProgressDto> creditsByCategory = new HashMap<>();
    private Map<String, Integer> creditsByDepartment = new HashMap<>();

    private List<String> unfulfilledRequirements = new ArrayList<>();

    public CreditBreakdownDto() {
        creditsByLevel.put(3, 0);
        creditsByLevel.put(4, 0);
        creditsByLevel.put(5, 0);
        creditsByLevel.put(6, 0);
    }

    public Integer getTotalEarnedCredits() { return totalEarnedCredits; }
    public void setTotalEarnedCredits(Integer totalEarnedCredits) { this.totalEarnedCredits = totalEarnedCredits; }

    public Integer getTotalExemptedCredits() { return totalExemptedCredits; }
    public void setTotalExemptedCredits(Integer totalExemptedCredits) { this.totalExemptedCredits = totalExemptedCredits; }

    public Integer getTotalRecognizedCredits() { return totalRecognizedCredits; }
    public void setTotalRecognizedCredits(Integer totalRecognizedCredits) { this.totalRecognizedCredits = totalRecognizedCredits; }

    public Integer getTotalCACredits() { return totalCACredits; }
    public void setTotalCACredits(Integer totalCACredits) { this.totalCACredits = totalCACredits; }

    public Integer getTargetTotalCredits() { return targetTotalCredits; }
    public void setTargetTotalCredits(Integer targetTotalCredits) { this.targetTotalCredits = targetTotalCredits; }

    public Integer getRemainingDegreeCredits() { return remainingDegreeCredits; }
    public void setRemainingDegreeCredits(Integer remainingDegreeCredits) { this.remainingDegreeCredits = remainingDegreeCredits; }

    public Integer getCompulsoryCredits() { return compulsoryCredits; }
    public void setCompulsoryCredits(Integer compulsoryCredits) { this.compulsoryCredits = compulsoryCredits; }
    public void setCompulsory(Integer compulsoryCredits) { this.compulsoryCredits = compulsoryCredits; }

    public Integer getElectiveCredits() { return electiveCredits; }
    public void setElectiveCredits(Integer electiveCredits) { this.electiveCredits = electiveCredits; }

    public Integer getLevel6ElectiveCredits() { return level6ElectiveCredits; }
    public void setLevel6ElectiveCredits(Integer level6ElectiveCredits) { this.level6ElectiveCredits = level6ElectiveCredits; }

    public Map<Integer, Integer> getCreditsByLevel() { return creditsByLevel; }
    public void setCreditsByLevel(Map<Integer, Integer> creditsByLevel) { this.creditsByLevel = creditsByLevel; }

    public Map<String, CategoryCreditProgressDto> getCreditsByCategory() { return creditsByCategory; }
    public void setCreditsByCategory(Map<String, CategoryCreditProgressDto> creditsByCategory) { this.creditsByCategory = creditsByCategory; }

    public Map<String, Integer> getCreditsByDepartment() { return creditsByDepartment; }
    public void setCreditsByDepartment(Map<String, Integer> creditsByDepartment) { this.creditsByDepartment = creditsByDepartment; }

    public List<String> getUnfulfilledRequirements() { return unfulfilledRequirements; }
    public void setUnfulfilledRequirements(List<String> unfulfilledRequirements) { this.unfulfilledRequirements = unfulfilledRequirements; }
}
