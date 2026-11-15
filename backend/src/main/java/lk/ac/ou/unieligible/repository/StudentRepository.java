package lk.ac.ou.unieligible.repository;

import lk.ac.ou.unieligible.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    Optional<Student> findByRegistrationNumber(String registrationNumber);
}

