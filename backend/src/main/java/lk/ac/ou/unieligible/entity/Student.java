package lk.ac.ou.unieligible.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "registration_number", nullable = false, unique = true, length = 50)
    private String registrationNumber;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "email", length = 150)
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "programme_id", nullable = false)
    private Programme programme;

    @Column(name = "current_academic_year", nullable = false)
    private Integer currentAcademicYear = 1;

    @Column(name = "current_academic_level", nullable = false)
    private Integer currentAcademicLevel = 3;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Student() {}

    public Student(String id, String registrationNumber, String fullName, String email, Programme programme, Integer currentAcademicYear, Integer currentAcademicLevel) {
        this.id = id;
        this.registrationNumber = registrationNumber;
        this.fullName = fullName;
        this.email = email;
        this.programme = programme;
        this.currentAcademicYear = currentAcademicYear;
        this.currentAcademicLevel = currentAcademicLevel;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Programme getProgramme() { return programme; }
    public void setProgramme(Programme programme) { this.programme = programme; }

    public Integer getCurrentAcademicYear() { return currentAcademicYear; }
    public void setCurrentAcademicYear(Integer currentAcademicYear) { this.currentAcademicYear = currentAcademicYear; }

    public Integer getCurrentAcademicLevel() { return currentAcademicLevel; }
    public void setCurrentAcademicLevel(Integer currentAcademicLevel) { this.currentAcademicLevel = currentAcademicLevel; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

