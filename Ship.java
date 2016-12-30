
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.math.*;

public class Ship implements Runnable {
    public int shipX;
    public int shipY;
    private int width;
    public int deltaX = 0;
    public int deltaY = 0;
    private boolean shipSpawned;
    public BufferedImage myPicture;
    public int speed;

    public ArrayList<BufferedImage> shipPics;
    public double newx;
    public double newy;



    public Ship(int x, int y){
        this.shipX = x;
        this.shipY = y;
        this.width = 100;
        this.shipSpawned = true;
        this.speed = 1;

        shipPics = new ArrayList<>();

    }

    public void shipStop()
    {
        shipSpawned = false;
    }

    public void run()
    {
        shipSpawned = true;
        while (shipSpawned)
        {
            try {
                Thread.sleep(10);
            }
            catch(InterruptedException e){}

            double oldx = this.getShipX();
            double oldy = this.getShipY();
            int newx = (int) oldx + deltaX;
            int newy = (int) oldy + deltaY;

            this.setShipX(newx);
            this.setShipY(newy);

        }
    }
    public void setShipX(int x){
        this.shipX = x;
    }

    public void setShipY(int y)
    {
        this.shipY = y;
    }
    public int getShipX() {
        return this.shipX;
    }

    public int getShipY() {
        return this.shipY;
    }

    public void fillImageArray()
    {
        try {
            shipPics = new ArrayList<>();
            for (int i = 0; i <= 15; i++ ) {
                BufferedImage myPicture = ImageIO.read(new File("RocketShipFlame" + i + ".png"));
                shipPics.add(myPicture);
            }

        }
        catch (IOException ex)
        {
            System.out.println("File not Found");
        }
    }

    public BufferedImage getImage(int i)
    {
        try {
            myPicture = this.shipPics.get(i);

        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            System.out.println("Index out of bounds");
        }
        return myPicture;
    }

    public void increaseSpeed(int i)
    {
        this.speed = this.speed + i;
    }
    public void decreaseSpeed(int i)
    {
        this.speed = this.speed - i;
        if (this.speed <= 0)
        {
            this.speed = 1;
        }
    }

    public void nullShip(Ship s)
    {
        this.shipX = 100;
        this.shipY = 100;
        this.width = 0;
        this.shipSpawned = false;
        this.speed = 0;
    }
    public void getDirection(int i)
    {
        switch(i)
        {
            case 0: deltaX = 0;
                deltaY = (this.speed) * -1;
                break;
            case 1: deltaX = (int)Math.round((this.speed) * 0.40 + 1);
                deltaY = (int)Math.round((this.speed) * 0.60 + 1) * -1;
                break;
            case 2: deltaX = (int)Math.round((this.speed) * 0.50 + 1);
                deltaY = (int)Math.round((this.speed) * 0.50 + 1) * -1;
                break;
            case 3: deltaX = (int)Math.round((this.speed) * 0.60 + 1);
                deltaY = (int)Math.round((this.speed) * 0.40 + 1) * -1;
                break;
            case 4: deltaX = this.speed;
                deltaY = 0;
                break;
            case 5: deltaX = (int)Math.round((this.speed) * 0.75 + 1);
                deltaY = (int)Math.round((this.speed) * 0.25 + 1);
                break;
            case 6: deltaX = (int)Math.round((this.speed) * 0.50 + 1);
                deltaY = (int)Math.round(((this.speed) * 0.50 + 1));
                break;
            case 7: deltaX = (int)Math.round((this.speed) * 0.25 + 1);
                deltaY = (int)Math.round(((this.speed) * 0.75 + 1));
                break;
            case 8: deltaX = 0;
                deltaY = this.speed;
                break;
            case 9: deltaX = (int)Math.round(((this.speed) * 0.25) + 1) * -1;
                deltaY = (int)Math.round(((this.speed) * 0.75) + 1);
                break;
            case 10:deltaX = (int)Math.round(((this.speed) * 0.50) + 1) * -1;
                deltaY = (int)Math.round(((this.speed) * 0.50) + 1);
                break;
            case 11:deltaX = (int)Math.round(((this.speed) * 0.75) + 1) * -1;
                deltaY = (int)Math.round(((this.speed) * 0.25) + 1);
                break;
            case 12: deltaX = (this.speed) * -1;
                deltaY = 0;
                break;
            case 13: deltaX = (int)Math.round(((this.speed) * 0.75 + 1)) * -1;
                deltaY = (int)Math.round((this.speed) * 0.25 + 1) * -1;
                break;
            case 14: deltaX = (int)Math.round(((this.speed) * 0.50)  + 1) * -1;
                deltaY = (int)Math.round(((this.speed) * 0.50)  + 1) * -1;
                break;
            case 15:deltaX = (int)Math.round(((this.speed) * 0.25) + 1) * -1;
                deltaY = (int)Math.round((this.speed) * 0.75 + 1) * -1;
                break;
        }

    }

}
