package quiz;

import entity.Entity;
import main.GamePanel;
import main.UtilityTool;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class NPC_QuizMaster extends Entity {

    GamePanel gp;

    public NPC_QuizMaster(GamePanel gp){
        this.gp = gp;

        direction = "down";
        speed = 0;

        getImage();
    }

    public void getImage(){

        try{

            BufferedImage image =
                ImageIO.read(getClass().getResource("/npc/quizzard.png"));

            UtilityTool uTool = new UtilityTool();
            down1 = uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void interact(){
        gp.quizManager.startQuiz();
    }

    public void draw(Graphics2D g2){

        BufferedImage image = down1;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        g2.drawImage(image, screenX, screenY, null);
    }
}