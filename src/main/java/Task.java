import java.time.LocalDate;
import java.util.Date;

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
                LocalDate byDate = DateParser.parse(byString);
                Deadline deadline = new Deadline(description, byDate);
                deadline.setIsDone(isDone);
                return deadline;
            case "E":
                String from = parts[3];
                String to = parts[4];
                Event event = new Event(description, from, to);
                event.setIsDone(isDone);
                return event;
            default:
                throw new IllegalArgumentException("OOPS!!! Unknown task type: " + type);
        }
    }
}
