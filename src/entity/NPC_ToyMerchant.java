package entity;

import main.GamePanel;

public class NPC_ToyMerchant extends NPC {

    private boolean hasSpoken = false;

    public NPC_ToyMerchant(GamePanel gp) {
        super(gp, "Toy Merchant");
        down1 = loadSprite("/npc/toyMerchant.png", gp.tileSize, gp.tileSize);
    }

    @Override
    public void interact() {
        if (!hasSpoken) {
            hasSpoken = true;
            say("Welcome to the toy store!", "Fun toys for only $10.");
            return;
        }

        if (gp.wallet.spend("Toy", 10)) {
            gp.wallet.toyCosts += 10;
            gp.inventoryManager.addItem("toy", 1);
            say("Here you go, one fun toy!");
        } else {
            say("Toys cost $10.", "Come back when you have enough money!");
        }
    }
}
