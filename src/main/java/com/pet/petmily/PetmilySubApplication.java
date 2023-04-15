package com.pet.petmily;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PetmilySubApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetmilySubApplication.class, args);
    }

}
