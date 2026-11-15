package lk.ac.ou.unieligible.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UpdateResultRequest {

    @NotBlank(message = "Result status is required")
    @Pattern(regexp = "PA|FA|CA|NOT_ATTEMPTED", message = "Result status must be PA, FA, CA, or NOT_ATTEMPTED")
    private String resultStatus;

    private String academicYearTaken;

    public UpdateResultRequest() {}

    public UpdateResultRequest(String resultStatus, String academicYearTaken) {
        this.resultStatus = resultStatus;
        this.academicYearTaken = academicYearTaken;
    }

    public String getResultStatus() { return resultStatus; }
    public void setResultStatus(String resultStatus) { this.resultStatus = resultStatus; }

    public String getAcademicYearTaken() { return academicYearTaken; }
    public void setAcademicYearTaken(String academicYearTaken) { this.academicYearTaken = academicYearTaken; }
}

