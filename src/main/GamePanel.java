package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
import entity.Pet;
import tile.TileManager;
import pet.PetManager;
import object.SuperObject;
import entity.Entity;
import quest.TaskManager;
import quiz.QuizManager;
import inventory.InventoryManager;
import finance.WalletManager;
import java.awt.Rectangle;




public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSizes = 16;
    final int scale = 3;

    public final int tileSize = originalTileSizes * scale;
    public final int maxScreenCol = 32;
    public final int maxScreenRow = 20;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
	// WORLD SETTINGS
	public final int maxWorldCol = 75;
	public final int maxWorldRow = 75;

	public int currentDay = 1;
	public final int MAX_DAYS = 30;
	public int dayTimer = 0;
	public final int FRAMES_PER_DAY = 1000; // Adjust this value to control the length of each in-game day
    public String petNameInput = "";

    // FPS
    int fps = 60;

	// TIME
	public int hour = 8;
	public int minute = 0;
	private int clockTimer = 0;


    // SYSTEM
    TileManager tileManager = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
	AssetSetter aSetter = new AssetSetter(this);
    MouseHandler mouseH = new MouseHandler(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public UI ui = new UI(this);
    public PetManager petManager = new PetManager(this);
    public TaskManager taskManager = new TaskManager(this);
    public InventoryManager inventoryManager = new InventoryManager(this);
    public WalletManager wallet = new WalletManager();
    public QuizManager quizManager = new QuizManager(this);


    Thread gameThread;

    // GAME STATE
    
    public final int TITLE_STATE = 0;
    public final int PLAY_STATE = 1;
	public final int REPORT_STATE = 2;
    public int gameState = TITLE_STATE;

    // ENTITY
    public Player player = new Player(this, keyH);
    public Pet pet = null;

    // MONEY SYSTEM
    public int money = 0;
    public int totalSpent = 0;
    public boolean showWallet = false;

    //TASKS
    public boolean showTasks = false;

    //OBJECTS
    public SuperObject obj[] = new SuperObject[20];
    public Entity npc[] = new Entity[10];

    //INVENTORY
    public boolean inventoryOpen = false;

    //PET STATS
    public boolean petStatsOpen = false;

    //PARK

    public Rectangle parkArea = new Rectangle(
        50 * tileSize,
        22 * tileSize,
        10 * tileSize,
        8 * tileSize
    );

    
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.addMouseListener(mouseH);
    }

	public void updateClock() {

		clockTimer++;

		if (clockTimer >= 45) {  // 30 frames = 1 in-game minute
			minute++;
			clockTimer = 0;
		}

		if (minute >= 60) {
			minute = 0;
			hour++;
		}

		if (hour >= 24) {
			hour = 0;
			nextDay();
		}	
	}

    public void setupGame() {
        aSetter.setObject();
        aSetter.setNPC();
        gameState = TITLE_STATE;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

   @Override
	public void run() {

		double drawInterval = 1000000000.0 / fps;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;

		while (gameThread != null) {

			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			lastTime = currentTime;

			if (delta >= 1) {
				Update();
				repaint();
				delta--;
			}
		}
	}

	public void nextDay() {

        Pet pet = petManager.currentPet;


		currentDay++;

		pet.hunger -= 8;
		pet.energy -= 6;
		pet.happiness -= 4;

		if(currentDay > MAX_DAYS) {
			gameState = REPORT_STATE;
        }
    }

    public void Update() {

        if (gameState == PLAY_STATE) {

            // player movement
            player.Update();
            taskManager.update();

            if(keyH.inventoryPressed){
                inventoryOpen = !inventoryOpen;
                keyH.inventoryPressed = false;
            }
            
            if(keyH.petStatsPressed){
                petStatsOpen = !petStatsOpen;
                keyH.petStatsPressed = false;
            }
            if(keyH.walletPressed){
                showWallet = !showWallet;
                keyH.walletPressed = false;
                showTasks = false;
            }
        
            if(keyH.taskPressed){
                showTasks = !showTasks;
                showWallet = false;
                keyH.taskPressed = false;
            }

            // update pet if one exists
            if (petManager.currentPet != null) {

                Pet pet = petManager.currentPet;

                pet.update();

                // FEED
                if (keyH.feedPressed) {

                    if(inventoryManager.removeItem("food", 1)) {

                        pet.feed();
                      
                    } else {

                        ui.showMessage("You have no pet food!");

                    }

                    keyH.feedPressed = false;
                }

                // PLAY
                if (keyH.playPressed && money >= 10) {
                    pet.play();
                    wallet.addTransaction("Pet Played", -10);
                    money -= 10;
                    totalSpent += 10;
                    keyH.playPressed = false;
                }
            }

            updateClock();

            dayTimer++;

            if (dayTimer >= FRAMES_PER_DAY) {
                nextDay();
                dayTimer = 0;
            }
        }
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        tileManager.draw(g2);
		
        for(int i = 0; i < obj.length; i++) {
            if(obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }

        for(int i = 0; i < npc.length; i++) {
            if(npc[i] != null) {
                npc[i].draw(g2, this);
            }
        }

        if(petManager.currentPet != null){
            petManager.currentPet.draw(g2);
        }

        player.Draw(g2);
		ui.draw(g2);
        if(petStatsOpen){
            ui.drawPetStats(g2);
        }
        g2.dispose();
    }
}