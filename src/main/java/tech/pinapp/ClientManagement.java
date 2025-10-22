package tech.pinapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ClientManagement {

    public static void main(String[] args) {
        SpringApplication.run(ClientManagement.class, args);
    }
}