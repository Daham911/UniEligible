package lk.ac.ou.unieligible.repository;

import lk.ac.ou.unieligible.entity.GraduationRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GraduationRequirementRepository extends JpaRepository<GraduationRequirement, Long> {
    List<GraduationRequirement> findByProgrammeId(String programmeId);
}

