package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;
import quest.Task;

import java.awt.Color;
import java.awt.Font; 

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

        down1 = setup("oldman_down_1");
    

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
                    "Along with the reward, I've given you 3 more tasks."
                });

                gp.wallet.addTransaction("Quest Reward", 50);
                gp.money += 50;

                questStage = 3;
                
                gp.taskManager.addTask(new Task(
                    "Visit the Vet",
                    "Take your pet to the veterinarian",
                    35 * gp.tileSize,
                    45 * gp.tileSize
                ));
                gp.taskManager.addTask(new Task(
                    "Visit the Toy Shop",
                    "Buy a toy for your pet",
                    45 * gp.tileSize,
                    41 * gp.tileSize
                ));
                gp.taskManager.addTask(new Task(
                    "Visit Your Home",
                    "Take your pet to your house",
                    35 * gp.tileSize,
                    15 * gp.tileSize
                ));

        } 
        else
            {

                gp.ui.startDialogue(new String[]{
                    "You still have tasks left.",
                    "Check your task list."
                });
            }


        }
        else if (questStage == 3){
            if(gp.taskManager.allCompleted()){
                gp.ui.startDialogue(new String[]{
                    "You have proven to be a great pet owner.",
                    "Have fun with your pet!"
                });
                gp.wallet.addTransaction("Final Reward", 100);
                gp.money += 100;
                questStage = 4;
            }
            else{
                gp.ui.startDialogue(new String[]{
                    "You still have tasks left.",
                    "Check your task list."
                });
            }
        }
        else if (questStage > 3){
             gp.ui.startDialogue(new String[]{
                    "You have proven to be a great pet owner.",
                    "Have fun with your pet!"
                });
        }
    }

    public void draw(Graphics2D g2, GamePanel gp) {

        BufferedImage image = down1;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        g2.drawImage(image, screenX, screenY, null);

        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(Color.WHITE);
        g2.drawString("Wiseman", screenX - 10, screenY - 5);

    }

}