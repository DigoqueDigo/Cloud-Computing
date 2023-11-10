package src;

import java.io.PrintWriter;

public class Task {
    private String taskCode;
    private int requiredMemory;

    public Task(String taskCode, int requiredMemory) {
        this.taskCode = taskCode;
        this.requiredMemory = requiredMemory;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public int getRequiredMemory() {
        return requiredMemory;
    }


}
