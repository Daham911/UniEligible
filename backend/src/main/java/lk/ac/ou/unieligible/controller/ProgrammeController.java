package lk.ac.ou.unieligible.controller;

import lk.ac.ou.unieligible.entity.Programme;
import lk.ac.ou.unieligible.repository.ProgrammeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/programmes")
@CrossOrigin(origins = "*")
public class ProgrammeController {

    private final ProgrammeRepository programmeRepository;

    public ProgrammeController(ProgrammeRepository programmeRepository) {
        this.programmeRepository = programmeRepository;
    }

    @GetMapping
    public ResponseEntity<List<Programme>> getProgrammes() {
        return ResponseEntity.ok(programmeRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Programme> getProgrammeById(@PathVariable String id) {
        return programmeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

