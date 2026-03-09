package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseAdapter {

    GamePanel gp;

    public MouseHandler(GamePanel gp){
        this.gp = gp;
    }

    public void mousePressed(MouseEvent e){

        int mouseX = e.getX();
        int mouseY = e.getY();

        if(gp.gameState == gp.TITLE_STATE){

            // dog button
            if(mouseX > gp.ui.dogX && mouseX < gp.ui.dogX + gp.ui.buttonWidth &&
               mouseY > gp.ui.dogY && mouseY < gp.ui.dogY + gp.ui.buttonHeight){

                gp.petManager.createDog();
                gp.pet = gp.petManager.currentPet;
                gp.pet.name = gp.petNameInput;
                gp.pet.petType = "dog";
                gp.pet.getPetImage();


                gp.gameState = gp.PLAY_STATE;
            }

            // cat button
            if(mouseX > gp.ui.catX && mouseX < gp.ui.catX + gp.ui.buttonWidth &&
               mouseY > gp.ui.catY && mouseY < gp.ui.catY + gp.ui.buttonHeight){

                gp.petManager.createCat();
                gp.pet = gp.petManager.currentPet;
                gp.pet.name = gp.petNameInput;
                gp.pet.petType = "cat";
                gp.pet.getPetImage();


                gp.gameState = gp.PLAY_STATE;
            }

            // koala button
            if(mouseX > gp.ui.koalaX && mouseX < gp.ui.koalaX + gp.ui.buttonWidth &&
               mouseY > gp.ui.koalaY && mouseY < gp.ui.koalaY + gp.ui.buttonHeight){

                gp.petManager.createKoala();
                gp.pet = gp.petManager.currentPet;
                gp.pet.name = gp.petNameInput;
                gp.pet.petType = "koala";
                gp.pet.getPetImage();

                gp.gameState = gp.PLAY_STATE;
            }
        }
    }
}