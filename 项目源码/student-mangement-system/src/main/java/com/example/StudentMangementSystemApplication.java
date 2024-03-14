package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com/example/student/repository")
@SpringBootApplication
public class StudentMangementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentMangementSystemApplication.class, args);
    }

}
