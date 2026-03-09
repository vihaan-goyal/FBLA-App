package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean feedPressed, playPressed, restPressed;
    public boolean onePressed, twoPressed, threePressed;
    public boolean walletPressed;

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
    }

    
}