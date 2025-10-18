package com.studenttech.service;

import com.studenttech.dto.StudentDTO;
import com.studenttech.entity.Student;
import com.studenttech.exception.ResourceNotFoundException;
import com.studenttech.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;

    /**
     * Get all students
     */
    public List<StudentDTO> getAllStudents() {
        log.info("Fetching all students");
        return studentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get student by ID
     */
    public StudentDTO getStudentById(Long id) {
        log.info("Fetching student with ID: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        return convertToDTO(student);
    }

    /**
     * Get student by name
     */
    public StudentDTO getStudentByName(String name) {
        log.info("Fetching student with name: {}", name);
        Student student = studentRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with name: " + name));
        return convertToDTO(student);
    }

    /**
     * Create new student
     */
    public StudentDTO createStudent(StudentDTO studentDTO) {
        log.info("Creating new student with name: {}", studentDTO.getName());
        
        if (studentDTO.getName() == null || studentDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty");
        }
        
        if (studentDTO.getTechStacks() == null || studentDTO.getTechStacks().isEmpty()) {
            throw new IllegalArgumentException("At least one tech stack must be selected");
        }

        Student student = Student.builder()
                .name(studentDTO.getName().trim())
                .build();
        
        student.setTechStacksFromArray(studentDTO.getTechStacks().toArray(new String[0]));
        
        Student savedStudent = studentRepository.save(student);
        log.info("Student created successfully with ID: {}", savedStudent.getId());
        
        return convertToDTO(savedStudent);
    }

    /**
     * Update existing student
     */
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        log.info("Updating student with ID: {}", id);
        
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        if (studentDTO.getName() != null && !studentDTO.getName().trim().isEmpty()) {
            student.setName(studentDTO.getName().trim());
        }

        if (studentDTO.getTechStacks() != null && !studentDTO.getTechStacks().isEmpty()) {
            student.setTechStacksFromArray(studentDTO.getTechStacks().toArray(new String[0]));
        }

        Student updatedStudent = studentRepository.save(student);
        log.info("Student updated successfully with ID: {}", id);
        
        return convertToDTO(updatedStudent);
    }

    /**
     * Delete student
     */
    public void deleteStudent(Long id) {
        log.info("Deleting student with ID: {}", id);
        
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        
        studentRepository.delete(student);
        log.info("Student deleted successfully with ID: {}", id);
    }

    /**
     * Search students by name
     */
    public List<StudentDTO> searchStudentsByName(String name) {
        log.info("Searching students by name: {}", name);
        return studentRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Search students by keyword
     */
    public List<StudentDTO> searchStudents(String keyword) {
        log.info("Searching students by keyword: {}", keyword);
        return studentRepository.searchStudents(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all unique tech stacks
     */
    public List<String> getAllUniqueTechStacks() {
        log.info("Fetching all unique tech stacks");
        return studentRepository.findAll().stream()
                .flatMap(student -> List.of(student.getTechStacksArray()).stream())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Filter students by tech stacks
     */
    public List<StudentDTO> filterStudentsByTechStacks(List<String> techStacks) {
        log.info("Filtering students by tech stacks: {}", techStacks);
        
        if (techStacks == null || techStacks.isEmpty()) {
            return getAllStudents();
        }

        return studentRepository.findAll().stream()
                .filter(student -> {
                    String[] studentTechs = student.getTechStacksArray();
                    return techStacks.stream()
                            .allMatch(tech -> List.of(studentTechs).contains(tech));
                })
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert Student entity to StudentDTO
     */
    private StudentDTO convertToDTO(Student student) {
        return StudentDTO.builder()
                .id(student.getId())
                .name(student.getName())
                .techStacks(List.of(student.getTechStacksArray()))
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}