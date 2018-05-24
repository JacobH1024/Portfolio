/*
This class spawns after a brick is destroyed. It then follows a player to try and destory them
The player must shoot to destory the enemy 
 */

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Enemy extends GameObject{
private Paddle player;
private AppWindow window;
private double speed = 20;
private int radius = 0;
    public Enemy(int x,int y, Handler handler){
        super(x,y, ID.enemy);
        player = handler.player;
        window = handler.window;
        speed = (37*handler.window.height/10000);
        radius = Brick.brickHeight/2;
    }

    @Override
    public double[] collide(GameObject obj, Handler handler){
        handler.removeObject(this);
        handler.removeObject(obj);
        return new double[]{0,0};
    }

    @Override
    public void tick(){
        velX = player.getX()-x;
        velY = player.getY()-y;
        double mag = Math.sqrt(velX*velX+velY*velY);
        velX=speed*velX/mag;
        velY=speed*velY/mag;
        x+=velX;
        y+=velY;
    }
    @Override
    public void render(Graphics g){
        g.setColor(Color.red);
        g.fillOval((int)(x-radius),(int)(y-radius),2*radius,2*radius);
    }

    @Override
    public Area getCollisionBounds(){

        return new Area(
                //new Rectangle((int)x,(int)y,ballRadius*10/8, ballRadius*10/8));
                new Ellipse2D.Double((int)x-radius,(int)y-radius,2*radius, 2*radius));
    }

}
