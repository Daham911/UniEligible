package lk.ac.ou.unieligible.repository;

import lk.ac.ou.unieligible.entity.PrerequisiteRuleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrerequisiteRuleGroupRepository extends JpaRepository<PrerequisiteRuleGroup, Long> {
    List<PrerequisiteRuleGroup> findByCourseCodeOrderByGroupOrderAsc(String courseCode);
}

