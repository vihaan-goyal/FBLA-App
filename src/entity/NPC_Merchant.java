package entity;

import main.GamePanel;

public class NPC_Merchant extends NPC {

    private boolean hasSpoken = false;

    public NPC_Merchant(GamePanel gp) {
        super(gp, "Merchant");
        down1 = loadSprite("/npc/foodMerchant.png", gp.tileSize, gp.tileSize);
    }

    @Override
    public void interact() {
        if (!hasSpoken) {
            hasSpoken = true;
            say(
                "Welcome to my pet food shop!",
                "Fresh pet food for only $10.",
                "Come back anytime if your pet gets hungry."
            );
            return;
        }

        if (gp.wallet.spend("Pet Food", 10)) {
            gp.wallet.foodCosts += 10;
            gp.inventoryManager.addItem("food", 1);
            say("Here you go, one fresh bag of pet food!");
        } else {
            say("Pet food costs $10.", "Come back when you have enough money.");
        }
    }
}
