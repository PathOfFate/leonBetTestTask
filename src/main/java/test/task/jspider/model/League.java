package test.task.jspider.model;

public record League(
        String id,
        String name,
        Sport sport,
        boolean top,
        int topOrder
) {
}
