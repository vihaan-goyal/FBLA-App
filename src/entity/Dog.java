package entity;

import main.GamePanel;

public class Dog extends Pet {

    public Dog(GamePanel gp) {
        super(gp);

        hungerDecay = 2;
        happinessDecay = 3;
        energyDecay = 3;
    }

    @Override
    public void play() {

        happiness += 10;
        energy -= 10;
        hunger -= 5;

        clampStats();
    }
}