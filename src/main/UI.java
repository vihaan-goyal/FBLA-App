package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.FontMetrics;

import finance.Transaction;
import quest.Task;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;


public class UI {

    GamePanel gp;

    Font titleFont;
    Font optionFont;
    Font smallFont;
    Font popupFont;
    Font menuFont;
    Font subtitleFont;

    BufferedImage hungerIcon;
    BufferedImage happinessIcon;
    BufferedImage energyIcon;

    // pet button positions
    public int dogX, dogY;
    public int catX, catY;
    public int koalaX, koalaY;

    public int buttonWidth = 200;
    public int buttonHeight = 50;

    // message system
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;

	// financial tracking
	public int foodCosts = 0;
	public int vetCosts = 0;
    public int toyCosts = 0;

	// dialogue system
	public boolean dialogueOn = false;
	public String[] dialogueLines;
	public int dialogueIndex = 0;
    public String speaker = "NPC";

    //quiz system
    public boolean typingMode = false;
    public String currentInput = "";

    //title page
    public int hoveredPet = -1;
    public int mouseX;
    public int mouseY;

    
	
    public UI(GamePanel gp){
        this.gp = gp;

        titleFont = new Font("Segoe UI", Font.BOLD, 80);
        menuFont = new Font("Segoe UI", Font.BOLD, 50);
        subtitleFont = new Font("Segoe UI", Font.HANGING_BASELINE, 36);
        optionFont = new Font("Segoe UI", Font.PLAIN, 36);
        popupFont = new Font("Segoe UI", Font.BOLD, 25);
        smallFont = new Font("Segoe UI", Font.PLAIN, 20);

        try {
            hungerIcon = ImageIO.read(getClass().getResource("/icons/hunger.png"));
            happinessIcon = ImageIO.read(getClass().getResource("/icons/happiness.png"));
            energyIcon = ImageIO.read(getClass().getResource("/icons/energy.png"));
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    

    public void showMessage(String text){
        message = text;
        messageOn = true;
    }

	public void startDialogue(String[] lines){
		dialogueLines = lines;
		dialogueIndex = 0;
		dialogueOn = true;
	}

	public void drawDialogueBox(Graphics2D g2){

		int boxX = gp.tileSize;
		int boxY = gp.screenHeight - gp.tileSize * 4;
		int boxWidth = gp.screenWidth - gp.tileSize * 2;
		int boxHeight = gp.tileSize * 2;

		// background
		g2.setColor(new Color(0,0,0,200));
		g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 25,25);

		// border
		g2.setColor(Color.WHITE);
		g2.setStroke(new java.awt.BasicStroke(4));
		g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 25,25);

		g2.setFont(optionFont);

		String text = dialogueLines[dialogueIndex];

		int textX = boxX + 20;
		int textY = boxY + 50;

		// draw speaker (bold)
        g2.setFont(optionFont.deriveFont(Font.BOLD));
        g2.drawString(speaker + ":", textX, textY);

        // measure width of speaker text
        int speakerWidth = g2.getFontMetrics().stringWidth(speaker + ":");

        // draw dialogue (normal)
        g2.setFont(optionFont);
        g2.drawString(" " + text, textX + speakerWidth, textY);
	}

	

    public void draw(Graphics2D g2){

        if(gp.gameState == gp.PLAY_STATE){
            drawHUD(g2);
        }

        if(gp.inventoryOpen){
            drawInventory(g2);
            return;
        }

        if(gp.showTasks){
            drawTasks(g2);
            return;
        }

        if(gp.showWallet){
            drawWallet(g2);
            return;
        }
		if(dialogueOn){
			drawDialogueBox(g2);
		}


        if(gp.gameState == gp.TITLE_STATE){
            drawTitleScreen(g2);
        }

        if(typingMode){

            int boxX = gp.tileSize;
            int boxY = gp.screenHeight - gp.tileSize * 4;
            int boxWidth = gp.screenWidth - gp.tileSize * 2;
            int boxHeight = gp.tileSize * 2;

            g2.setColor(new Color(0,0,0,200));
		    g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 25,25);

		    // border
		    g2.setColor(Color.WHITE);
		    g2.setStroke(new java.awt.BasicStroke(4));
		    g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 25,25);

