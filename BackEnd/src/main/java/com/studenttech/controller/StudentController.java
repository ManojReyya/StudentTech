package com.studenttech.controller;

import com.studenttech.dto.StudentDTO;
import com.studenttech.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/students")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Student Management", description = "APIs for managing students and their tech stacks")
public class StudentController {

    private final StudentService studentService;

    /**
     * Get all students
     */
    @GetMapping
    @Operation(summary = "Get all students", description = "Retrieve a list of all students")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all students")
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        log.info("GET /v1/students - Fetching all students");
        List<StudentDTO> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    /**
     * Get student by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID", description = "Retrieve a specific student by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student found"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<StudentDTO> getStudentById(
            @Parameter(description = "Student ID") @PathVariable Long id) {
        log.info("GET /v1/students/{} - Fetching student by ID", id);
        StudentDTO student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    /**
     * Get student by name
     */
    @GetMapping("/name/{name}")
    @Operation(summary = "Get student by name", description = "Retrieve a specific student by their name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student found"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<StudentDTO> getStudentByName(
            @Parameter(description = "Student name") @PathVariable String name) {
        log.info("GET /v1/students/name/{} - Fetching student by name", name);
        StudentDTO student = studentService.getStudentByName(name);
        return ResponseEntity.ok(student);
    }

    /**
     * Create new student
     */
    @PostMapping
    @Operation(summary = "Create a new student", description = "Add a new student to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<StudentDTO> createStudent(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Student data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = StudentDTO.class))
            )
            @RequestBody StudentDTO studentDTO) {
        log.info("POST /v1/students - Creating new student: {}", studentDTO.getName());
        StudentDTO createdStudent = studentService.createStudent(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    /**
     * Update existing student
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a student", description = "Update an existing student's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student updated successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<StudentDTO> updateStudent(
            @Parameter(description = "Student ID") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated student data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = StudentDTO.class))
            )
            @RequestBody StudentDTO studentDTO) {
        log.info("PUT /v1/students/{} - Updating student", id);
        StudentDTO updatedStudent = studentService.updateStudent(id, studentDTO);
        return ResponseEntity.ok(updatedStudent);
    }

    /**
     * Delete student
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a student", description = "Remove a student from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Student deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<Void> deleteStudent(
            @Parameter(description = "Student ID") @PathVariable Long id) {
        log.info("DELETE /v1/students/{} - Deleting student", id);
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search students by name
     */
    @GetMapping("/search/name")
    @Operation(summary = "Search students by name", description = "Search for students by name (partial match)")
    @ApiResponse(responseCode = "200", description = "Search results returned")
    public ResponseEntity<List<StudentDTO>> searchStudentsByName(
            @Parameter(description = "Search keyword") @RequestParam String keyword) {
        log.info("GET /v1/students/search/name - Searching by name: {}", keyword);
        List<StudentDTO> results = studentService.searchStudentsByName(keyword);
        return ResponseEntity.ok(results);
    }

    /**
     * Search students by keyword
     */
    @GetMapping("/search")
    @Operation(summary = "Search students", description = "Search for students by name or tech stack")
    @ApiResponse(responseCode = "200", description = "Search results returned")
    public ResponseEntity<List<StudentDTO>> searchStudents(
            @Parameter(description = "Search keyword") @RequestParam String keyword) {
        log.info("GET /v1/students/search - Searching by keyword: {}", keyword);
        List<StudentDTO> results = studentService.searchStudents(keyword);
        return ResponseEntity.ok(results);
    }

    /**
     * Get all unique tech stacks
     */
    @GetMapping("/tech-stacks/unique")
    @Operation(summary = "Get unique tech stacks", description = "Retrieve all unique tech stacks used by students")
    @ApiResponse(responseCode = "200", description = "List of unique tech stacks")
    public ResponseEntity<List<String>> getUniqueTechStacks() {
        log.info("GET /v1/students/tech-stacks/unique - Fetching unique tech stacks");
        List<String> techStacks = studentService.getAllUniqueTechStacks();
        return ResponseEntity.ok(techStacks);
    }

    /**
     * Filter students by tech stacks
     */
    @PostMapping("/filter/tech-stacks")
    @Operation(summary = "Filter students by tech stacks", description = "Get students that have all specified tech stacks")
    @ApiResponse(responseCode = "200", description = "Filtered students list")
    public ResponseEntity<List<StudentDTO>> filterByTechStacks(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "List of tech stacks to filter by",
                    required = true,
                    content = @Content(schema = @Schema(implementation = List.class))
            )
            @RequestBody List<String> techStacks) {
        log.info("POST /v1/students/filter/tech-stacks - Filtering by tech stacks: {}", techStacks);
        List<StudentDTO> results = studentService.filterStudentsByTechStacks(techStacks);
        return ResponseEntity.ok(results);
    }

    /**
     * Get API statistics
     */
    @GetMapping("/stats/count")
    @Operation(summary = "Get student count", description = "Get total number of students")
    @ApiResponse(responseCode = "200", description = "Total student count")
    public ResponseEntity<Map<String, Object>> getStudentCount() {
        log.info("GET /v1/students/stats/count - Getting student count");
        List<StudentDTO> students = studentService.getAllStudents();
        List<String> techStacks = studentService.getAllUniqueTechStacks();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudents", students.size());
        stats.put("uniqueTechStacks", techStacks.size());
        stats.put("techStacksList", techStacks);
        
        return ResponseEntity.ok(stats);
    }
}