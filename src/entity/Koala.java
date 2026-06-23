package entity;

import main.GamePanel;

public class Koala extends Pet {

    public Koala(GamePanel gp) {
        super(gp);

        hungerDecay = 3;
        happinessDecay = 3;
        energyDecay = 4;
    }

    @Override
    public void rest() {

        energy += 40;

        if(energy > 100) energy = 100;

    }
}