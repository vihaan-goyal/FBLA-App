package entity;

import java.awt.Graphics2D;
import main.GamePanel;

/**
 * Abstract base class for all NPCs.
 * Provides shared image loading, dialogue, and drawing behaviour.
 */
public abstract class NPC extends Entity {

    protected GamePanel gp;
    protected String speakerName;

    public NPC(GamePanel gp, String speakerName) {
        this.gp          = gp;
        this.speakerName = speakerName;
        direction        = "down";
        speed            = 0;
    }

    /** Start a dialogue with this NPC's speaker name. */
    protected void say(String... lines) {
        gp.ui.speaker = speakerName;
        gp.ui.startDialogue(lines);
    }

    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        super.draw(g2, gp);
        drawLabel(g2, gp, speakerName);
    }
}
