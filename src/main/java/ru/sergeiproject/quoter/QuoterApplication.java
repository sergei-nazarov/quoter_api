package ru.sergeiproject.quoter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
public class QuoterApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuoterApplication.class, args);
    }

}