            g2.setColor(Color.WHITE);
            g2.setFont(optionFont);
            g2.drawString("Answer: " + currentInput + "_",
                        gp.tileSize + 10,
                        gp.screenHeight - gp.tileSize * 3);
        }
    }

    // ---------------- PET STATS ----------------

    public void drawPetStats(Graphics2D g2){

        // background
        g2.setColor(new Color(232,169,97));
        g2.fillRect(gp.screenWidth/4-5, gp.screenHeight/4+5, gp.screenWidth/2, gp.screenHeight/2);

        // border
        g2.setColor(new Color(115,83,47));
        g2.setStroke(new BasicStroke(5));
        g2.drawRect(gp.screenWidth/4-5, gp.screenHeight/4+5, gp.screenWidth/2, gp.screenHeight/2);

        g2.setColor(Color.WHITE);

        int x = gp.tileSize * 9;
        int y = gp.tileSize * 6;

        g2.setFont(menuFont);
        g2.drawString("Pet Status", x - gp.tileSize, y);

        g2.setFont(optionFont);
        if(gp.petManager.currentPet != null){

            var pet = gp.petManager.currentPet;

            int barWidth = gp.tileSize * 4;
            int barHeight = 20;
            int barX = x + 160;

            // HUNGER
            y += gp.tileSize * 2;
            g2.drawImage(hungerIcon, x - 40, y - 30, 32, 32, null);
            g2.drawString("Hunger:", x, y);

            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(barX-20, y-20, barWidth, barHeight);

            g2.setColor(new Color(150, 75, 0)); //brown
            g2.fillRect(barX-20, y-20, (int)(barWidth * (pet.hunger / 100.0)), barHeight);

            g2.setColor(Color.BLACK);
            g2.drawRect(barX-20, y-20, barWidth, barHeight);


            // HAPPINESS
            y += gp.tileSize;
            g2.setColor(Color.WHITE);
            g2.drawImage(happinessIcon, x - 40, y - 30, 32, 32, null);
            g2.drawString("Happiness:", x, y);

            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(barX+20, y-20, barWidth, barHeight);

            g2.setColor(new Color (255,244,79)); //yellow
            g2.fillRect(barX+20, y -20, (int)(barWidth * (pet.happiness / 100.0)), barHeight);

            g2.setColor(Color.BLACK);
            g2.drawRect(barX+20, y-20, barWidth, barHeight);


            // ENERGY
            y += gp.tileSize;
            g2.setColor(Color.WHITE);
            g2.drawImage(energyIcon, x - 40, y - 30, 32, 32, null);
            g2.drawString("Energy:", x, y);

            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(barX-30, y-20, barWidth, barHeight);

            g2.setColor(Color.GREEN);
            g2.fillRect(barX-30, y-20, (int)(barWidth * (pet.energy / 100.0)), barHeight);

            g2.setColor(Color.BLACK);
            g2.drawRect(barX-30, y-20, barWidth, barHeight);

            g2.setColor(Color.WHITE);
            g2.drawString("Status: ", x, y + gp.tileSize);


            if(pet.sick){
                g2.setColor(Color.RED);
                g2.drawString("Sick", x + 110, y + gp.tileSize);
            }
            else{
                g2.setColor(Color.GREEN);
                g2.drawString("Healthy", x + 110, y + gp.tileSize);
            }
        }

        y += gp.tileSize * 3;

        g2.setColor(Color.WHITE);
        g2.drawString("Press 4 to close", x, y);
    }

    // ---------------- TITLE SCREEN ----------------

    public void drawTitleScreen(Graphics2D g2){

        //background
        BufferedImage grassBackground = null;
        try{
            grassBackground = ImageIO.read(getClass().getResource("/tiles/grass.png"));
        }
        catch(IOException e){
            System.out.println(e.getStackTrace());
        }
        UtilityTool uTool = new UtilityTool();
        grassBackground = uTool.scaleImage(grassBackground, gp.tileSize, gp.tileSize);
        for(int x = 0; x < gp.screenWidth; x += gp.tileSize){
            for(int y = 0; y < gp.screenHeight; y += gp.tileSize){
                g2.drawImage(grassBackground, x, y, null);
            }
        }
        g2.setColor(new Color(0,0,0,120));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        // TITLE
        g2.setFont(titleFont);

        String title = "PetLife";

        int titleX = gp.screenWidth/2 - g2.getFontMetrics().stringWidth(title)/2;

        g2.setColor(Color.black);
        g2.drawString(title, titleX+4, 140);

        g2.setColor(Color.white);
        g2.drawString(title, titleX, 140);

        // subtitle
        g2.setFont(subtitleFont);

        String choose = "Choose Your Pet";

        int chooseX = gp.screenWidth/2 - g2.getFontMetrics().stringWidth(choose)/2;

        g2.drawString(choose, chooseX, 240);

        // ---------------- 
        BufferedImage dogImage = null;
        BufferedImage catImage = null;
        BufferedImage koalaImage = null;


        g2.setFont(optionFont);
        try {

            dogImage = ImageIO.read(getClass().getResource("/pet/dog_down_1.png"));
            catImage = ImageIO.read(getClass().getResource("/pet/cat_down_1.png"));
            koalaImage = ImageIO.read(getClass().getResource("/pet/koala_down_1.png"));

        } catch(IOException e){
            e.printStackTrace();
        }

        dogImage = uTool.scaleImage(dogImage, 300, 300);
        catImage = uTool.scaleImage(catImage, 300, 300);
        koalaImage = uTool.scaleImage(koalaImage, 300, 300);

        int panelX = gp.screenWidth/2+40;
        int panelY = 300;
        int panelWidth = 220;
        int panelHeight = 300;
 

        g2.setColor(new Color(0,0,0,90));
        g2.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 30, 30);

        BufferedImage preview = null;
        String[] stats = null;

        hoveredPet = -1;

        if(mouseX > dogX && mouseX < dogX + buttonWidth &&
        mouseY > dogY && mouseY < dogY + buttonHeight){
            hoveredPet = 0;
        }

        if(mouseX > catX && mouseX < catX + buttonWidth &&
        mouseY > catY && mouseY < catY + buttonHeight){
            hoveredPet = 1;
        }

        if(mouseX > koalaX && mouseX < koalaX + buttonWidth &&
        mouseY > koalaY && mouseY < koalaY + buttonHeight){
            hoveredPet = 2;
        }


        FontMetrics fm = g2.getFontMetrics();

        int textWidth = fm.stringWidth("Dog");

        int x = panelX + panelWidth/2 - textWidth/2;
        int y = panelY-10;

        if(hoveredPet == 0){
            g2.setColor(Color.white);
            g2.drawString("Dog", x, y);
            preview = dogImage;
            stats = new String[]{
                "Loyal and energetic",
                "Needs lots of love",
                "High happiness gain"
            };
        }

        else if(hoveredPet == 1){
            textWidth = fm.stringWidth("Cat");
            x = panelX + panelWidth/2 - textWidth/2;

            g2.setColor(Color.white);
            g2.drawString("Cat", x, y);
            preview = catImage;
            stats = new String[]{
                "Independent",
                "Gets hungry often",
                "Moody personality"
            };
        }

        else if(hoveredPet == 2){
            textWidth = fm.stringWidth("Koala");
            x = panelX + panelWidth/2 - textWidth/2;
            
            g2.setColor(Color.white);
            g2.drawString("Koala", x, y);
            preview = koalaImage;
            stats = new String[]{
                "Very sleepy",
                "Rests frequently",
                "Low energy drain"
            };
        }
        else{
            stats = new String[]{
                ""
            };
        }

        int imageSize = 120;

        g2.drawImage(preview,
            panelX + (panelWidth - imageSize)/2,
            panelY + 20,
            imageSize,
            imageSize,
            null);

        g2.setFont(g2.getFont().deriveFont(18f));

        int textY = panelY + 170;

        for(String s : stats){
            int tW = (int)g2.getFontMetrics().getStringBounds(s, g2).getWidth();
            int textX = panelX + (panelWidth - tW)/2;

            g2.setColor(Color.WHITE);
            g2.drawString(s, textX, textY);
            textY += 25;
        }


        g2.setFont(optionFont);

        dogX = gp.screenWidth/2 - buttonWidth - 30;
        dogY = 330;

        catX = gp.screenWidth/2 - buttonWidth - 30;
        catY = 410;

        koalaX = gp.screenWidth/2 - buttonWidth - 30;
        koalaY = 490;

        drawButton(g2, dogX, dogY, "Dog");
        drawButton(g2, catX, catY, "Cat");
        drawButton(g2, koalaX, koalaY, "Koala ");
        

        // ---------------- NAME INPUT ----------------

        g2.setFont(smallFont);

        int boxWidth = 260;
        int boxHeight = 35;
        int boxX = gp.screenWidth/2 - boxWidth/2;
        int boxY = gp.screenHeight * 3 / 4;
        String nameText = "Name your pet:";
        int nameX = gp.screenWidth/2 - g2.getFontMetrics().stringWidth(nameText)/2;

        g2.drawString(nameText, nameX, boxY-20);

        g2.drawRect(boxX, boxY, boxWidth, boxHeight);

        g2.drawString(gp.petNameInput + "_", boxX + 10, boxY+25);
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

        g2.setFont(smallFont);
        g2.drawString("1 = Wallet" , 20, 40);
        g2.drawString("2 = Inventory" , 20, 70);
        g2.drawString("3 = TODO List" , 20, 100);
        g2.drawString("4 = Pet Stats" , 20, 130);
        g2.drawString("---------------", 20, 150);

        g2.drawString("F = Feed", 20, 180);
        g2.drawString("M = Medicine", 20, 210);
        g2.drawString("P = Play", 20, 240);

        // message popup
        if(messageOn){
            g2.setFont(popupFont);
            int x = gp.screenWidth/2 - g2.getFontMetrics().stringWidth(message)/2;

            g2.drawString(message, x, 120);

            messageCounter++;

            if(messageCounter > 120){
                messageCounter = 0;
                messageOn = false;
            }
        }
    }

    //DRAW INVENTORY
    public void drawInventory(Graphics2D g2){

        // dark background
        g2.setColor(new Color(232,169,97));
        g2.fillRect(gp.screenWidth/4-5, gp.screenHeight/4+5, gp.screenWidth/2, gp.screenHeight/2);

        // border
        g2.setColor(new Color(115,83,47));
        g2.setStroke(new java.awt.BasicStroke(5));
        g2.drawRect(gp.screenWidth/4-5, gp.screenHeight/4+5, gp.screenWidth/2, gp.screenHeight/2);

        g2.setFont(smallFont);
        g2.setColor(Color.WHITE);

        int x = gp.tileSize * 8;
        int y = gp.tileSize * 6;

        g2.setFont(menuFont);
        g2.drawString("Inventory", x, y);

        g2.setFont(optionFont);
        y += gp.tileSize * 2;

        int foodCount = gp.inventoryManager.getItemCount("food");
        g2.drawString("Pet Food: " + foodCount, x, y);

        int medicineCount = gp.inventoryManager.getItemCount("medicine");

        g2.drawString("Medicine: " + medicineCount, x, y + gp.tileSize);

        int toyCount = gp.inventoryManager.getItemCount("toy");
        g2.drawString("Toys: " + toyCount, x, y + gp.tileSize*2);

        y += gp.tileSize * 5;

        g2.drawString("Press 2 to close", x, y);
    }

    //DRAW TASKS
	public void drawTasks(Graphics2D g2){

		g2.setColor(new Color(232,169,97));
		g2.fillRect(gp.screenWidth/4-5, gp.screenHeight/4+5, gp.screenWidth/2, gp.screenHeight/2);

		// border
		g2.setColor(new Color(115,83,47));
		g2.setStroke(new java.awt.BasicStroke(5));
		g2.drawRect(gp.screenWidth/4-5, gp.screenHeight/4+5, gp.screenWidth/2, gp.screenHeight/2);

		g2.setColor(Color.WHITE);

		int x = gp.tileSize * 8;
		int y = gp.tileSize * 6;

		g2.setFont(menuFont);


		g2.drawString("TODO List", x, y);

		y += gp.tileSize * 2;

        g2.setFont(optionFont);

		for(Task t : gp.taskManager.tasks){

			int boxSize = 24;

			int boxX = x;
			int boxY = y - 20;

			// draw checkbox square
			g2.setColor(Color.WHITE);
			g2.drawRect(boxX, boxY, boxSize, boxSize);

			// draw checkmark if completed
			if(t.completed){

				g2.setColor(Color.GREEN);
				g2.setStroke(new java.awt.BasicStroke(4));

				g2.drawLine(boxX + 4, boxY + 14, boxX + 10, boxY + 20);
				g2.drawLine(boxX + 10, boxY + 20, boxX + 20, boxY + 6);
			}

			// draw task name
			g2.setColor(Color.WHITE);
			g2.drawString(t.name, x + 40, y);

			y += gp.tileSize;
		}

		y += gp.tileSize;

		g2.drawString("Press 3 to close", x, y);
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

		g2.setFont(menuFont);
		g2.drawString("Wallet", x, y);

        g2.setFont(optionFont);
		y += gp.tileSize * 2;
		g2.drawString("Balance: $" + gp.money, x, y);

		y += gp.tileSize;
		g2.drawString("Total Spent: $" + gp.totalSpent, x, y);

		y += gp.tileSize * 2;
		g2.drawString("Food Costs: $" + foodCosts, x, y);

		y += gp.tileSize;
		g2.drawString("Vet Costs: $" + vetCosts, x, y);

        y += gp.tileSize;
		g2.drawString("Toy Costs: $" + toyCosts, x, y);


		y += gp.tileSize * 2;
		g2.drawString("Press 1 to close", x, y);

        // -------- TRANSACTION HISTORY --------

        int transX = gp.screenWidth/2 + 40;   // right side of wallet
        int transY = gp.screenHeight/4 + 120;

        g2.setFont(menuFont);
        g2.drawString("Transactions", transX, transY-72);
        g2.setFont(smallFont);
        for(Transaction t : gp.wallet.history){

            String sign = (t.amount > 0 ? "+" : "");

            g2.drawString(
                sign + t.amount + " : " + t.description,
                transX,
                transY
            );

            transY += 25;
        }
    }

}