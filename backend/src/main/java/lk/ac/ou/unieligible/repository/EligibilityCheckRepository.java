package lk.ac.ou.unieligible.repository;

import lk.ac.ou.unieligible.entity.EligibilityCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EligibilityCheckRepository extends JpaRepository<EligibilityCheck, Long> {
    List<EligibilityCheck> findByStudentId(String studentId);
    Optional<EligibilityCheck> findByStudentIdAndCourseCode(String studentId, String courseCode);
    void deleteByStudentId(String studentId);
}

