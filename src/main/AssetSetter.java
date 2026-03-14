package main;

import object.OBJ_Bed;
import object.OBJ_Chest;
import entity.NPC_Merchant;
import entity.NPC_OldMan;
import entity.NPC_Veterinarian;

import quiz.NPC_QuizMaster;


public class AssetSetter {
	GamePanel gp;
	
	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}


	public void setNPC() {

		gp.npc[0] = new NPC_OldMan(gp);
		gp.npc[0].worldX = gp.tileSize * 32;
		gp.npc[0].worldY = gp.tileSize * 23;

		gp.npc[1] = new NPC_Merchant(gp);
		gp.npc[1].worldX = 20 * gp.tileSize;
		gp.npc[1].worldY = 40 * gp.tileSize;

		gp.npc[2] = new NPC_Veterinarian(gp);
		gp.npc[2].worldX = gp.tileSize * 20;
		gp.npc[2].worldY = gp.tileSize * 25;

		// QUIZ MASTER
		gp.npc[3] = new NPC_QuizMaster(gp);
		gp.npc[3].worldX = 37 * gp.tileSize;
		gp.npc[3].worldY = 23 * gp.tileSize;

	}
	
	public void setObject() {
		gp.obj[0] = new OBJ_Chest(gp);
		gp.obj[0].worldX = 12 * gp.tileSize;
		gp.obj[0].worldY = 10 * gp.tileSize;

		gp.obj[1] = new OBJ_Chest(gp);
		gp.obj[1].worldX = 51 * gp.tileSize;
		gp.obj[1].worldY = 15 * gp.tileSize;

		gp.obj[2] = new OBJ_Chest(gp);
		gp.obj[2].worldX = 20 * gp.tileSize;
		gp.obj[2].worldY = 41 * gp.tileSize;

		gp.obj[3] = new OBJ_Chest(gp);
		gp.obj[3].worldX = 50 * gp.tileSize;
		gp.obj[3].worldY = 15 * gp.tileSize;

		// BED (2x2)

		gp.obj[4] = new OBJ_Bed(gp);
		gp.obj[4].worldX = 30 * gp.tileSize;
		gp.obj[4].worldY = 10 * gp.tileSize;
		gp.obj[4].description = "topLeft";

		gp.obj[5] = new OBJ_Bed(gp);
		gp.obj[5].worldX = 31 * gp.tileSize;
		gp.obj[5].worldY = 10 * gp.tileSize;
		gp.obj[5].flipX = true;
		gp.obj[5].description = "topRight";

		gp.obj[6] = new OBJ_Bed(gp);
		gp.obj[6].worldX = 30 * gp.tileSize;
		gp.obj[6].worldY = 11 * gp.tileSize;
		gp.obj[6].flipY = true;
		gp.obj[6].description = "bottomLeft";

		gp.obj[7] = new OBJ_Bed(gp);
		gp.obj[7].worldX = 31 * gp.tileSize;
		gp.obj[7].worldY = 11 * gp.tileSize;
		gp.obj[7].flipX = true;
		gp.obj[7].flipY = true;
		gp.obj[7].description = "bottomRight";
	}
}
