package lk.ac.ou.unieligible.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "programmes")
public class Programme {

    @Id
    @Column(name = "id", length = 20)
    private String id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "faculty", length = 100)
    private String faculty;

    @Column(name = "min_total_credits")
    private Integer minTotalCredits = 125;

    @Column(name = "max_annual_credits")
    private Integer maxAnnualCredits = 38;

    @Column(name = "curriculum_version", length = 20)
    private String curriculumVersion = "2026/2027";

    public Programme() {}

    public Programme(String id, String name, String faculty, Integer minTotalCredits, Integer maxAnnualCredits, String curriculumVersion) {
        this.id = id;
        this.name = name;
        this.faculty = faculty;
        this.minTotalCredits = minTotalCredits;
        this.maxAnnualCredits = maxAnnualCredits;
        this.curriculumVersion = curriculumVersion;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFaculty() { return faculty; }
    public void setFaculty(String faculty) { this.faculty = faculty; }

    public Integer getMinTotalCredits() { return minTotalCredits; }
    public void setMinTotalCredits(Integer minTotalCredits) { this.minTotalCredits = minTotalCredits; }

    public Integer getMaxAnnualCredits() { return maxAnnualCredits; }
    public void setMaxAnnualCredits(Integer maxAnnualCredits) { this.maxAnnualCredits = maxAnnualCredits; }

    public String getCurriculumVersion() { return curriculumVersion; }
    public void setCurriculumVersion(String curriculumVersion) { this.curriculumVersion = curriculumVersion; }
}

