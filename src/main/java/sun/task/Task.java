package sun.task;

import sun.parser.DateParser;
import java.time.LocalDateTime;

public abstract class Task {
    private String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getDescription() {
        return this.description;
    }

    public String getIsDone() {

        return this.isDone ? "X" : " ";
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", this.getIsDone(), this.description);
    }

    public abstract String toFileString();

    public static Task fromFileString(String line) {
        String[] parts = line.split(" \\| ");
        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        switch (type) {
            case "T":
                Todo todo = new Todo(description);
                todo.setIsDone(isDone);;
                return todo;
            case "D":
                String byString = parts[3];
                LocalDateTime byDateTime = DateParser.parse(byString);
                Deadline deadline = new Deadline(description, byDateTime);
                deadline.setIsDone(isDone);
                return deadline;
            case "E":
                String fromString = parts[3];
                String toString = parts[4];
                LocalDateTime fromDateTime = DateParser.parse(fromString);
                LocalDateTime toDateTime = DateParser.parse(toString);
                Event event = new Event(description, fromDateTime, toDateTime);
                event.setIsDone(isDone);
                return event;
            default:
                throw new IllegalArgumentException("OOPS!!! Unknown task type: " + type);
        }
    }
}
