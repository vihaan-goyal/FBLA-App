package entity;

import main.GamePanel;

public class Koala extends Pet {

    public Koala(GamePanel gp) {
        super(gp);

        hungerDecay = 1;
        happinessDecay = 1;
        energyDecay = 2;
    }

    @Override
    public void rest() {

        energy += 40;

        if(energy > 100) energy = 100;

    }
}