package com.example.tefs_springboot;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.tefs_springboot.dao")
public class TEFSSpringbootApplication {
    public static void main(String[] args) {
        SpringApplication.run(TEFSSpringbootApplication.class, args);
    }
}
