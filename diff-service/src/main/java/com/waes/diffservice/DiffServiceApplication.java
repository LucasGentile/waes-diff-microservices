package com.waes.diffservice;

import com.waes.diffservice.model.Diff;
import com.waes.diffservice.repository.DiffRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@EnableDiscoveryClient
@SpringBootApplication
public class DiffServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiffServiceApplication.class, args);
    }

    @Bean
    ApplicationRunner init(DiffRepository repository) {
        Diff diff1 = new Diff(1L, "ABC", "ABC");
        Diff diff2 = new Diff(2L, "AAA", "ABA");
        Diff diff3 = new Diff(3L, "Abb", "BAA");
        return args -> {
            repository.saveAll(Arrays.asList(diff1, diff2, diff3));
            repository.findAll().forEach(System.out::println);
        };
    }
}
