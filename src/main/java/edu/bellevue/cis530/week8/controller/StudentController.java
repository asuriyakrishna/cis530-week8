package edu.bellevue.cis530.week8.controller;

import edu.bellevue.cis530.week8.entity.Student;
import edu.bellevue.cis530.week8.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Students", description = "Student management endpoints")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(
            summary = "Create a student",
            description = "Creates a new student record using the provided student payload. Returns the created student."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Student created successfully",
                    content = @Content(schema = @Schema(implementation = StudentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid student request payload"),
            @ApiResponse(responseCode = "409", description = "A student with the same email already exists")
    })
    @PostMapping
    public ResponseEntity<StudentResponse> create(@Valid @RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(student));
    }

    @Operation(
            summary = "Get all students",
            description = "Retrieves a list of all students in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Students retrieved successfully",
                    content = @Content(schema = @Schema(implementation = StudentResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    @GetMapping
    public List<StudentResponse> findAll() {
        return studentService.getAllStudents();
    }

    @Operation(
            summary = "Get a student by ID",
            description = "Retrieves a single student record using the provided student ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student found",
                    content = @Content(schema = @Schema(implementation = StudentResponse.class))),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "400", description = "Invalid student ID format")
    })
    @GetMapping("/{id}")
    public StudentResponse findById(@Parameter(description = "Unique student ID", required = true) @PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PutMapping("/{id}")
    public StudentResponse update(@PathVariable Long id, @Valid @RequestBody Student student) {
        return studentService.updateStudent(id, student);
    }

    @Operation(
            summary = "Delete a student by ID",
            description = "Deletes a student record identified by the provided student ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Student deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "400", description = "Invalid student ID format")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "Unique student ID to delete", required = true) @PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/profile")
    public StudentResponse.Profile getProfile(@PathVariable Long id) {
        return studentService.getProfileByStudentId(id);
    }

    @GetMapping("/major/{major}")
    public List<StudentResponse> getStudentsByMajor(@PathVariable String major) {
        return studentService.getStudentsByMajor(major);
    }

    @GetMapping("/year/{year}")
    public List<StudentResponse> getStudentsByEnrollmentYear(@PathVariable Integer year) {
        return studentService.getStudentsByEnrollmentYear(year);
    }
}
