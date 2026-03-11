package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean feedPressed, playPressed, restPressed;
    public boolean onePressed, twoPressed, threePressed;
    public boolean walletPressed;
    public boolean enterPressed;
    public boolean taskPressed;

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
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) upPressed = true;
        if (code == KeyEvent.VK_S) downPressed = true;
        if (code == KeyEvent.VK_A) leftPressed = true;
        if (code == KeyEvent.VK_D) rightPressed = true;

        if (code == KeyEvent.VK_F) feedPressed = true;
        if (code == KeyEvent.VK_P) playPressed = true;
        if (code == KeyEvent.VK_R) restPressed = true;

        if (code == KeyEvent.VK_1) onePressed = true;
        if (code == KeyEvent.VK_2) twoPressed = true;
        if (code == KeyEvent.VK_3) threePressed = true;

        if(code == KeyEvent.VK_I) walletPressed = true;

        if(code == KeyEvent.VK_T) taskPressed = true;

        if(code == KeyEvent.VK_ENTER) {

            // advance dialogue if active
            if(gp.ui.dialogueOn){
                gp.ui.dialogueIndex++;

                if(gp.ui.dialogueIndex >= gp.ui.dialogueLines.length){
                    gp.ui.dialogueOn = false;
                }

                return;
            }

            enterPressed = true;
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
        if (code == KeyEvent.VK_R) restPressed = false;

        if (code == KeyEvent.VK_1) onePressed = false;
        if (code == KeyEvent.VK_2) twoPressed = false;
        if (code == KeyEvent.VK_3) threePressed = false;

        if(code == KeyEvent.VK_I) walletPressed = false;

        if(code == KeyEvent.VK_ENTER) enterPressed = false;

        if(code == KeyEvent.VK_T) taskPressed = false;
            
    }

    
}