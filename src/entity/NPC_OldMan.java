package entity;

import main.GamePanel;
import quest.Task;

public class NPC_OldMan extends NPC {

    public int questStage = 0;

    public NPC_OldMan(GamePanel gp) {
        super(gp, "Wiseman");
        down1 = loadSprite("/npc/oldman_down_1.png", gp.tileSize, gp.tileSize);
    }

    @Override
    public void interact() {

        String type = gp.petManager.currentPet.petType;
        String name = gp.petManager.currentPet.name;

        if (questStage == 0) {
            gp.wallet.earn("Initial Reward", 100);
            gp.taskManager.addTask(new Task(
                "Visit Park", "Take your pet to the park",
                50 * gp.tileSize, 25 * gp.tileSize));
            say(
                "Hello, fair traveler!",
                "You don't remember? You helplessly begged for a pet from your Dad.",
                "Your pet is a little " + type.toUpperCase() + ", and you named it " + name.toUpperCase() + ".",
                "With my awesome magic, I've granted you $100 to start off.",
                "If you take good care of your pet, I might even give you some more.",
                "Just talk to me after you've completed some tasks on your TODO list."
            );
            questStage = 1;

        } else if (questStage == 1) {
            if (gp.taskManager.allCompleted()) {
                gp.wallet.earn("Quest Reward", 50);
                gp.taskManager.addTask(new Task(
                    "Visit Food Store", "Take your pet to the food store",
                    24 * gp.tileSize, 40 * gp.tileSize));
                say(
                    "Excellent work!",
                    "Here is your reward.",
                    "Now that you've learned the ropes, I'll be assigning more tasks.",
                    "Beware, there has been a sickness going around, and your pet might catch it!",
                    "Well then, get at them!"
                );
                questStage = 2;
            } else {
                say("You still have tasks left.", "Check your task list.");
            }

        } else if (questStage == 2) {
            if (gp.taskManager.allCompleted()) {
                gp.wallet.earn("Quest Reward", 50);
                gp.taskManager.addTask(new Task(
                    "Visit the Vet", "Take your pet to the veterinarian",
                    35 * gp.tileSize, 45 * gp.tileSize));
                gp.taskManager.addTask(new Task(
                    "Visit the Toy Shop", "Buy a toy for your pet",
                    45 * gp.tileSize, 41 * gp.tileSize));
                gp.taskManager.addTask(new Task(
                    "Visit Your Home", "Take your pet to your house",
                    35 * gp.tileSize, 15 * gp.tileSize));
                say(
                    "Excellent work!",
                    "You're becoming a pro at taking care of your pet!",
                    "Your Dad will sure be happy!",
                    "Along with the reward, I've given you 3 more tasks."
                );
                questStage = 3;
            } else {
                say("You still have tasks left.", "Check your task list.");
            }

        } else if (questStage == 3) {
            if (gp.taskManager.allCompleted()) {
                gp.wallet.earn("Final Reward", 100);
                say("You have proven to be a great pet owner.", "Have fun with your pet!");
                questStage = 4;
            } else {
                say("You still have tasks left.", "Check your task list.");
            }

        } else {
            say("You have proven to be a great pet owner.", "Have fun with your pet!");
        }
    }
}
