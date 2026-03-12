package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics2D;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    int standCounter = 0;

    int parkTimer = 0;


    public Player(GamePanel gp, KeyHandler keyH) {

        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 34;
        worldY = gp.tileSize * 25;
        speed = 5;
        direction = "down";
    }

    public void getPlayerImage() {
        up1 = setup("boy_up_1");
        up2 = setup("boy_up_2");
        down1 = setup("boy_down_1");
        down2 = setup("boy_down_2");
        left1 = setup("boy_left_1");
        left2 = setup("boy_left_2");
        right1 = setup("boy_right_1");
        right2 = setup("boy_right_2");
    }

    public BufferedImage setup(String imageName) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResource("/player/" + imageName + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public void Update() {


        if(keyH.enterPressed){

            for(int i = 0; i < gp.npc.length; i++){

                if(gp.npc[i] != null){

                    int dx = Math.abs(worldX - gp.npc[i].worldX);
                    int dy = Math.abs(worldY - gp.npc[i].worldY);

                    if(dx < gp.tileSize*2 && dy < gp.tileSize*2){

                        gp.npc[i].interact();

                        keyH.enterPressed = false;
                        return;
                    }
                }
            }
        }

        

        Rectangle playerRect = new Rectangle(worldX, worldY, gp.tileSize, gp.tileSize);

        Pet pet = gp.petManager.currentPet;
        Rectangle petRect = new Rectangle(pet.worldX, pet.worldY, gp.tileSize, gp.tileSize);


        boolean playerInPark = gp.parkArea.intersects(playerRect);
        boolean petInPark = gp.parkArea.intersects(petRect);

        
        if(playerInPark && petInPark){

            parkTimer++;
            System.out.println("Timer: " + parkTimer);

            if(parkTimer >= 60){
                pet.happiness += 3;

                if(pet.happiness > 100){
                    pet.happiness = 100;
                }

                System.out.println("Happiness increased!");
                parkTimer = 0;
            }

        }else{
            parkTimer = 0;
        }

        
        if (keyH.upPressed || keyH.downPressed ||
            keyH.leftPressed || keyH.rightPressed) {

            boolean movingVert = keyH.upPressed || keyH.downPressed;
            boolean movingHorz = keyH.leftPressed || keyH.rightPressed;

            int origSpeed = speed;
            double factor = (movingVert && movingHorz) ? (1.0 / Math.sqrt(2)) : 1.0;
            int moveAmount = (int) Math.round(origSpeed * factor);

            int moveY = 0;
            int moveX = 0;

            if (movingVert) moveY = keyH.upPressed ? -moveAmount : moveAmount;
            if (movingHorz) moveX = keyH.leftPressed ? -moveAmount : moveAmount;

            boolean canMoveY = true;
            boolean canMoveX = true;

            // Vertical collision check
            if (movingVert) {

                direction = keyH.upPressed ? "up" : "down";
                collisionOn = false;

                speed = Math.abs(moveY);
                gp.cChecker.checkTile(this);
                speed = origSpeed;

                if (collisionOn) canMoveY = false;
            }

            // Horizontal collision check
            if (movingHorz) {

                direction = keyH.leftPressed ? "left" : "right";
                collisionOn = false;

                speed = Math.abs(moveX);
                gp.cChecker.checkTile(this);
                speed = origSpeed;

                if (collisionOn) canMoveX = false;
            }

            if (canMoveY) worldY += moveY;
            if (canMoveX) worldX += moveX;

            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }

        } else {

            standCounter++;
            if (standCounter > 20) {
                spriteNum = 1;
                standCounter = 0;
            }
        }

        // Check object collision (chests, keys, etc.)
        int objIndex = gp.cChecker.checkObject(this, true);
        pickUpObject(objIndex);

    }

    public void pickUpObject(int i) {
        if(i != 999) {
            String objectName = gp.obj[i].name;

            if(objectName.equals("Chest")) {

                int mon = (int)(Math.random() * 10) + 5; // Random money between 5 and 15
                gp.wallet.addTransaction("Chest Money", mon);
                gp.money += mon;
                gp.ui.showMessage("+$" + mon + " from chest!");
                gp.obj[i] = null;
            }
        }
    }

    public void Draw(Graphics2D g2) {

        BufferedImage image = null;

        switch (direction) {
            case "up": image = (spriteNum == 1) ? up1 : up2; break;
            case "down": image = (spriteNum == 1) ? down1 : down2; break;
            case "left": image = (spriteNum == 1) ? left1 : left2; break;
            case "right": image = (spriteNum == 1) ? right1 : right2; break;
        }

        g2.drawImage(image, screenX, screenY, null);
    }
}