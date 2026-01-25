public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return String.format("[T]%s", super.toString());
    }

    @Override
    public String toFileString() {
        return String.format("T | %d | %s", this.getIsDone().equals("X") ? 1 : 0, this.getDescription());
    }
}
