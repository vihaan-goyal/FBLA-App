package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.FontMetrics;
import java.awt.RenderingHints;

import finance.Transaction;
import quest.Task;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class UI {

    GamePanel gp;

    // Georgia for display, Verdana for body — both ship with Windows
    Font titleFont;    // Georgia Bold 76   — main title
    Font menuFont;     // Georgia Bold 44   — panel headers
    Font optionFont;   // Verdana Plain 28  — primary body text
    Font smallFont;    // Verdana Plain 18  — secondary / labels
    Font popupFont;    // Verdana Bold 22   — popup messages
    Font subtitleFont; // Georgia Italic 32 — title-screen subtitle

    BufferedImage hungerIcon;
    BufferedImage happinessIcon;
    BufferedImage energyIcon;

    // ---- original palette ----
    static final Color C_PANEL  = new Color(232, 169, 97);   // warm tan
    static final Color C_BORDER = new Color(115, 83,  47);   // dark brown
    static final Color C_DARK   = new Color(190, 130, 65);   // mid-brown — dividers
    static final Color C_WHITE  = Color.WHITE;
    static final Color C_DIM    = new Color(255, 255, 255, 180);

    // pet button positions (read by MouseHandler)
    public int dogX, dogY, catX, catY, koalaX, koalaY;
    public int buttonWidth = 200, buttonHeight = 50;

    // message system
    public boolean messageOn = false;
    public String  message   = "";
    int messageCounter = 0;

    // financial totals live in gp.wallet (foodCosts, vetCosts, toyCosts, balance, totalSpent)

    // dialogue system
    public boolean  dialogueOn    = false;
    public String[] dialogueLines;
    public int      dialogueIndex = 0;
    public String   speaker       = "NPC";

    // quiz system
    public boolean typingMode   = false;
    public String  currentInput = "";

    // title screen
    public int hoveredPet = -1, mouseX, mouseY;

    // transaction scroll
    int transactionScroll      = 0;
    int maxVisibleTransactions = 11;

    // ----------------------------------------------------------------

    public UI(GamePanel gp) {
        this.gp = gp;

        titleFont    = new Font("Georgia",  Font.BOLD,            76);
        menuFont     = new Font("Georgia",  Font.BOLD,            44);
        subtitleFont = new Font("Georgia",  Font.ITALIC,          32);
        optionFont   = new Font("Verdana",  Font.PLAIN,           28);
        popupFont    = new Font("Verdana",  Font.BOLD,            22);
        smallFont    = new Font("Verdana",  Font.PLAIN,           18);

        try {
            hungerIcon    = ImageIO.read(getClass().getResource("/icons/hunger.png"));
            happinessIcon = ImageIO.read(getClass().getResource("/icons/happiness.png"));
            energyIcon    = ImageIO.read(getClass().getResource("/icons/energy.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================================================================
    //  SHARED HELPERS
    // ================================================================

    private void aa(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    /** Shared panel background used by all four overlay menus. */
    private void drawPanel(Graphics2D g2) {
        int x = gp.screenWidth  / 4 - 5;
        int y = gp.screenHeight / 4 + 5;
        int w = gp.screenWidth  / 2;
        int h = gp.screenHeight / 2;

        g2.setColor(C_PANEL);
        g2.fillRoundRect(x, y, w, h, 24, 24);

        g2.setColor(C_BORDER);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x, y, w, h, 24, 24);
    }

    /** Thin horizontal divider inside a panel. */
    private void drawDivider(Graphics2D g2, int x, int y, int w) {
        g2.setColor(C_DARK);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(x, y, x + w, y);
    }

    /** Rounded stat bar with icon, label, and a fixed bar start column (barX). */
    private void drawStatBar(Graphics2D g2, BufferedImage icon, String label,
                             int value, Color fill, int x, int y, int barX, int barW) {
        int barH = 22;

        // icon
        if (icon != null) g2.drawImage(icon, x, y - 20, 24, 24, null);

        // label — left-aligned after icon
        g2.setFont(smallFont);
        g2.setColor(C_WHITE);
        g2.drawString(label, x + 32, y);

        // track — starts at fixed barX so all bars align
        g2.setColor(new Color(80, 50, 20, 180));
        g2.fillRoundRect(barX, y - 18, barW, barH, 10, 10);

        // fill
        int filled = Math.max((int)(barW * (value / 100.0)), 4);
        g2.setColor(fill);
        g2.fillRoundRect(barX, y - 18, filled, barH, 10, 10);

        // border
        g2.setColor(C_BORDER);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(barX, y - 18, barW, barH, 10, 10);
    }

    // ================================================================
    //  PUBLIC API
    // ================================================================

    public void showMessage(String text) {
        message        = text;
        messageOn      = true;
        messageCounter = 0;
    }

    public void startDialogue(String[] lines) {
        dialogueLines = lines;
        dialogueIndex = 0;
        dialogueOn    = true;
    }

    // ================================================================
    //  MAIN DRAW DISPATCH
    // ================================================================

    public void draw(Graphics2D g2) {
        aa(g2);

        if (gp.gameState == gp.PLAY_STATE) drawHUD(g2);

        if (gp.inventoryOpen) { drawInventory(g2); return; }
        if (gp.showTasks)     { drawTasks(g2);     return; }
        if (gp.showWallet)    { drawWallet(g2);    return; }

        if (dialogueOn) drawDialogueBox(g2);

        if (gp.gameState == gp.TITLE_STATE) drawTitleScreen(g2);

        if (typingMode) {
            int bx = gp.tileSize;
            int by = gp.screenHeight - gp.tileSize * 4;
            int bw = gp.screenWidth  - gp.tileSize * 2;
            int bh = gp.tileSize * 2;

            g2.setColor(new Color(0, 0, 0, 210));
            g2.fillRoundRect(bx, by, bw, bh, 24, 24);
            g2.setColor(C_WHITE);
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(bx, by, bw, bh, 24, 24);
            g2.setFont(optionFont);
            g2.setColor(C_WHITE);
            g2.drawString("Answer: " + currentInput + "_", bx + 24, by + 56);
        }
    }

    // ================================================================
    //  DIALOGUE BOX
    // ================================================================

    public void drawDialogueBox(Graphics2D g2) {
        aa(g2);

        int bx = gp.tileSize;
        int by = gp.screenHeight - gp.tileSize * 4 - 10;
        int bw = gp.screenWidth  - gp.tileSize * 2;
        int bh = gp.tileSize * 2 + 20;

        // background
        g2.setColor(new Color(0, 0, 0, 210));
        g2.fillRoundRect(bx, by, bw, bh, 24, 24);

        // border
        g2.setColor(C_WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(bx, by, bw, bh, 24, 24);

        // speaker name on its own line
        g2.setFont(new Font("Georgia", Font.BOLD, 22));
        g2.setColor(new Color(255, 220, 140));   // warm amber — readable on black
        g2.drawString(speaker, bx + 24, by + 30);

        // thin separator under speaker
        g2.setColor(new Color(255, 255, 255, 60));
        g2.setStroke(new BasicStroke(1));
        g2.drawLine(bx + 24, by + 38, bx + bw - 24, by + 38);

        // dialogue text
        g2.setFont(optionFont);
        g2.setColor(C_WHITE);
        g2.drawString(dialogueLines[dialogueIndex], bx + 24, by + 74);

        // continue hint (bottom-right)
        g2.setFont(new Font("Verdana", Font.PLAIN, 14));
        g2.setColor(C_DIM);
        String hint = dialogueIndex < dialogueLines.length - 1 ? "[ Enter ]" : "[ Enter to close ]";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(hint, bx + bw - fm.stringWidth(hint) - 16, by + bh - 12);
    }

    // ================================================================
    //  PET STATS
    // ================================================================

    public void drawPetStats(Graphics2D g2) {
        aa(g2);
        drawPanel(g2);

        int px  = gp.screenWidth  / 4 - 5 + 24;   // panel left + padding
        int py  = gp.screenHeight / 4 + 5 + 24;   // panel top  + padding
        int pw  = gp.screenWidth  / 2 - 48;        // usable width

        // header
        g2.setFont(menuFont);
        g2.setColor(C_WHITE);
        g2.drawString("Pet Status", px, py + 40);

        drawDivider(g2, px, py + 52, pw);

        if (gp.petManager.currentPet == null) return;
        var pet    = gp.petManager.currentPet;
        int barW   = 230;
        int barX   = px + 160;   // fixed column — all bars start here
        int startY = py + 96;
        int gap    = 46;

        drawStatBar(g2, hungerIcon,    "Hunger:",    pet.hunger,    new Color(200, 90,  20), px, startY,         barX, barW);
        drawStatBar(g2, happinessIcon, "Happiness:", pet.happiness, new Color(255, 210, 50), px, startY + gap,   barX, barW);
        drawStatBar(g2, energyIcon,    "Energy:",    pet.energy,    new Color(60,  185, 80), px, startY + gap*2, barX, barW);

        int divY = startY + gap * 3 - 4;
        drawDivider(g2, px, divY, pw);

        int statusY = divY + 38;
        g2.setFont(optionFont);
        g2.setColor(C_WHITE);
        g2.drawString("Status:", px, statusY);
        if (pet.sick) {
            g2.setColor(new Color(255, 85, 85));
            g2.drawString("Sick", px + 120, statusY);
        } else {
            g2.setColor(new Color(100, 230, 110));
            g2.drawString("Healthy", px + 120, statusY);
        }

        g2.setFont(smallFont);
        g2.setColor(C_DIM);
        g2.drawString("Press 4 to close", px, statusY + 44);
    }

    // ================================================================
    //  TITLE SCREEN
    // ================================================================

    public void drawTitleScreen(Graphics2D g2) {
        aa(g2);

        // tiled grass backdrop
        BufferedImage grass = null;
        try { grass = ImageIO.read(getClass().getResource("/tiles/grass.png")); }
        catch (IOException e) { e.printStackTrace(); }
        grass = UtilityTool.scaleImage(grass, gp.tileSize, gp.tileSize);
        for (int x = 0; x < gp.screenWidth;  x += gp.tileSize)
            for (int y = 0; y < gp.screenHeight; y += gp.tileSize)
                g2.drawImage(grass, x, y, null);
        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // title
        g2.setFont(titleFont);
        String title = "PetLife";
        FontMetrics fm = g2.getFontMetrics();
        int titleX = gp.screenWidth / 2 - fm.stringWidth(title) / 2;
        g2.setColor(Color.BLACK);
        g2.drawString(title, titleX + 4, 144);
        g2.setColor(C_WHITE);
        g2.drawString(title, titleX, 140);

        // subtitle
        g2.setFont(subtitleFont);
        String sub = "Choose Your Pet";
        fm = g2.getFontMetrics();
        g2.setColor(new Color(255, 255, 255, 200));
        g2.drawString(sub, gp.screenWidth / 2 - fm.stringWidth(sub) / 2, 196);

        // pet images
        BufferedImage dogImg = null, catImg = null, koalaImg = null;
        try {
            dogImg   = ImageIO.read(getClass().getResource("/pet/dog_down_1.png"));
            catImg   = ImageIO.read(getClass().getResource("/pet/cat_down_1.png"));
            koalaImg = ImageIO.read(getClass().getResource("/pet/koala_down_1.png"));
        } catch (IOException e) { e.printStackTrace(); }
        dogImg   = UtilityTool.scaleImage(dogImg,   300, 300);
        catImg   = UtilityTool.scaleImage(catImg,   300, 300);
        koalaImg = UtilityTool.scaleImage(koalaImg, 300, 300);

        // button positions
        dogX   = gp.screenWidth / 2 - buttonWidth - 30; dogY   = 300;
        catX   = dogX;                                   catY   = 368;
        koalaX = dogX;                                   koalaY = 436;

        // hover detection
        hoveredPet = -1;
        if (mouseX > dogX   && mouseX < dogX   + buttonWidth && mouseY > dogY   && mouseY < dogY   + buttonHeight) hoveredPet = 0;
        if (mouseX > catX   && mouseX < catX   + buttonWidth && mouseY > catY   && mouseY < catY   + buttonHeight) hoveredPet = 1;
        if (mouseX > koalaX && mouseX < koalaX + buttonWidth && mouseY > koalaY && mouseY < koalaY + buttonHeight) hoveredPet = 2;

        // preview panel
        int panelX = gp.screenWidth / 2 + 50;
        int panelY = 260;
        int panelW = 240;
        int panelH = 310;

        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 20, 20);
        g2.setColor(new Color(255, 255, 255, 55));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(panelX, panelY, panelW, panelH, 20, 20);

        // preview content
        BufferedImage preview = null;
        String previewName    = null;
        String[] stats        = new String[]{};

        if (hoveredPet == 0) { preview = dogImg;   previewName = "Dog";   stats = new String[]{"Loyal and energetic", "Needs lots of love",  "High happiness gain"}; }
        if (hoveredPet == 1) { preview = catImg;   previewName = "Cat";   stats = new String[]{"Independent",         "Gets hungry often",   "Moody personality"};   }
        if (hoveredPet == 2) { preview = koalaImg; previewName = "Koala"; stats = new String[]{"Very sleepy",         "Rests frequently",    "Low energy drain"};    }

        if (previewName != null) {
            g2.setFont(new Font("Georgia", Font.BOLD, 22));
            fm = g2.getFontMetrics();
            g2.setColor(C_WHITE);
            g2.drawString(previewName, panelX + panelW / 2 - fm.stringWidth(previewName) / 2, panelY + 26);
        }

        int imgSize = 130;
        g2.drawImage(preview, panelX + (panelW - imgSize) / 2, panelY + 34, imgSize, imgSize, null);

        g2.setFont(new Font("Verdana", Font.PLAIN, 14));
        fm = g2.getFontMetrics();
        int sy = panelY + 188;
        for (String s : stats) {
            g2.setColor(new Color(255, 255, 255, 210));
            g2.drawString(s, panelX + panelW / 2 - fm.stringWidth(s) / 2, sy);
            sy += 26;
        }

        // buttons
        drawButton(g2, dogX,   dogY,   "Dog",   hoveredPet == 0);
        drawButton(g2, catX,   catY,   "Cat",   hoveredPet == 1);
        drawButton(g2, koalaX, koalaY, "Koala", hoveredPet == 2);

        // name input
        int boxW = 270, boxH = 38;
        int boxX = gp.screenWidth / 2 - boxW / 2;
        int boxY = (int)(gp.screenHeight * 0.72);
        boolean ready = gp.petNameInput.trim().length() >= 2;

        g2.setFont(smallFont);
        fm = g2.getFontMetrics();
        String lbl = "Name your pet:";
        g2.setColor(C_WHITE);
        g2.drawString(lbl, gp.screenWidth / 2 - fm.stringWidth(lbl) / 2, boxY - 16);

        // input box — tints green border when ready
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRoundRect(boxX, boxY, boxW, boxH, 10, 10);
        g2.setColor(ready ? new Color(140, 230, 140) : C_WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(boxX, boxY, boxW, boxH, 10, 10);

        g2.setFont(optionFont);
        g2.setColor(C_WHITE);
        g2.drawString(gp.petNameInput + "_", boxX + 12, boxY + 28);

        // hint below box
        g2.setFont(new Font("Verdana", Font.PLAIN, 15));
        fm = g2.getFontMetrics();
        String hint = ready ? "Click a pet above to start!" : "(12 characters max)";
        g2.setColor(ready ? new Color(140, 230, 140) : new Color(220, 220, 220));
        g2.drawString(hint, gp.screenWidth / 2 - fm.stringWidth(hint) / 2, boxY + boxH + 22);
    }

    // ================================================================
    //  BUTTON (title screen)
    // ================================================================

    private void drawButton(Graphics2D g2, int x, int y, String text, boolean hovered) {
        g2.setColor(hovered ? new Color(255, 255, 255, 70) : new Color(255, 255, 255, 38));
        g2.fillRoundRect(x, y, buttonWidth, buttonHeight, 14, 14);
        g2.setColor(hovered ? C_WHITE : new Color(255, 255, 255, 160));
        g2.setStroke(new BasicStroke(hovered ? 3f : 1.5f));
        g2.drawRoundRect(x, y, buttonWidth, buttonHeight, 14, 14);
        g2.setFont(optionFont);
        FontMetrics fm = g2.getFontMetrics();
        g2.setColor(C_WHITE);
        g2.drawString(text, x + buttonWidth / 2 - fm.stringWidth(text) / 2, y + 33);
    }

    // ================================================================
    //  HUD
    // ================================================================

    public void drawHUD(Graphics2D g2) {
        aa(g2);

        // keybind list — top-left, tight spacing
        g2.setFont(smallFont);
        g2.setColor(C_WHITE);
        int kx = 16, ky = 36, ks = 26;
        g2.drawString("1  Wallet",    kx, ky);          ky += ks;
        g2.drawString("2  Inventory", kx, ky);          ky += ks;
        g2.drawString("3  Tasks",     kx, ky);          ky += ks;
        g2.drawString("4  Pet Stats", kx, ky);          ky += ks + 4;
        g2.setColor(new Color(255, 255, 255, 100));
        g2.setStroke(new BasicStroke(1));
        g2.drawLine(kx, ky - 2, kx + 148, ky - 2);
        g2.setColor(C_WHITE);                           ky += 6;
        g2.drawString("F  Feed",      kx, ky);          ky += ks;
        g2.drawString("M  Medicine",  kx, ky);          ky += ks;
        g2.drawString("P  Play",      kx, ky);          ky += ks;
        g2.drawString("↵  Interact",  kx, ky);

        // message popup — dark box, centered
        if (messageOn) {
            g2.setFont(popupFont);
            FontMetrics fm = g2.getFontMetrics();
            int mw  = fm.stringWidth(message) + 40;
            int mh  = 44;
            int mx  = gp.screenWidth  / 2 - mw / 2;
            int my  = 84;

            g2.setColor(new Color(0, 0, 0, 185));
            g2.fillRoundRect(mx, my, mw, mh, 12, 12);
            g2.setColor(new Color(255, 255, 255, 100));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(mx, my, mw, mh, 12, 12);
            g2.setColor(C_WHITE);
            g2.drawString(message, mx + 20, my + 30);

            messageCounter++;
            if (messageCounter > 120) { messageCounter = 0; messageOn = false; }
        }
    }

    // ================================================================
    //  INVENTORY
    // ================================================================

    public void drawInventory(Graphics2D g2) {
        aa(g2);
        drawPanel(g2);

        int px = gp.screenWidth  / 4 - 5 + 24;
        int py = gp.screenHeight / 4 + 5 + 24;
        int pw = gp.screenWidth  / 2 - 48;

        g2.setFont(menuFont);
        g2.setColor(C_WHITE);
        g2.drawString("Inventory", px, py + 40);

        drawDivider(g2, px, py + 52, pw);

        g2.setFont(optionFont);
        g2.setColor(C_WHITE);
        int y = py + 96;
        g2.drawString("Pet Food:  " + gp.inventoryManager.getItemCount("food"),     px, y); y += 40;
        g2.drawString("Medicine:  " + gp.inventoryManager.getItemCount("medicine"), px, y); y += 40;
        g2.drawString("Toys:      " + gp.inventoryManager.getItemCount("toy"),      px, y); y += 52;

        drawDivider(g2, px, y, pw);

        g2.setFont(smallFont);
        g2.setColor(C_DIM);
        g2.drawString("Press 2 to close", px, y + 28);
    }

    // ================================================================
    //  TASKS
    // ================================================================

    public void drawTasks(Graphics2D g2) {
        aa(g2);
        drawPanel(g2);

        int px = gp.screenWidth  / 4 - 5 + 24;
        int py = gp.screenHeight / 4 + 5 + 24;
        int pw = gp.screenWidth  / 2 - 48;

        g2.setFont(menuFont);
        g2.setColor(C_WHITE);
        g2.drawString("TODO List", px, py + 40);

        drawDivider(g2, px, py + 52, pw);

        int y   = py + 100;
        int gap = 40;

        for (Task t : gp.taskManager.tasks) {
            // checkbox
            int cbSz = 22;
            g2.setColor(C_WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(px, y - 18, cbSz, cbSz, 5, 5);

            if (t.completed) {
                g2.setColor(new Color(100, 220, 110));
                g2.setStroke(new BasicStroke(3));
                g2.drawLine(px + 4,  y - 5,  px + 9,  y);
                g2.drawLine(px + 9,  y,       px + 19, y - 12);
            }

            g2.setFont(optionFont);
            g2.setColor(t.completed ? new Color(170, 230, 170) : C_WHITE);
            g2.drawString(t.name, px + 34, y);

            y += gap;
        }

        y += 16;
        drawDivider(g2, px, y, pw);

        g2.setFont(smallFont);
        g2.setColor(C_DIM);
        g2.drawString("Press 3 to close", px, y + 28);
    }

    // ================================================================
    //  WALLET
    // ================================================================

    public void drawWallet(Graphics2D g2) {
        aa(g2);
        drawPanel(g2);

        int px = gp.screenWidth  / 4 - 5 + 24;
        int py = gp.screenHeight / 4 + 5 + 24;
        int pw = gp.screenWidth  / 2 - 48;

        // header
        g2.setFont(menuFont);
        g2.setColor(C_WHITE);
        g2.drawString("Wallet", px, py + 40);
        drawDivider(g2, px, py + 52, pw);

        // left column — balance + breakdown
        int lx = px;
        int y  = py + 96;

        // large balance
        g2.setFont(new Font("Georgia", Font.BOLD, 38));
        g2.setColor(C_WHITE);
        g2.drawString("$" + gp.wallet.balance, lx, y);

        g2.setFont(smallFont);
        g2.setColor(C_DIM);
        g2.drawString("current balance", lx, y + 22);

        y += 40;
        drawDivider(g2, lx, y, pw / 2 - 20);
        y += 30;

        g2.setFont(smallFont);
        g2.setColor(C_WHITE);
        drawWalletRow(g2, "Total Spent", "$" + gp.wallet.totalSpent, lx, y); y += 30;
        drawWalletRow(g2, "Food",        "$" + gp.wallet.foodCosts,  lx, y); y += 30;
        drawWalletRow(g2, "Vet",         "$" + gp.wallet.vetCosts,   lx, y); y += 30;
        drawWalletRow(g2, "Toys",        "$" + gp.wallet.toyCosts,   lx, y); y += 52;

        drawDivider(g2, lx, y, pw / 2 - 20);

        g2.setFont(smallFont);
        g2.setColor(C_DIM);
        g2.drawString("Press 1 to close", lx, y + 28);

        // right column — transaction history
        int rx  = px + pw / 2 + 10;
        int ty  = py + 75;

        // vertical divider
        g2.setColor(C_DARK);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(rx - 14, py + 56, rx - 14, py + (gp.screenHeight / 2) - 32);

        g2.setFont(new Font("Georgia", Font.BOLD, 22));
        g2.setColor(C_WHITE);
        g2.drawString("Transactions", rx, ty);
        ty += 12;
        drawDivider(g2, rx, ty, pw / 2 - 20);
        ty += 20;

        if (transactionScroll < 0) transactionScroll = 0;
        int maxScroll = Math.max(0, gp.wallet.history.size() - maxVisibleTransactions);
        if (transactionScroll > maxScroll) transactionScroll = maxScroll;

        int end = Math.min(transactionScroll + maxVisibleTransactions, gp.wallet.history.size());
        g2.setFont(new Font("Verdana", Font.PLAIN, 15));
        for (int i = transactionScroll; i < end; i++) {
            Transaction t  = gp.wallet.history.get(i);
            String sign    = t.amount > 0 ? "+" : "";
            g2.setColor(t.amount > 0 ? new Color(150, 230, 150) : new Color(230, 130, 130));
            g2.drawString(sign + "$" + t.amount, rx, ty);
            g2.setColor(C_WHITE);
            g2.drawString("  " + t.description, rx + 58, ty);
            ty += 24;
        }

        g2.setFont(new Font("Verdana", Font.PLAIN, 13));
        g2.setColor(C_DIM);
        g2.drawString("↑ ↓  scroll", rx, py + (gp.screenHeight / 2) - 16);
    }

    private void drawWalletRow(Graphics2D g2, String label, String value, int x, int y) {
        g2.setColor(C_DIM);
        g2.drawString(label + ":", x, y);
        FontMetrics fm = g2.getFontMetrics();
        g2.setColor(C_WHITE);
        g2.drawString(value, x + fm.stringWidth(label + ":") + 10, y);
    }
}
