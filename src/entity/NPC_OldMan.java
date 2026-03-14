package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;
import quest.Task;

public class NPC_OldMan extends Entity {

    GamePanel gp;
    public int questStage = 0;

    public NPC_OldMan(GamePanel gp) {
        this.gp = gp;

        direction = "down";
        speed = 1;

        getNPCImage();
    }

    public void getNPCImage() {

        up1 = setup("oldman_up_1");
        up2 = setup("oldman_up_2");
        down1 = setup("oldman_down_1");
        down2 = setup("oldman_down_2");
        left1 = setup("oldman_left_1");
        left2 = setup("oldman_left_2");
        right1 = setup("oldman_right_1");
        right2 = setup("oldman_right_2");

    }

    public BufferedImage setup(String imageName) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {

            image = ImageIO.read(getClass().getResource("/npc/" + imageName + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch(IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    @Override
    public void interact(){
        gp.ui.speaker = "Wiseman";

        // Stage 0: give quest
        if(questStage == 0){

            String type = gp.petManager.currentPet.petType;
            String name = gp.petManager.currentPet.name;

            gp.wallet.addTransaction("Initial Reward", 100);
            gp.money += 100;

            gp.ui.startDialogue(new String[]{
                "Hello, fair traveler!",
                "You don't remember? You helplessly begged for a pet from your Dad.",
                "Your pet is a little " + type.toUpperCase() + ", and you named it " + name.toUpperCase() + ".",
                "With my awesome magic, I've granted you $100 to start off.",
                "If you take good care of your pet, I might even give you some more.",
                "Just talk to me after you've completed some tasks on your TODO list."
            });

            gp.taskManager.addTask(new Task(
                "Visit Park",
                "Take your pet to the park",
                50 * gp.tileSize,
                25 * gp.tileSize
            ));

            questStage = 1;
        }

        // Stage 1: waiting for completion
        else if(questStage == 1){

            if(gp.taskManager.allCompleted()){

                gp.ui.startDialogue(new String[]{
                    "Excellent work!",
                    "Here is your reward.",
                    "Now that you've learned the ropes, I'll be assigning more tasks.",
                    "Beware, there has been a sickness going around, and your pet might catch it!",
                    "Well then, get at them!"
                });


                gp.taskManager.addTask(new Task(
                    "Visit Food Store",
                    "Take your pet to the food store",
                    24 * gp.tileSize,
                    40 * gp.tileSize
                ));


                gp.wallet.addTransaction("Quest Reward", 50);
                gp.money += 50;

                questStage = 2;
            }
            else{

                gp.ui.startDialogue(new String[]{
                    "You still have tasks left.",
                    "Check your task list."
                });
            }
        }

        // Stage 2: quest already finished
        else if(questStage == 2){

            if(gp.taskManager.allCompleted()){

                gp.ui.startDialogue(new String[]{
                    "Excellent work!",
                    "You're becoming a pro at taking care of your pet!",
                    "Your Dad will sure be happy!",
                    "I've given you one last task."
                });

                gp.wallet.addTransaction("Quest Reward", 50);
                gp.money += 50;

                questStage = 3;
                
                gp.taskManager.addTask(new Task(
                    "Vaccinate Your Pet",
                    "Take your pet to the veterinarian",
                    23 * gp.tileSize,
                    25 * gp.tileSize
                ));

            }
            else{

                gp.ui.startDialogue(new String[]{
                    "You still have tasks left.",
                    "Check your task list."
                });
            }


        }
        else if (questStage == 3){
             gp.ui.startDialogue(new String[]{
                    "You have proven to be a great pet owner.",
                    "Have fun with your pet!"
                });
        }
        else if (questStage > 3){
             gp.ui.startDialogue(new String[]{
                    "You have proven to be a great pet owner.",
                    "Have fun with your pet!"
                });
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = down1;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        g2.drawImage(image, screenX, screenY, null);

    }

}