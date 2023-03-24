package com.zerobase.healbits;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class HealbitsApplication {
    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "classpath:application-aws.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(HealbitsApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }

}
