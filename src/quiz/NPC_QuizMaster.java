package quiz;

import entity.NPC;
import main.GamePanel;

public class NPC_QuizMaster extends NPC {

    public NPC_QuizMaster(GamePanel gp) {
        super(gp, "Quizzard");
        down1 = loadSprite("/npc/quizzard.png", gp.tileSize, gp.tileSize);
    }

    @Override
    public void interact() {
        gp.quizManager.startQuiz();
    }
}
