package entity;

import main.GamePanel;

public class Dog extends Pet {

    public Dog(GamePanel gp) {
        super(gp);

        hungerDecay = 1;
        happinessDecay = 2;
        energyDecay = 1;
    }

    @Override
    public void play() {

        happiness += 10;
        energy -= 10;
        hunger -= 5;

        clampStats();
    }
}