package com.zerobase.healbits;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HealbitsApplicationTests {
    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "classpath:application-aws.yml";

    @Test
    void contextLoads() {
        new SpringApplicationBuilder(HealbitsApplication.class)
                .properties(APPLICATION_LOCATIONS);
    }

}
