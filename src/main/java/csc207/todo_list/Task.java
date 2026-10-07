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

  public void edit(String name) {
    this.name=name;
  }

  @Override
  public String toString(){
    int Y = dueDate/10000;
    int M = dueDate%10000 /100;
    int D = dueDate%100;
    String str = name + " Y: " + Y + " M: "
    + M + " D: " + D + " Priority:" + priority;
    if(done){
      str = str + " (done)";
      return str;
    }
    return str;
  }
}