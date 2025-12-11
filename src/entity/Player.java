package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public int hasKey = 0;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
        screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);

        // hardcoded collision rectangle dimensions
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";
        getPlayerImage();
    }

    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/Walking_sprites/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/Walking_sprites/boy_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/Walking_sprites/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/Walking_sprites/boy_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/Walking_sprites/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/Walking_sprites/boy_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/Walking_sprites/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/Walking_sprites/boy_right_2.png"));

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void update() {

        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            if (keyH.upPressed) {
                direction = "up";
            }
            if (keyH.downPressed) {
                direction = "down";
            }
            if (keyH.leftPressed) {
                direction = "left";
            }
            if (keyH.rightPressed) {
                direction = "right";
            }

            // CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObj(objIndex);

            // IF COLLISION IS FALSE THEN PLAYER CAN MOVE
            if (!collisionOn) {
                switch (direction) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }


            spriteCounter++;

            if (spriteCounter > 12) {
                spriteNum = spriteNum == 1 ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                break;
        }

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }

    public void pickUpObj(int idx){
        if(idx != 999){
            String objName = gp.obj[idx].name;

            switch(objName){
                case "Key":
                    gp.obj[idx] = null;
                    hasKey++;
                    gp.playSE(1);
                    gp.ui.showMessage("obtained key!!!");

                    break;
                case "Door":
                    if(hasKey > 0){
                        hasKey--;
                        gp.obj[idx] = null;
                        gp.playSE(3);
                        gp.ui.showMessage("opened Door!!!");
                    }
                    else{
                        gp.ui.showMessage("You need a key to open door!");
                    }
                    break;
                case "Boots":
                    speed += 1;
                    gp.obj[idx] = null;
                    gp.playSE(2);
                    gp.ui.showMessage("obtained Boots!!!");
                    break;
                case "Chest":
                    gp.ui.gameFinished = true;
                    gp.stopMusic();
                    gp.playSE(4);
                    break;
            }
        }
    }
}
