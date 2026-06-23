package entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.UtilityTool;

public class Entity {

    public int worldX, worldY;
    public int speed;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;

    // ----------------------------------------------------------------
    //  Shared helpers
    // ----------------------------------------------------------------

    /** Load, scale, and return a sprite from the given resource path. */
    protected BufferedImage loadSprite(String path, int w, int h) {
        try {
            BufferedImage img = ImageIO.read(getClass().getResource(path));
            return UtilityTool.scaleImage(img, w, h);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** Draw a white bold label centered above this entity. */
    protected void drawLabel(Graphics2D g2, GamePanel gp, String label) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        int lx = screenX + gp.tileSize / 2 - fm.stringWidth(label) / 2;
        g2.drawString(label, lx, screenY - 5);
    }

    // ----------------------------------------------------------------
    //  Default draw — camera-corrected, sized to tileSize
    // ----------------------------------------------------------------

    public void draw(Graphics2D g2, GamePanel gp) {
        BufferedImage image;
        if      (direction == null)          image = down1;
        else if (direction.equals("up"))     image = (spriteNum == 1) ? up1    : up2;
        else if (direction.equals("down"))   image = (spriteNum == 1) ? down1  : down2;
        else if (direction.equals("left"))   image = (spriteNum == 1) ? left1  : left2;
        else if (direction.equals("right"))  image = (spriteNum == 1) ? right1 : right2;
        else                                 image = down1;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }

    public void interact() {}
}
