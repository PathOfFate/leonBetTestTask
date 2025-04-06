package test.task.jspider.model;

import java.util.List;

public record Market(
        String id,
        String name,
        List<Runner> runners
) {
}
