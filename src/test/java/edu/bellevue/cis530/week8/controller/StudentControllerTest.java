package edu.bellevue.cis530.week8.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.bellevue.cis530.week8.entity.Student;
import edu.bellevue.cis530.week8.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @Test
    void createStudent_shouldReturnCreatedResponse() throws Exception {
        StudentResponse response = new StudentResponse(
                1L,
                "Alice",
                "Jones",
                "alice@example.com",
                "CS",
                new BigDecimal("3.90"),
                2024,
                new StudentResponse.Profile(
                        101L,
                        "5551234567",
                        "123 Main St",
                        "Seattle",
                        "WA",
                        "98101",
                        "Parent",
                        "5550001111"
                )
        );

        when(studentService.createStudent(any(Student.class))).thenReturn(response);

        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("firstName", "Alice");
        payload.put("lastName", "Jones");
        payload.put("email", "alice@example.com");
        payload.put("major", "CS");
        payload.put("gpa", "3.90");
        payload.put("enrollmentYear", 2024);

        ObjectNode profile = objectMapper.createObjectNode();
        profile.put("phone", "5551234567");
        profile.put("address", "123 Main St");
        profile.put("city", "Seattle");
        profile.put("state", "WA");
        profile.put("zip", "98101");
        profile.put("emergencyContactName", "Parent");
        profile.put("emergencyContactPhone", "5550001111");
        payload.set("profile", profile);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.major").value("CS"));

        verify(studentService).createStudent(any(Student.class));
    }

    @Test
    void getAllStudents_shouldReturnOkResponseWithList() throws Exception {
        StudentResponse first = new StudentResponse(
                1L,
                "Alice",
                "Jones",
                "alice@example.com",
                "CS",
                new BigDecimal("3.90"),
                2024,
                new StudentResponse.Profile(101L, "5551234567", "123 Main St", "Seattle", "WA", "98101", "Parent", "5550001111"));
        StudentResponse second = new StudentResponse(
                2L,
                "Bob",
                "Smith",
                "bob@example.com",
                "Math",
                new BigDecimal("3.75"),
                2023,
                new StudentResponse.Profile(102L, "5559876543", "456 Oak Ave", "Seattle", "WA", "98101", "Guardian", "5550002222"));

        when(studentService.getAllStudents()).thenReturn(List.of(first, second));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("alice@example.com"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].email").value("bob@example.com"));

        verify(studentService).getAllStudents();
    }

    @Test
    void getStudentById_shouldReturnOkResponse() throws Exception {
        StudentResponse response = new StudentResponse(
                7L,
                "Charlie",
                "Brown",
                "charlie@example.com",
                "Biology",
                new BigDecimal("3.80"),
                2022,
                new StudentResponse.Profile(107L, "5551112222", "789 Pine Rd", "Seattle", "WA", "98101", "Parent", "5550003333"));

        when(studentService.getStudentById(7L)).thenReturn(response);

        mockMvc.perform(get("/api/students/{id}", 7L))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.firstName").value("Charlie"))
                .andExpect(jsonPath("$.email").value("charlie@example.com"));

        verify(studentService).getStudentById(7L);
    }

    @Test
    void deleteStudent_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/students/{id}", 10L))
                .andExpect(status().isNoContent());

        verify(studentService).deleteStudent(10L);
    }
}
