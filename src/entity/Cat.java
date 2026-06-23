package entity;

import java.util.Random;

import main.GamePanel;

public class Cat extends Pet {

    Random rand = new Random();

    public Cat(GamePanel gp) {
        super(gp);

        hungerDecay = 1;
        happinessDecay = 2;
        energyDecay = 1;
    }

    @Override
    public void play() {

        if(rand.nextInt(100) < 30) {
            annoyed = true;
            annoyedTimer = 180;
            gp.ui.showMessage(name + " doesn't want to play right now!");
            return;
        }

        super.play();
    }
}