package com.backend.config.flyway;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlywayInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final Flyway flyway;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            flyway.migrate();
            System.out.println("✅ Flyway migration completed after Hibernate!");
        } catch (Exception e) {
            System.err.println("❌ Flyway migration failed: " + e.getMessage());
        }
    }
}