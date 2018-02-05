package com.springui;

import com.springui.thymeleaf.ComponentDialect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author Stephan Grundner
 */
@SpringBootApplication
public class DemoApplication {

    @Bean
    protected ComponentDialect componentDialect() {
        ComponentDialect dialect = new ComponentDialect();

        return dialect;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(DemoApplication.class, args);
        assert context.isRunning();
    }
}
