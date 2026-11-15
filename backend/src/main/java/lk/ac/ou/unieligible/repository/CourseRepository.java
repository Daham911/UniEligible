package lk.ac.ou.unieligible.repository;

import lk.ac.ou.unieligible.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    List<Course> findByAcademicYear(Integer academicYear);
    List<Course> findByLevel(Integer level);
    List<Course> findByAcademicYearAndCompulsory(Integer academicYear, Boolean compulsory);
    List<Course> findAllByOrderByAcademicYearAscSemesterAscCodeAsc();

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.ruleGroups rg LEFT JOIN FETCH rg.rules WHERE c.academicYear = :academicYear")
    List<Course> findByAcademicYearWithRules(Integer academicYear);
}

