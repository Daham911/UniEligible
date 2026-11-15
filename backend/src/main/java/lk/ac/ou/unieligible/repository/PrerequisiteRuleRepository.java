package lk.ac.ou.unieligible.repository;

import lk.ac.ou.unieligible.entity.PrerequisiteRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrerequisiteRuleRepository extends JpaRepository<PrerequisiteRule, Long> {
    List<PrerequisiteRule> findByGroupId(Long groupId);
}

