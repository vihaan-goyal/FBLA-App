package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean feedPressed, playPressed;
    public boolean onePressed, twoPressed, threePressed;
    public boolean walletPressed;
    public boolean enterPressed;
    public boolean taskPressed;
    public boolean inventoryPressed;
    public boolean petStatsPressed;

    GamePanel gp;

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }

    public void keyTyped(KeyEvent e){

        if(gp.gameState == gp.TITLE_STATE){

            char c = e.getKeyChar();

            if(Character.isLetterOrDigit(c) && gp.petNameInput.length() < 12){
                gp.petNameInput += c;
            }

            if(c == '\b' && gp.petNameInput.length() > 0){
                gp.petNameInput =
                    gp.petNameInput.substring(0, gp.petNameInput.length()-1);
            }
        }
        if(gp.ui.typingMode){

            char c = e.getKeyChar();

            if(Character.isLetterOrDigit(c) || c == ' '){
                gp.ui.currentInput += c;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if(code == KeyEvent.VK_BACK_SPACE){

            if(gp.ui.typingMode && gp.ui.currentInput.length() > 0){

                gp.ui.currentInput =
                    gp.ui.currentInput.substring(0, gp.ui.currentInput.length() - 1);

            }
        }

        if(code == KeyEvent.VK_ENTER) {

            // submit answer first
            if(gp.ui.typingMode){

                gp.quizManager.submitAnswer(gp.ui.currentInput);

                gp.ui.currentInput = "";
                gp.ui.typingMode = false;

                return;
            }

            // advance dialogue second
            if(gp.ui.dialogueOn){

                gp.ui.dialogueIndex++;

                if(gp.ui.dialogueIndex >= gp.ui.dialogueLines.length){
                    gp.ui.dialogueOn = false;

                    if(gp.quizManager.quizState == 1){
                        gp.quizManager.beginTyping();
                    }
                }

                return;
            }

            enterPressed = true;
        }
        // block movement while typing
        if(gp.ui.typingMode){
            return;
        }
        // block menus and movement during dialogue
        if(gp.ui.dialogueOn){
            return;
        }

        if (code == KeyEvent.VK_W) upPressed = true;
        if (code == KeyEvent.VK_S) downPressed = true;
        if (code == KeyEvent.VK_A) leftPressed = true;
        if (code == KeyEvent.VK_D) rightPressed = true;

        if (code == KeyEvent.VK_F) feedPressed = true;
        if (code == KeyEvent.VK_P) playPressed = true;
        if(code == KeyEvent.VK_M) gp.player.useMedicine();

        if(code == KeyEvent.VK_1){

            walletPressed = true;

            gp.inventoryOpen = false;
            gp.showTasks = false;
            gp.petStatsOpen = false;
        }

        if(code == KeyEvent.VK_2){

            inventoryPressed = true;

            gp.showWallet = false;
            gp.showTasks = false;
            gp.petStatsOpen = false;
        }

        if(code == KeyEvent.VK_3){

            taskPressed = true;

            gp.showWallet = false;
            gp.inventoryOpen = false;
            gp.petStatsOpen = false;
        }

        if(code == KeyEvent.VK_4){

            petStatsPressed = true;

            gp.showWallet = false;
            gp.inventoryOpen = false;
            gp.showTasks = false;
        }


    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) upPressed = false;
        if (code == KeyEvent.VK_S) downPressed = false;
        if (code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_D) rightPressed = false;

        if (code == KeyEvent.VK_F) feedPressed = false;
        if (code == KeyEvent.VK_P) playPressed = false;
        if(code == KeyEvent.VK_ENTER) enterPressed = false;

        if(code == KeyEvent.VK_1) walletPressed = false;
        if(code == KeyEvent.VK_2) inventoryPressed = false;
        if(code == KeyEvent.VK_3) taskPressed = false;
        if(code == KeyEvent.VK_4) petStatsPressed = false;

        
            
    }

    
}