package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import java.awt.Color;
import java.awt.Font;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Pet extends Entity {

    public String name = "Pet";
    public String petType = "dog";

    
    public int hunger = 100;
    public int happiness = 50;
    public int energy = 100;

    public String mood = "Happy";

    public boolean isAlive = true;
    public boolean isSick = false;

    // stat decay rates
    public int hungerDecay = 1;
    public int happinessDecay = 1;
    public int energyDecay = 1;

    int statTimer = 0;


    int directionTimer = 0;
    int directionDelay = 10; // frames before sprite can change
    String lastDirection = "down";

    

    GamePanel gp;

    public Pet(GamePanel gp) {
        this.gp = gp;

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 16;
        solidArea.height = 16;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getPetImage();
    }

    public void setDefaultValues() {
        worldX = gp.player.worldX - gp.tileSize;
        worldY = gp.player.worldY;
        speed = 3;
        direction = "down";
    }

    // ---------- STAT SYSTEM ----------

    public void updateStats() {

        statTimer++;

        if(statTimer > 300) { // every ~5 seconds

            hunger -= hungerDecay;
            happiness -= happinessDecay;
            energy -= energyDecay;

            clampStats();
            checkHealth();

            statTimer = 0;
        }
    }

    public void checkHealth() {

        if(hunger <= 0 || energy <= 0) {
            isAlive = false;
            mood = "Dead";
            return;
        }

        if(hunger < 20 || happiness < 20) {
            isSick = true;
            mood = "Sick";
        } else {
            isSick = false;
        }

        updateMood();
    }

    protected void clampStats() {

        if(hunger > 100) hunger = 100;
        if(happiness > 100) happiness = 100;
        if(energy > 100) energy = 100;

        if(hunger < 0) hunger = 0;
        if(happiness < 0) happiness = 0;
        if(energy < 0) energy = 0;
    }

    protected void updateMood() {

        if(hunger < 40) {
            mood = "Hungry";
        }
        else if(energy < 40) {
            mood = "Tired";
        }
        else if(happiness < 40) {
            mood = "Sad";
        }
        else {
            mood = "Happy";
        }
    }

    // ---------- PLAYER ACTIONS ----------

    public void feed() {
        hunger += 20;
        happiness += 5;
        clampStats();
        updateMood();
        if(mood.equals("Happy")){
            gp.ui.showMessage(name + " loved the food!");
        }
    }

    public void play() {
        happiness += 15;
        energy -= 10;
        hunger -= 5;
        clampStats();
        updateMood();
    }

    public void rest() {
        energy += 20;
        clampStats();
        updateMood();
    }

    private void updateBehavior() {

        if(mood.equals("Happy")) {
            speed = 4;
        }
        else if(mood.equals("Hungry")) {
            speed = 2;
        }
        else if(mood.equals("Tired")) {
            speed = 1;
        }
        else if(mood.equals("Sad")) {
            speed = 2;
        }
        else if(mood.equals("Sick")) {
            speed = 1;
        }
    }

    // ---------- PET AI FOLLOW ----------

    public void update() {

        updateStats();
        updateBehavior();

        int dx = gp.player.worldX - worldX;
        int dy = gp.player.worldY - worldY;

        int distance = Math.abs(dx) + Math.abs(dy);

        if(distance < gp.tileSize / 2) {
            return;
        }

        String primaryDir;
        String secondaryDir;

        if(Math.abs(dx) > Math.abs(dy)) {
            primaryDir = (dx > 0) ? "right" : "left";
            secondaryDir = (dy > 0) ? "down" : "up";
        } else {
            primaryDir = (dy > 0) ? "down" : "up";
            secondaryDir = (dx > 0) ? "right" : "left";
        }

        //direction = primaryDir;

        

        if(!primaryDir.equals(lastDirection)) {
            directionTimer++;

            if(directionTimer > directionDelay) {
                direction = primaryDir;
                lastDirection = primaryDir;
                directionTimer = 0;
            }
        } else {
            direction = primaryDir;
            directionTimer = 0;
        }

        collisionOn = false;
        gp.cChecker.checkTile(this);

        if(!collisionOn) {
            move();

            spriteCounter++;
            if(spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        } else {

            direction = secondaryDir;
            collisionOn = false;
            gp.cChecker.checkTile(this);

            if(!collisionOn) {
                move();
            }
        }
    }

    private void move() {

        switch(direction) {
            case "up": worldY -= speed; break;
            case "down": worldY += speed; break;
            case "left": worldX -= speed; break;
            case "right": worldX += speed; break;
        }
    }

    // ---------- GRAPHICS ----------

    public void getPetImage() {

        up1 = setup(petType + "_up_1");
        up2 = setup(petType + "_up_2");

        down1 = setup(petType + "_down_1");
        down2 = setup(petType + "_down_2");

        left1 = setup(petType + "_left_1");
        left2 = setup(petType + "_left_2");

        right1 = setup(petType + "_right_1");
        right2 = setup(petType + "_right_2");
    }

    public BufferedImage setup(String imageName) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResource("/pet/" + imageName + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch(IOException e){
            e.printStackTrace();
        }

        return image;
    }

    public void draw(Graphics2D g2) {

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        BufferedImage image = null;

        switch(direction) {
            case "up": image = (spriteNum == 1) ? up1 : up2; break;
            case "down": image = (spriteNum == 1) ? down1 : down2; break;
            case "left": image = (spriteNum == 1) ? left1 : left2; break;
            case "right": image = (spriteNum == 1) ? right1 : right2; break;
        }

        // draw pet sprite
        g2.drawImage(image, screenX, screenY, null);


        // ---------- NAME TAG ----------
        if(name != null && !name.equals("")) {

            g2.setFont(new Font("Arial", Font.BOLD, 16));

            int nameWidth = g2.getFontMetrics().stringWidth(name);
            int nameX = screenX + gp.tileSize/2 - nameWidth/2;
            int nameY = screenY - 6;

            // background box
            g2.setColor(new Color(0,0,0,150));
            g2.fillRoundRect(nameX - 6, nameY - 16, nameWidth + 12, 18, 8, 8);

            // shadow
            g2.setColor(Color.black);
            g2.drawString(name, nameX+2, nameY+2);

            // main text
            g2.setColor(Color.white);
            g2.drawString(name, nameX, nameY);
        }

        // ---------- MOOD TAG ----------
        if(mood != null){

            g2.setFont(new Font("Arial", Font.PLAIN, 12));

            int moodWidth = g2.getFontMetrics().stringWidth(mood);
            int moodX = screenX + gp.tileSize/2 - moodWidth/2;
            int moodY = screenY - 26;

            g2.setColor(new Color(0,0,0,120));
            g2.fillRoundRect(moodX - 4, moodY - 12, moodWidth + 8, 16, 6, 6);

            if(mood.equals("Happy")) g2.setColor(Color.green);
            else if(mood.equals("Hungry")) g2.setColor(Color.orange);
            else if(mood.equals("Sad")) g2.setColor(Color.red);
            else g2.setColor(Color.yellow);
            g2.drawString(mood, moodX, moodY);
        }

    }
    
}