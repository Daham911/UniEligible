package lk.ac.ou.unieligible.repository;

import lk.ac.ou.unieligible.entity.ConcurrentRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConcurrentRegistrationRepository extends JpaRepository<ConcurrentRegistration, Long> {
    List<ConcurrentRegistration> findByStudentIdAndActiveTrue(String studentId);
    Optional<ConcurrentRegistration> findByStudentIdAndCourseCodeAndAcademicSession(String studentId, String courseCode, String academicSession);
}

