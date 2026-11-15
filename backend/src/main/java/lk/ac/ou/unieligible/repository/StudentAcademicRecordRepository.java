package lk.ac.ou.unieligible.repository;

import lk.ac.ou.unieligible.entity.StudentAcademicRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentAcademicRecordRepository extends JpaRepository<StudentAcademicRecord, Long> {
    List<StudentAcademicRecord> findByStudentId(String studentId);
    Optional<StudentAcademicRecord> findByStudentIdAndCourseCode(String studentId, String courseCode);
    void deleteByStudentId(String studentId);
}

