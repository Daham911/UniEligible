package lk.ac.ou.unieligible.repository;

import lk.ac.ou.unieligible.entity.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear, Integer> {
    List<AcademicYear> findAllByOrderByYearNumberAsc();
}

