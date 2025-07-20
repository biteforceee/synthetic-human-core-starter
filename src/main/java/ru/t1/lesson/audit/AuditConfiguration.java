package ru.t1.lesson.audit;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "synthetic-human-core-starter.audit")
public class AuditConfiguration {
    private AuditMode mode = AuditMode.CONSOLE; // console или kafka
}
