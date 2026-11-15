package lk.ac.ou.unieligible.controller;

import lk.ac.ou.unieligible.entity.AcademicYear;
import lk.ac.ou.unieligible.repository.AcademicYearRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/academic-years")
@CrossOrigin(origins = "*")
public class AcademicYearController {

    private final AcademicYearRepository academicYearRepository;

    public AcademicYearController(AcademicYearRepository academicYearRepository) {
        this.academicYearRepository = academicYearRepository;
    }

    @GetMapping
    public ResponseEntity<List<AcademicYear>> getAcademicYears() {
        return ResponseEntity.ok(academicYearRepository.findAllByOrderByYearNumberAsc());
    }
}

