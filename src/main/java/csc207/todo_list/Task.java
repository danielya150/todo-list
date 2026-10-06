package csc207.todo_list;

public class Task {
  private String name;
  private int priority;
  private boolean done;
  private int dueDate;

  public Task(String name, int priority, boolean done, int dueDate) {
    this.name = name;
    this.priority = priority;
    this.done = done;
    this.dueDate = dueDate;
  }

  public Task(String name, int priority, int dueDate) {
    this(name, priority, false, dueDate);
  }

  public void toggleDone() {
    this.done = !done;
  }

  public int getDueDate() {
    return dueDate;
  }

  public String getName() {
    return name;
  }

  public boolean isDone() {
    return done;
  }

  public int getPriority() {
    return priority;
  }

  public void edit(String name,  int priority, boolean done, int dueDate) {
    this.name=name;
    this.priority=priority;
    this.done=done;
    this.dueDate=dueDate;
  }

  @Override
  public String toString(){
    return name;
  }
}