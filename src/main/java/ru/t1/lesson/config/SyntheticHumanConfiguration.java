package ru.t1.lesson.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import ru.t1.lesson.audit.AuditConfiguration;

@AutoConfiguration
@ComponentScan("ru/t1/lesson")
@EnableConfigurationProperties(AuditConfiguration.class)
public class SyntheticHumanConfiguration { }
