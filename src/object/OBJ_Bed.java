package object;

import java.io.IOException;
import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class OBJ_Bed extends SuperObject {

    GamePanel gp;

    public OBJ_Bed(GamePanel gp){
        this.gp = gp;

        name = "Bed";
        description = "";

        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/bed.png"));
            image = UtilityTool.scaleImage(image, gp.tileSize, gp.tileSize);
        }catch(IOException e){
            e.printStackTrace();
        }

        collision = false; // player can walk next to it
    }
}