package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {
	GamePanel gp;
	public Tile[] tile;
	public int mapTileNum[][];
	
	public TileManager(GamePanel gp) {
		this.gp = gp;
		tile = new Tile[30];
		mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
		
		GetTileImage();
		loadMap("/maps/finalMap.txt");
	}
	
	public void GetTileImage() {
		setup(0, "grass", false); //done
		setup(1, "wall", true); //done
		setup(2, "water", true); //done
		setup(3, "lantern", true); //done
		setup(4, "tree", true); //done
		setup(5, "sand", false); //done
		setup(6, "bush", true); //done
		setup(7, "woodBricks", false); //done
		setup(8, "sign", true);
		setup(9, "rock", true); //done
		setup(10, "stoneBricks", true); //done
		setup(14, "miniHouse", true); //done
		setup(15, "redFlower", false); //done
		setup(16, "purpleFlower", false); //done
		setup(17, "pinkFlower", false); //done
		setup(18, "whiteFlower", false); //done
		setup(19, "tent", true); //done
		setup(20, "waterBowl", true); //done
		setup(21, "foodBowl", true); //done

	}
	
	public void setup(int index, String imageName, boolean collision) {
		UtilityTool uTool = new UtilityTool();
		
		try {
			tile[index] = new Tile();
			
			tile[index].image = ImageIO.read(getClass().getResource("/tiles/" + imageName + ".png"));
			tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
			tile[index].collision = collision;
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadMap(String filePath) {
		try {
			InputStream is = getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int row = 0;
			
			while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
				String line = br.readLine();
				
				String numbers[] = line.split(" ");
				while(col < gp.maxWorldCol) {
					int num = Integer.parseInt(numbers[col]);
					mapTileNum[col][row] = num;
					col++;
				}
				
				if(col == gp.maxWorldCol) {
					col = 0;
					row++;
				}
			}
			
			br.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) {
		int worldCol = 0;
		int worldRow = 0;
		
		while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
			int tileNum = mapTileNum[worldCol][worldRow];
			
			int worldX = worldCol * gp.tileSize;
			int worldY = worldRow * gp.tileSize;
			int screenX = worldX - gp.player.worldX + gp.player.screenX;
			int screenY = worldY - gp.player.worldY + gp.player.screenY;
			
			if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
			   worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
			   worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
			   worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
				g2.drawImage(tile[tileNum].image, screenX, screenY, null);
			}
			worldCol++;
			
			if(worldCol == gp.maxWorldCol) {
				worldCol = 0;
				worldRow++;
			}
		}
	}
}