package ru.t1.lesson.audit;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@Aspect
@AllArgsConstructor
public class WeylandWatchingYouAspect {
    private KafkaTemplate<String,String> kafkaTemplate;
    private AuditConfiguration auditConfiguration;

    @Around("@annotation(ru.t1.lesson.audit.WeylandWatchingYou)")
    public Object WeylandWatchingYouAdvise(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        Object proceed = joinPoint.proceed();

        String str = "Метод: " + signature
                + ", вызван с аргументами: "
                + args.toString()
                + ", результат выполнения: " + proceed;

        if(AuditMode.CONSOLE.equals(auditConfiguration.getMode())) {
            kafkaTemplate.send("audit", str);
        } else {
            log.info("Метод {} вызван с аргументами: {} , результат выполнения: {}"
                    ,signature.getName()
                    ,args
                    ,proceed);
        }

        return proceed;
    }
}
