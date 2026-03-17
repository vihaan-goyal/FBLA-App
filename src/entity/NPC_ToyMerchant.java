package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

import java.awt.Color;
import java.awt.Font;

public class NPC_ToyMerchant extends Entity {

    GamePanel gp;
    int dialogueStateToy = 0;

    public NPC_ToyMerchant(GamePanel gp) {
        this.gp = gp;

        direction = "down";
        speed = 0;

        getImage();
    }

    public void getImage() {

        down1 = setup("toyMerchant"); // reuse same sprite
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

        gp.ui.speaker = "Toy Seller";


        if(dialogueStateToy == 0){
             gp.ui.startDialogue(new String[]{
                "Welcome to the toy store!",
                "Fun toys for only $10.",
            });
            dialogueStateToy = 1;
            return;
        }

        if(gp.money >= 10) {

            gp.money -= 10;
            gp.ui.toyCosts += 10;
            gp.totalSpent += 10;
            gp.wallet.addTransaction("Toy", -10);

            gp.inventoryManager.addItem("toy", 1);

            gp.ui.startDialogue(new String[]{
                "Here you go, one fun toy!"
            });

        }
        else {

            gp.ui.startDialogue(new String[]{
                "Welcome to the toy store!",
                "Toys cost $10.",
                "Come back when you have enough money!"
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
        g2.drawString("Toy Merchant", screenX - 10, screenY - 5);
    }
}