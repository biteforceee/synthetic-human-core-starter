package ru.t1.lesson.command;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
public class Command {
    @Size(max = 1000, message = "Длина описания должна быть меньше 1000 символов")
    private String description;
    private CommandPriority priority;
    @Size(max = 100, message = "Длина имени автора должна быть меньше 100 символов")
    private String author;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String time;
}

