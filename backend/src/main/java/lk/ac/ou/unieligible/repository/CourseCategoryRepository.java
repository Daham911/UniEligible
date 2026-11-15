package lk.ac.ou.unieligible.repository;

import lk.ac.ou.unieligible.entity.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseCategoryRepository extends JpaRepository<CourseCategory, String> {
}

