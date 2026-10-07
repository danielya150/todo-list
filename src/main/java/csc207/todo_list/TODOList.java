package csc207.todo_list;

import java.util.ArrayList;
import java.util.Collections;

public class TODOList {
  private ArrayList<Task> tasks;
  public TODOList() {
    tasks = new ArrayList<>();
  }
  public void addTask(Task task) {
    tasks.add(task);
  }
  public ArrayList<Task> getTasks() {
    return tasks;
  }
  public void removeTask(Task task) {
    tasks.remove(task);
  }
  public void sortTasks(int ver) {
    tasks.sort(new TaskComparer(ver));
  }
}
