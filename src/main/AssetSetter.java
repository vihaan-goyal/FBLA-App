package main;

import object.OBJ_Chest;
import entity.NPC_OldMan;


public class AssetSetter {
	GamePanel gp;
	
	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}


	public void setNPC() {

		gp.npc[0] = new NPC_OldMan(gp);
		gp.npc[0].worldX = gp.tileSize * 21;
		gp.npc[0].worldY = gp.tileSize * 21;

	}
	
	public void setObject() {
		gp.obj[0] = new OBJ_Chest(gp);
		gp.obj[0].worldX = 12 * gp.tileSize;
		gp.obj[0].worldY = 10 * gp.tileSize;

		gp.obj[1] = new OBJ_Chest(gp);
		gp.obj[1].worldX = 51 * gp.tileSize;
		gp.obj[1].worldY = 15 * gp.tileSize;

		gp.obj[2] = new OBJ_Chest(gp);
		gp.obj[2].worldX = 50 * gp.tileSize;
		gp.obj[2].worldY = 40 * gp.tileSize;

		gp.obj[3] = new OBJ_Chest(gp);
		gp.obj[3].worldX = 50 * gp.tileSize;
		gp.obj[3].worldY = 15 * gp.tileSize;
	}
}
