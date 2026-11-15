package lk.ac.ou.unieligible.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ac.ou.unieligible.dto.UpdateResultRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/v1/programmes should return BSEHons")
    void testGetProgrammes() throws Exception {
        mockMvc.perform(get("/api/v1/programmes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].id", is("BSEHONS")));
    }

    @Test
    @DisplayName("GET /api/v1/courses?year=1 should return Year 1 courses")
    void testGetYear1Courses() throws Exception {
        mockMvc.perform(get("/api/v1/courses").param("year", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(12))))
                .andExpect(jsonPath("$[*].code", hasItem("EEI3346")));
    }

    @Test
    @DisplayName("PUT /api/v1/students/{studentId}/results/{courseCode} should update result and trigger recalculation")
    void testUpdateCourseResult() throws Exception {
        UpdateResultRequest req = new UpdateResultRequest("PA", "2026/2027");

        mockMvc.perform(put("/api/v1/students/OUSL-SE-2026-001/results/EEI3346")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseCode", is("EEI3346")))
                .andExpect(jsonPath("$.resultStatus", is("PA")));

        // Verify credit progress updated
        mockMvc.perform(get("/api/v1/students/OUSL-SE-2026-001/progress"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalEarnedCredits", greaterThanOrEqualTo(3)));
    }

    @Test
    @DisplayName("GET /actuator/health should return UP status")
    void testActuatorHealth() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")));
    }
}

