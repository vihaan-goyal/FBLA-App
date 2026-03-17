package quest;

import java.util.ArrayList;
import main.GamePanel;

public class TaskManager {

    GamePanel gp;

    public boolean rewardGiven = false;

    public ArrayList<Task> tasks = new ArrayList<>();

    public TaskManager(GamePanel gp) {
        this.gp = gp;
    }

    public void addTask(Task t) {
        tasks.add(t);
    }

   public void update() {

        for(Task t : tasks) {

            if(!t.completed) {

                if(t.checkCompletion(gp.player.worldX, gp.player.worldY)) {

                    t.completed = true;

                    gp.ui.showMessage("Task Completed: " + t.name);

                }

            }

        }
    }

    public boolean allCompleted() {

        for(Task t : tasks) {
            if(!t.completed) return false;
        }

        return true;
    }

}