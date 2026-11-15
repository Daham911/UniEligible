package lk.ac.ou.unieligible.repository;

import lk.ac.ou.unieligible.entity.Programme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgrammeRepository extends JpaRepository<Programme, String> {
}

