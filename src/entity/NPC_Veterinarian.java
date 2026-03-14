package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

import java.awt.Color;
import java.awt.Font;

public class NPC_Veterinarian extends Entity {

    GamePanel gp;
    int dialogueStageVet = 0;


    public NPC_Veterinarian(GamePanel gp) {
        this.gp = gp;

        direction = "down";
        speed = 0;

        getImage();
    }

    public void getImage() {
        down1 = setup("vet_down_1");
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
    public void interact() {
        NPC_OldMan oldMan = (NPC_OldMan) gp.npc[0];

        gp.ui.speaker = "Veterinarian";
        if(oldMan.questStage == 3){
            gp.ui.startDialogue(new String[]{
                "Hello!",
                "I gave your pet some shots!",
                "It should be fully vaccinated now!"
            });
            oldMan.questStage = 4;
            return;
        }

        if(dialogueStageVet == 0){
            gp.ui.startDialogue(new String[]{
                "Welcome to the veterinarian office!",
                "Here, you can purchase medicine for your dog.",
                "Come back anytime if your pet get sick."
            });

            dialogueStageVet = 1;
            return;
        }

        if(gp.money >= 15) {

            gp.money -= 15;            
            gp.wallet.addTransaction("Pet Medicine", -15);
            gp.ui.vetCosts += 15;
            gp.totalSpent += 15;
            gp.inventoryManager.addItem("medicine", 1);

            gp.ui.startDialogue(new String[]{
                "Hello!",
                "Here is some dog medicine.",
                "That will be $15."
            });

        } else {

            gp.ui.startDialogue(new String[]{
                "Hello!",
                "Dog medicine costs $15.",
                "Come back when you have enough money."
            });

        }
    }

    public void draw(Graphics2D g2, GamePanel gp) {

        BufferedImage image = down1;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(Color.WHITE);
        g2.drawString("Veterinarian", screenX - 20, screenY - 5);
    }
}