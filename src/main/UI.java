package main;

import object.OBJ_Key;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class UI {
    GamePanel gp;
    Font arial20, arial80B;
    BufferedImage key_img;

    public boolean messageOn = false;
    public String message = "";
    public int messageCounter = 0;
    public boolean gameFinished = false;
    int fWindow = 0;

    Double playTime = 0.0;
    DecimalFormat df = new DecimalFormat("#0.00");

    public UI(GamePanel gp) {
        this.gp = gp;
        arial20 = new Font("Arial", Font.PLAIN, 20);
        arial80B = new Font("Arial", Font.BOLD, 80);
        OBJ_Key key = new OBJ_Key();
        key_img = key.image;
    }

    public void showMessage(String msg) {
        this.message = msg;
        this.messageOn = true;
    }

    public void draw(Graphics2D g2) {

        // Game finishing
        if (gameFinished) {
            // small message above
            g2.setFont(arial20);
            g2.setColor(Color.white);
            String text = "You found the treasure!!!";

            FontMetrics fmSmall = g2.getFontMetrics();
            int textWidth = fmSmall.stringWidth(text);
            int x = (gp.screenWidth / 2) - (textWidth / 2);
            int y = (gp.screenHeight / 2) - (gp.tileSize * 3) + fmSmall.getAscent() / 2;
            g2.drawString(text, x, y);

            // big congratulations
            g2.setFont(arial80B);
            g2.setColor(Color.YELLOW);
            text = "Congratulations!";

            FontMetrics fmBig = g2.getFontMetrics();
            textWidth = fmBig.stringWidth(text);
            x = (gp.screenWidth / 2) - (textWidth / 2);
            // place vertically centered; use ascent to position baseline correctly
            y = (gp.screenHeight / 2) + (gp.tileSize * 2) + fmBig.getAscent() / 2;
            g2.drawString(text, x, y);

            fWindow++;

            if(fWindow > 60){
                gp.gameThread = null;
            }

            return;
        }

        g2.setFont(arial20);
        g2.setColor(Color.white);

        // draw key icon and key count
        g2.drawImage(key_img, gp.tileSize / 4, gp.tileSize / 4, null);
        // If you want to show number of keys, better to convert to int/string explicitly:
        g2.drawString(String.valueOf(gp.player.hasKey), 35, 27);

        // TIME
        playTime += (double) 1 / 60;
        g2.drawString(String.valueOf(df.format(playTime)), (gp.tileSize * 15) - (gp.tileSize / 2), 27);

        // MESSAGE
        if (messageOn) {
            g2.drawString(message, 15, 47);
            messageCounter++;
            if (messageCounter > 90) {
                messageCounter = 0;
                messageOn = false;
            }
        }
    }

}
