package lk.ac.ou.unieligible.dto;

public class StudentDto {
    private String id;
    private String registrationNumber;
    private String fullName;
    private String email;
    private String programmeId;
    private String programmeName;
    private Integer currentAcademicYear;
    private Integer currentAcademicLevel;

    public StudentDto() {}

    public StudentDto(String id, String registrationNumber, String fullName, String email,
                      String programmeId, String programmeName, Integer currentAcademicYear, Integer currentAcademicLevel) {
        this.id = id;
        this.registrationNumber = registrationNumber;
        this.fullName = fullName;
        this.email = email;
        this.programmeId = programmeId;
        this.programmeName = programmeName;
        this.currentAcademicYear = currentAcademicYear;
        this.currentAcademicLevel = currentAcademicLevel;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProgrammeId() { return programmeId; }
    public void setProgrammeId(String programmeId) { this.programmeId = programmeId; }

    public String getProgrammeName() { return programmeName; }
    public void setProgrammeName(String programmeName) { this.programmeName = programmeName; }

    public Integer getCurrentAcademicYear() { return currentAcademicYear; }
    public void setCurrentAcademicYear(Integer currentAcademicYear) { this.currentAcademicYear = currentAcademicYear; }

    public Integer getCurrentAcademicLevel() { return currentAcademicLevel; }
    public void setCurrentAcademicLevel(Integer currentAcademicLevel) { this.currentAcademicLevel = currentAcademicLevel; }
}

