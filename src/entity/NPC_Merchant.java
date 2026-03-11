package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

import java.awt.Color;
import java.awt.Font;

public class NPC_Merchant extends Entity {

    GamePanel gp;

    public NPC_Merchant(GamePanel gp) {
        this.gp = gp;

        direction = "down";
        speed = 0;

        getImage();
    }

    public void getImage() {

        down1 = setup("merchant_down_1");
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
        gp.ui.speaker = "Merchant";


        if(gp.money >= 10) {

            gp.money -= 10;
            gp.ui.foodCosts += 10;
            gp.totalSpent += 10;
            gp.inventoryManager.addItem("food", 1);
            gp.ui.startDialogue(new String[]{
                "Welcome to my pet shop!",
                "Fresh pet food for only $10.",
                "Your pet will love it!"
            });

        } 
        else {


            gp.ui.startDialogue(new String[]{
                "Welcome to my pet shop!",
                "Pet food costs $10.",
                "Come back when you have enough money!"
            });

        }
    }

    public void draw(Graphics2D g2, GamePanel gp) {

        BufferedImage image = down1;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // draw merchant
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

        // draw shop sign
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(Color.WHITE);
        g2.drawString("Pet Food Shop", screenX - 20, screenY - 5);
    }

}