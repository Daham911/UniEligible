package lk.ac.ou.unieligible.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraduationAuditDto {
    private String studentId;
    private String studentName;
    private String programmeName;
    private Integer targetTotalCredits = 125;
    private Integer totalRecognizedCredits = 0;
    private Boolean graduationRequirementsSatisfied = false;
    private String statusMessage;

    private List<GraduationRequirementCheckDto> requirements = new ArrayList<>();
    private List<CategoryCreditProgressDto> categoryRequirements = new ArrayList<>();
    private Map<String, Integer> levelCredits = new HashMap<>();
    private List<String> outstandingConditions = new ArrayList<>();

    public GraduationAuditDto() {}

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getProgrammeName() { return programmeName; }
    public void setProgrammeName(String programmeName) { this.programmeName = programmeName; }

    public Integer getTargetTotalCredits() { return targetTotalCredits; }
    public void setTargetTotalCredits(Integer targetTotalCredits) { this.targetTotalCredits = targetTotalCredits; }

    public Integer getTotalRecognizedCredits() { return totalRecognizedCredits; }
    public void setTotalRecognizedCredits(Integer totalRecognizedCredits) { this.totalRecognizedCredits = totalRecognizedCredits; }

    public Boolean getGraduationRequirementsSatisfied() { return graduationRequirementsSatisfied; }
    public void setGraduationRequirementsSatisfied(Boolean graduationRequirementsSatisfied) { this.graduationRequirementsSatisfied = graduationRequirementsSatisfied; }

    public String getStatusMessage() { return statusMessage; }
    public void setStatusMessage(String statusMessage) { this.statusMessage = statusMessage; }

    public List<GraduationRequirementCheckDto> getRequirements() { return requirements; }
    public void setRequirements(List<GraduationRequirementCheckDto> requirements) { this.requirements = requirements; }

    public List<CategoryCreditProgressDto> getCategoryRequirements() { return categoryRequirements; }
    public void setCategoryRequirements(List<CategoryCreditProgressDto> categoryRequirements) { this.categoryRequirements = categoryRequirements; }

    public Map<String, Integer> getLevelCredits() { return levelCredits; }
    public void setLevelCredits(Map<String, Integer> levelCredits) { this.levelCredits = levelCredits; }

    public List<String> getOutstandingConditions() { return outstandingConditions; }
    public void setOutstandingConditions(List<String> outstandingConditions) { this.outstandingConditions = outstandingConditions; }
}

