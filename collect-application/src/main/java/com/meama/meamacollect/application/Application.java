package com.meama.meamacollect.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {
        "com.meama.meamacollect.application",
        "com.meama.filesystem",
        "com.meama.common",
        "com.meama.atom",
        "com.meama.security"})
@EnableJpaRepositories(basePackages = {"com.meama.security", "com.meama.atom"})
@EntityScan(basePackages = {"com.meama.security", "com.meama.atom"})
@EnableTransactionManagement
public class Application extends SpringBootServletInitializer {

    //for war extension
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    //for jar extension
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
