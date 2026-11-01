package edu.bellevue.cis530.week8.service;

import edu.bellevue.cis530.week8.controller.StudentResponse;
import edu.bellevue.cis530.week8.entity.Student;
import edu.bellevue.cis530.week8.entity.StudentProfile;
import edu.bellevue.cis530.week8.repository.StudentRepository;
import edu.bellevue.cis530.week8.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void createStudent_shouldSaveAndReturnStudentResponse() {
        Student student = buildStudent(1L, "Alice", "Jones", "alice@example.com", "CS",
                new BigDecimal("3.90"), 2024, "5551234567", "123 Main St");

        when(studentRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(studentRepository.save(student)).thenReturn(student);

        StudentResponse result = studentService.createStudent(student);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Alice", result.firstName());
        assertEquals("alice@example.com", result.email());
        assertEquals("CS", result.major());
        verify(studentRepository).existsByEmail("alice@example.com");
        verify(studentRepository).save(student);
    }

    @Test
    void getAllStudents_shouldReturnAllStudents() {
        Student first = buildStudent(1L, "Alice", "Jones", "alice@example.com", "CS",
                new BigDecimal("3.90"), 2024, "5551234567", "123 Main St");
        Student second = buildStudent(2L, "Bob", "Smith", "bob@example.com", "Math",
                new BigDecimal("3.75"), 2023, "5559876543", "456 Oak Ave");

        when(studentRepository.findAll()).thenReturn(List.of(first, second));

        List<StudentResponse> result = studentService.getAllStudents();

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).firstName());
        assertEquals("Bob", result.get(1).firstName());
        verify(studentRepository).findAll();
    }

    @Test
    void getStudentById_shouldReturnStudentResponse() {
        Student student = buildStudent(7L, "Charlie", "Brown", "charlie@example.com", "Biology",
                new BigDecimal("3.80"), 2022, "5551112222", "789 Pine Rd");

        when(studentRepository.findById(7L)).thenReturn(Optional.of(student));

        StudentResponse result = studentService.getStudentById(7L);

        assertNotNull(result);
        assertEquals(7L, result.id());
        assertEquals("Charlie", result.firstName());
        assertEquals("charlie@example.com", result.email());
        assertEquals("Biology", result.major());
        verify(studentRepository).findById(7L);
    }

    @Test
    void deleteStudent_shouldDeleteStudentById() {
        Student student = buildStudent(10L, "Dana", "White", "dana@example.com", "History",
                new BigDecimal("3.60"), 2021, "5554443333", "12 Cedar Ln");

        when(studentRepository.findById(10L)).thenReturn(Optional.of(student));

        studentService.deleteStudent(10L);

        verify(studentRepository).findById(10L);
        verify(studentRepository).delete(student);
    }

    private Student buildStudent(Long id, String firstName, String lastName, String email, String major,
                               BigDecimal gpa, Integer enrollmentYear, String phone, String address) {
        Student student = new Student();
        student.setId(id);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setMajor(major);
        student.setGpa(gpa);
        student.setEnrollmentYear(enrollmentYear);

        StudentProfile profile = new StudentProfile();
        profile.setId(id + 100L);
        profile.setPhone(phone);
        profile.setAddress(address);
        profile.setCity("Seattle");
        profile.setState("WA");
        profile.setZip("98101");
        profile.setEmergencyContactName("Parent");
        profile.setEmergencyContactPhone("5550001111");
        student.setProfile(profile);
        return student;
    }
}
