package org.example.coursemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "org.example.coursemanagement.Entity")
@EnableJpaRepositories(basePackages = "org.example.coursemanagement.Repository")
public class CourseManagementAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(CourseManagementAdminApplication.class, args);
    }
}
