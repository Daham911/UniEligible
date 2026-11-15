package lk.ac.ou.unieligible.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "course_categories")
public class CourseCategory {

    @Id
    @Column(name = "code", length = 5)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "min_credits")
    private Integer minCredits;

    @Column(name = "max_credits")
    private Integer maxCredits;

    public CourseCategory() {}

    public CourseCategory(String code, String name, Integer minCredits, Integer maxCredits) {
        this.code = code;
        this.name = name;
        this.minCredits = minCredits;
        this.maxCredits = maxCredits;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getMinCredits() { return minCredits; }
    public void setMinCredits(Integer minCredits) { this.minCredits = minCredits; }

    public Integer getMaxCredits() { return maxCredits; }
    public void setMaxCredits(Integer maxCredits) { this.maxCredits = maxCredits; }
}

