package entity;

import main.GamePanel;

public class Dog extends Pet {

    public Dog(GamePanel gp) {
        super(gp);

        hungerDecay = 2;
        happinessDecay = 1;
        energyDecay = 1;
    }

    @Override
    public void play() {

        happiness += 25;
        energy -= 10;
        hunger -= 5;

        clampStats();
    }
}