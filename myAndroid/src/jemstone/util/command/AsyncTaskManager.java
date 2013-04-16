package jemstone.util.command;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncTaskManager {
  private static ExecutorService executor = Executors.newSingleThreadExecutor();
  
  public static void execute(Runnable task) {
    executor.execute(task);
//    task.run();
  }
}
