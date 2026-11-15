package lk.ac.ou.unieligible.controller;

import lk.ac.ou.unieligible.dto.CourseDto;
import lk.ac.ou.unieligible.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<CourseDto>> getCourses(
            @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(courseService.getCoursesByYear(year));
    }

    @GetMapping("/{courseCode}")
    public ResponseEntity<CourseDto> getCourseByCode(@PathVariable String courseCode) {
        return ResponseEntity.ok(courseService.getCourseByCode(courseCode));
    }
}

