package lk.ac.ou.unieligible.repository;

import lk.ac.ou.unieligible.entity.StudentExemption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentExemptionRepository extends JpaRepository<StudentExemption, Long> {
    List<StudentExemption> findByStudentId(String studentId);
}

