package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class UI {

    GamePanel gp;

    Font titleFont;
    Font optionFont;
    Font smallFont;

    // pet button positions
    public int dogX, dogY;
    public int catX, catY;
    public int koalaX, koalaY;

    public int buttonWidth = 450;
    public int buttonHeight = 50;

    // message system
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;

	// financial tracking
	public int foodCosts = 0;
	public int vetCosts = 0;

    public UI(GamePanel gp){
        this.gp = gp;

        titleFont = new Font("Segoe UI", Font.BOLD, 80);
        optionFont = new Font("Segoe UI", Font.PLAIN, 36);
        smallFont = new Font("Segoe UI", Font.PLAIN, 20);
    }

    public void showMessage(String text){
        message = text;
        messageOn = true;
    }

	public void drawWallet(Graphics2D g2){

		// dark background
		g2.setColor(new Color(232,169,97
		));
		g2.fillRect(gp.screenWidth/4-5, gp.screenHeight/4+5, gp.screenWidth/2, gp.screenHeight/2);
		//border
		g2.setColor(new Color(115,83 ,47));
		g2.setStroke(new java.awt.BasicStroke(5));
		g2.drawRect(gp.screenWidth/4-5, gp.screenHeight/4+5, gp.screenWidth/2, gp.screenHeight/2);

		g2.setFont(smallFont);
		g2.setColor(Color.WHITE);

		int x = gp.tileSize * 8;
		int y = gp.tileSize * 6;

		g2.setFont(optionFont);
		g2.drawString("Financial Overview", x, y);

		y += gp.tileSize * 2;
		g2.drawString("Current Savings: $" + gp.money, x, y);

		y += gp.tileSize;
		g2.drawString("Total Spent: $" + gp.totalSpent, x, y);

		y += gp.tileSize * 2;
		g2.drawString("Food Costs: $" + foodCosts, x, y);

		y += gp.tileSize;
		g2.drawString("Vet Costs: $" + vetCosts, x, y);

		y += gp.tileSize * 2;
		g2.drawString("Press I to close", x, y);
	}

    public void draw(Graphics2D g2){

		if(gp.showWallet){
			drawWallet(g2);
			return;
		}

        if(gp.gameState == gp.TITLE_STATE){
            drawTitleScreen(g2);
        }

        if(gp.gameState == gp.PLAY_STATE){
            drawHUD(g2);
        }
    }

    // ---------------- TITLE SCREEN ----------------

    public void drawTitleScreen(Graphics2D g2){

        // background
        g2.setColor(new Color(40,40,200));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        // TITLE
        g2.setFont(titleFont);

        String title = "Tung Tung Pet";

        int titleX = gp.screenWidth/2 - g2.getFontMetrics().stringWidth(title)/2;

        g2.setColor(Color.black);
        g2.drawString(title, titleX+4, 140);

        g2.setColor(Color.white);
        g2.drawString(title, titleX, 140);

        // subtitle
        g2.setFont(optionFont);

        String choose = "Choose Your Pet";

        int chooseX = gp.screenWidth/2 - g2.getFontMetrics().stringWidth(choose)/2;

        g2.drawString(choose, chooseX, 240);

        // ---------------- PET BUTTONS ----------------

        g2.setFont(optionFont);

        dogX = gp.screenWidth/2 - buttonWidth/2;
        dogY = 320;

        catX = gp.screenWidth/2 - buttonWidth/2;
        catY = 400;

        koalaX = gp.screenWidth/2 - buttonWidth/2;
        koalaY = 480;

        drawButton(g2, dogX, dogY, "Dog  (needs lots of love)");
        drawButton(g2, catX, catY, "Cat  (moody, hungry often)");
        drawButton(g2, koalaX, koalaY, "Koala  (sleepy, rests often)");

        // ---------------- NAME INPUT ----------------

        g2.setFont(smallFont);

        String nameText = "Name your pet";
        int nameX = gp.screenWidth/2 - g2.getFontMetrics().stringWidth(nameText)/2;

        g2.drawString(nameText, nameX, 560);

        int boxWidth = 260;
        int boxHeight = 35;

        int boxX = gp.screenWidth/2 - boxWidth/2;
        int boxY = 580;

        g2.drawRect(boxX, boxY, boxWidth, boxHeight);

        g2.drawString(gp.petNameInput, boxX + 10, boxY + 23);
    }

    // ---------------- BUTTON DRAWER ----------------

    private void drawButton(Graphics2D g2, int x, int y, String text){

        g2.setColor(new Color(255,255,255,40));
        g2.fillRoundRect(x, y, buttonWidth, buttonHeight, 15,15);

        g2.setColor(Color.white);
        g2.drawRoundRect(x, y, buttonWidth, buttonHeight, 15,15);

        int textX = x + buttonWidth/2 - g2.getFontMetrics().stringWidth(text)/2;
        int textY = y + 35;

        g2.drawString(text, textX, textY);
    }

    // ---------------- GAME HUD ----------------

    public void drawHUD(Graphics2D g2){

        g2.setFont(smallFont);
        g2.setColor(Color.white);

        g2.drawString("Money: $" + gp.money, 20, 40);

        if(gp.petManager.currentPet != null){

            g2.drawString("Hunger: " + gp.petManager.currentPet.hunger, 20, 70);
            g2.drawString("Happiness: " + gp.petManager.currentPet.happiness, 20, 100);
            g2.drawString("Energy: " + gp.petManager.currentPet.energy, 20, 130);
        }

        g2.drawString("F = Feed ($5)", 20, 180);
        g2.drawString("P = Play ($10)", 20, 210);
        g2.drawString("R = Rest (Free)", 20, 240);

        // message popup
        if(messageOn){

            int x = gp.screenWidth/2 - g2.getFontMetrics().stringWidth(message)/2;

            g2.drawString(message, x, 120);

            messageCounter++;

            if(messageCounter > 120){
                messageCounter = 0;
                messageOn = false;
            }
        }
    }
}