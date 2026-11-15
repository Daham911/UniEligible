package lk.ac.ou.unieligible.service;

import lk.ac.ou.unieligible.dto.CourseDto;
import lk.ac.ou.unieligible.entity.Course;
import lk.ac.ou.unieligible.exception.ResourceNotFoundException;
import lk.ac.ou.unieligible.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional(readOnly = true)
    public List<CourseDto> getCoursesByYear(Integer year) {
        List<Course> courses;
        if (year != null && year >= 1 && year <= 4) {
            courses = courseRepository.findByAcademicYear(year);
        } else {
            courses = courseRepository.findAllByOrderByAcademicYearAscSemesterAscCodeAsc();
        }
        return courses.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CourseDto getCourseByCode(String code) {
        Course course = courseRepository.findById(code)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with code: " + code));
        return toDto(course);
    }

    @Transactional(readOnly = true)
    public Course getCourseEntity(String code) {
        return courseRepository.findById(code)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with code: " + code));
    }

    public CourseDto toDto(Course course) {
        CourseDto dto = new CourseDto();
        dto.setCode(course.getCode());
        dto.setName(course.getName());
        if (course.getDepartment() != null) {
            dto.setDepartmentCode(course.getDepartment().getCode());
            dto.setDepartmentName(course.getDepartment().getName());
        }
        if (course.getCategory() != null) {
            dto.setCategoryCode(course.getCategory().getCode());
            dto.setCategoryName(course.getCategory().getName());
        }
        dto.setLevel(course.getLevel());
        dto.setAcademicYear(course.getAcademicYear());
        dto.setCredits(course.getCredits());
        dto.setCreditRatingNote(course.getCreditRatingNote());
        dto.setSemester(course.getSemester());
        dto.setCompulsory(course.getCompulsory());
        dto.setDegreeCredit(course.getDegreeCredit());
        dto.setGpaContributing(course.getGpaContributing());
        dto.setVirtual(course.getVirtual());
        dto.setPrerequisiteDisplayText(course.getPrerequisiteDisplayText());
        dto.setRemarks(course.getRemarks());
        dto.setOffered(course.getOffered());
        dto.setDeliveryCenter(course.getDeliveryCenter());
        dto.setSpecialRestriction(course.getSpecialRestriction());
        dto.setDiscrepancyFlag(course.getDiscrepancyFlag());
        return dto;
    }
}

