package test.task.jspider.model;

import test.task.jspider.model.config.LocalDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;
import java.util.List;

public record Match(
        String id,
        String name,
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime kickoff,
        League league,
        List<Market> markets
) {
}
