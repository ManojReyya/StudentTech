package com.studenttech.config;

import com.studenttech.entity.Student;
import com.studenttech.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeData(StudentRepository studentRepository) {
        return args -> {
            log.info("Initializing database with sample data...");

            // Clear existing data
            studentRepository.deleteAll();

            // Add sample students
            Student student1 = Student.builder()
                    .name("Aditi")
                    .build();
            student1.setTechStacksFromArray(new String[]{"Java"});
            studentRepository.save(student1);

            Student student2 = Student.builder()
                    .name("Rohan")
                    .build();
            student2.setTechStacksFromArray(new String[]{"Python"});
            studentRepository.save(student2);

            Student student3 = Student.builder()
                    .name("Vijay")
                    .build();
            student3.setTechStacksFromArray(new String[]{"Java", "Python"});
            studentRepository.save(student3);

            Student student4 = Student.builder()
                    .name("Karthik")
                    .build();
            student4.setTechStacksFromArray(new String[]{"Python", "MSB"});
            studentRepository.save(student4);

            Student student5 = Student.builder()
                    .name("Nandhini")
                    .build();
            student5.setTechStacksFromArray(new String[]{"Java", "MSB"});
            studentRepository.save(student5);

            Student student6 = Student.builder()
                    .name("Priya")
                    .build();
            student6.setTechStacksFromArray(new String[]{"Java", "Angular", "MSB"});
            studentRepository.save(student6);

            Student student7 = Student.builder()
                    .name("Arjun")
                    .build();
            student7.setTechStacksFromArray(new String[]{"Python", "Angular"});
            studentRepository.save(student7);

            Student student8 = Student.builder()
                    .name("Neha")
                    .build();
            student8.setTechStacksFromArray(new String[]{"Java", "Python", "MSB"});
            studentRepository.save(student8);

            log.info("Database initialized with 8 sample students");
        };
    }
}