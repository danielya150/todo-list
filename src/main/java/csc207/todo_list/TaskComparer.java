package csc207.todo_list;

import java.util.Comparator;

public class TaskComparer implements Comparator<Task> {
  public static final int DATE = 1;
  public static final int PRIORITY = 2;
  public static final int NAME = 3;
  private static int vers = 0;
  @Override
  public int compare(Task o1, Task o2) {
    switch(vers){
      case 1:
        return o1.getDueDate() - o2.getDueDate();
      case 2:
        return o1.getPriority() - o2.getPriority();
      default:
        return o1.getName().compareTo(o2.getName());
    }
  }

  public static void setVers(int vers) {
    TaskComparer.vers = vers;
  }
}
