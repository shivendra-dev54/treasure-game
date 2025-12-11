package main;

import entity.Entity;

public class CollisionChecker {
    GamePanel gp;

    CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity e) {

        int entityLeftWorldX = e.worldX + e.solidArea.x;
        int entityRightWorldX = e.worldX + e.solidArea.x + e.solidArea.width;
        int entityTopWorldY = e.worldY + e.solidArea.y;
        int entityBottomWorldY = e.worldY + e.solidArea.y + e.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        switch (e.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - e.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    e.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + e.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    e.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - e.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    e.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + e.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    e.collisionOn = true;
                }
                break;
        }

    }

    public int checkObject(Entity e, boolean player){
        int index = 999;

        for(int i = 0; i < gp.obj.length; i++){
            if(gp.obj[i] != null){
                // get entity solid area position
                e.solidArea.x = e.worldX + e.solidArea.x;
                e.solidArea.y = e.worldY + e.solidArea.y;

                // get object's solid area position
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

                switch(e.direction){
                    case "up":
                        e.solidArea.y -= e.speed;
                        if(e.solidArea.intersects(gp.obj[i].solidArea)){
                            if(gp.obj[i].collision){
                                e.collisionOn = true;
                            }
                            if(player){
                                index = i;
                            }
                        }
                        break;
                    case "down":
                        e.solidArea.y += e.speed;
                        if(e.solidArea.intersects(gp.obj[i].solidArea)){
                            if(gp.obj[i].collision){
                                e.collisionOn = true;
                            }
                            if(player){
                                index = i;
                            }
                        }
                        break;
                    case"left":
                        e.solidArea.x -= e.speed;
                        if(e.solidArea.intersects(gp.obj[i].solidArea)){
                            if(gp.obj[i].collision){
                                e.collisionOn = true;
                            }
                            if(player){
                                index = i;
                            }
                        }
                        break;
                    case "right":
                        e.solidArea.x += e.speed;
                        if(e.solidArea.intersects(gp.obj[i].solidArea)){
                            if(gp.obj[i].collision){
                                e.collisionOn = true;
                            }
                            if(player){
                                index = i;
                            }
                        }
                        break;
                }
                e.solidArea.x = e.solidAreaDefaultX;
                e.solidArea.y = e.solidAreaDefaultY;

                gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
            }
        }

        return index;
    }

}
