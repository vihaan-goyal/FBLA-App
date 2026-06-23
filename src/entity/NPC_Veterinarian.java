package entity;

import main.GamePanel;

public class NPC_Veterinarian extends NPC {

    public NPC_Veterinarian(GamePanel gp) {
        super(gp, "Veterinarian");
        down1 = loadSprite("/npc/vet_down_1.png", gp.tileSize, gp.tileSize);
    }

    @Override
    public void interact() {
        NPC_OldMan oldMan = (NPC_OldMan) gp.npc[0];

        // Free vaccination at questStage 3
        if (oldMan.questStage == 3) {
            gp.petManager.currentPet.isVaccinated = true;
            oldMan.questStage = 4;
            say("Hello!", "I gave your pet some shots!", "It should be fully vaccinated now!");
            return;
        }

        if (gp.wallet.spend("Pet Medicine", 15)) {
            gp.wallet.vetCosts += 15;
            gp.inventoryManager.addItem("medicine", 1);
            say("Hello!", "Here is some medicine.", "That will be $15.");
        } else {
            say("Hello!", "Medicine costs $15.", "Come back when you have enough money.");
        }
    }
}
