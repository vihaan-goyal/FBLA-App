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
    public int happiness = 100;
    public int energy = 100;

    public String mood = "Happy";

    public boolean isAlive = true;

    // stat decay rates
    public int hungerDecay = 2;
    public int happinessDecay = 2;
    public int energyDecay = 2;

    int statTimer = 0;

    int directionTimer = 0;
    int directionDelay = 10; // frames before sprite can change
    String lastDirection = "down";

    public boolean sick = false;

    int sicknessTimer = 0;

    int gameTime = 0;

    int oldX = worldX;
    int oldY = worldY;

    public int returnX;
    public int returnY;

    public boolean resting = false;
    public int restTimer = 0;

    public boolean isVaccinated = false;
    public boolean hasBeenSick = false;
    int demoSicknessTimer = 0;



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
        gameTime++;
        sicknessTimer++;

        if(statTimer > 300) { // every ~5 seconds

            hunger -= hungerDecay;
            happiness -= happinessDecay;
            energy -= energyDecay;
            clampStats();

            statTimer = 0;
        }
    }

    

    public void clampStats() {

        if(hunger > 100) hunger = 100;
        if(happiness > 100) happiness = 100;
        if(energy > 100) energy = 100;

        if(hunger < 0) hunger = 0;
        if(happiness < 0) happiness = 0;
        if(energy < 0) energy = 0;
    }

   protected void updateMood() {
        // update sickness first
        if(sick) {
            mood = "Sick";
            return;
        }

        if(hunger < 15 && energy < 15 && happiness < 15){
            mood = "Miserable";
        }
        // Double critical states second
        else if(hunger < 25 && happiness < 25) {
            mood = "Hangry";
        }
        else if(hunger < 25 && energy < 25) {
            mood = "Lethargic";
        }
        else if(energy < 25 && happiness < 25) {
            mood = "Sluggish";
        }

        // Single stat states third
        else if(hunger < 40) {
            mood = "Hungry";
        }
        else if(energy < 40) {
            mood = "Tired";
        }
        else if(happiness < 40) {
            mood = "Sad";
        }


        // Normal
        else {
            mood = "Happy";
        }
    }

    // ---------- PLAYER ACTIONS ----------

    public void feed() {
        hunger += 20;
        happiness += 5;
        energy += 5;
        clampStats();
        updateMood();
        double chance = Math.random();

        if(chance < 0.10 && ((NPC_OldMan) gp.npc[0]).questStage > 1) {  
            sick = true;
            hasBeenSick = true;
            gp.ui.showMessage(name.toUpperCase() + " got sick from the food!");
        }
        else if(mood.equals("Happy")){
            gp.ui.showMessage(name.toUpperCase() + " loved the food!");
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
        else if(mood.equals("Hungry") || mood.equals("Tired") || mood.equals("Sad")) {
            speed = 3;
        }
        else if(mood.equals("Hangry") || mood.equals("Lethargic") || mood.equals("Sluggish")) {
            speed = 2;
        }
        else if(mood.equals("Miserable") || mood.equals("Sick")) {
            speed = 1;
        }
    }

    // ---------- RESTING MECHANIC ----------

    public void restOnBed(int bedX, int bedY){

        // save original position
        returnX = worldX;
        returnY = worldY;

        // teleport onto bed
        worldX = bedX;
        worldY = bedY;

        resting = true;
        restTimer = 180; // 3 seconds
    }

    // ---------- PET AI FOLLOW ----------

    public void update() {

        updateStats();
        updateBehavior();
        updateMood();

        if(gameTime > 2400) { //change to 2400 for 20 seconds

            if(sicknessTimer > 1200 && ((NPC_OldMan) gp.npc[0]).questStage > 1) {
                sicknessTimer = 0;

                if(!sick && Math.random() < 0.10 && !isVaccinated && ((NPC_OldMan) gp.npc[0]).questStage >= 1) {
                    sick = true;
                    hasBeenSick = true;
                    gp.ui.showMessage("Oh no, your pet caught an illness!");
                }
            }
        }

        // Guarantee sickness for demo after second quest
        if(gp.npc[0] instanceof NPC_OldMan){

            NPC_OldMan oldman = (NPC_OldMan) gp.npc[0];

            if(oldman.questStage > 2 && !hasBeenSick && !isVaccinated){
                demoSicknessTimer++;

                if(demoSicknessTimer >= 900){ // ~10 seconds at 60 FPS
                    sick = true;
                    hasBeenSick = true;
                    demoSicknessTimer = 0;
                    gp.ui.showMessage("Oh no, your pet caught an illness!");
                }
            }
}

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

        if(sick) {
            speed = 1;
        } else {
            speed = 3; // normal speed
        }
        if(resting){

            restTimer--;

            if(restTimer <= 0){

                resting = false;

                // go back to original position
                worldX = returnX;
                worldY = returnY;

                energy += 30;
            }

            return; // stop normal movement while resting
        }

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
        //g2.drawImage(image, screenX, screenY, null);

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

        if(sick){
            g2.setColor(new Color(0,255,0,80)); // green tint
            g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
        }

        g2.setFont(new Font("Arial", Font.BOLD, 12));

        if(sick){
            g2.setColor(Color.RED);
            g2.drawString("Sick", screenX + 15, screenY - 40);
        }


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

        // --------- MOOD TAG ---------
if(mood != null){

    g2.setFont(new Font("Arial", Font.PLAIN, 12));

        int moodWidth = g2.getFontMetrics().stringWidth(mood);
        int moodX = screenX + gp.tileSize/2 - moodWidth/2;
        int moodY = screenY - 26;

        g2.setColor(new Color(0,0,0,120));
        g2.fillRoundRect(moodX - 4, moodY - 12, moodWidth + 8, 16, 6, 6);

        // Mood colors
        if(mood.equals("Happy")){
            g2.setColor(Color.green);
        }
        else if(mood.equals("Hungry")){
            g2.setColor(Color.orange);
        }
        else if(mood.equals("Sad")){
            g2.setColor(Color.red);
        }
        else if(mood.equals("Hangry")){
            g2.setColor(new Color(255, 80, 80)); // angry red
        }
        else if(mood.equals("Lethargic")){
            g2.setColor(new Color(150, 120, 255)); // tired purple
        }
        else if(mood.equals("Sluggish")){
            g2.setColor(new Color(120, 170, 255)); // low energy blue
        }
        else if(mood.equals("Tired")){
            g2.setColor(Color.yellow);
        }
        else if (mood.equals("Miserable")){
            g2.setColor(new Color(100, 100, 100)); // gray
        }
        else{
            g2.setColor(Color.white);
        }

        g2.drawString(mood, moodX, moodY);
    }

    }
    
}