package lk.ac.ou.unieligible.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "academic_years")
public class AcademicYear {

    @Id
    @Column(name = "year_number")
    private Integer yearNumber;

    @Column(name = "level_number", nullable = false)
    private Integer levelNumber;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    public AcademicYear() {}

    public AcademicYear(Integer yearNumber, Integer levelNumber, String title, String description) {
        this.yearNumber = yearNumber;
        this.levelNumber = levelNumber;
        this.title = title;
        this.description = description;
    }

    public Integer getYearNumber() { return yearNumber; }
    public void setYearNumber(Integer yearNumber) { this.yearNumber = yearNumber; }

    public Integer getLevelNumber() { return levelNumber; }
    public void setLevelNumber(Integer levelNumber) { this.levelNumber = levelNumber; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

