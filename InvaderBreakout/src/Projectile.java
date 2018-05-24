/**
 * Created by jacob on 3/9/2017.
 * This class is a new addition along with the enemy
 * Now by clicking the mouse the player is able to shoot a projectile to destroy enemies
 */
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;


public class Projectile extends GameObject {
public Handler handler;
public int radius = 10;
    public Projectile(int x,int y,double velX,double velY,Handler handler){
        super(x,y,ID.special);
        this.velX = velX;
        this.velY = velY;
        this.handler = handler;
    }

    public void collision() {
        for(int i = 0; i < handler.object.size(); i++){
            GameObject tempObj = handler.object.get(i);
            if (tempObj.id == ID.enemy){
                Area intersection = getCollisionBounds();
                intersection.intersect(tempObj.getCollisionBounds());
                if(!intersection.isEmpty()){
                    tempObj.collide(this,handler);
                }
            }
        }
    }

    @Override
    public void tick(){
        x+=velX;
        y+=velY;
        try{
            collision();
        }catch(java.lang.NullPointerException e ){
            System.out.println("Error");
        }
    }
    @Override
    public void render(Graphics g){
        g.setColor(Color.cyan);
        g.fillOval((int)(x-radius),(int)(y-radius),2*radius,2*radius);
    }

    @Override
    public Area getCollisionBounds(){

        return new Area(
                //new Rectangle((int)x,(int)y,ballRadius*10/8, ballRadius*10/8));
                new Ellipse2D.Double((int)x-radius,(int)y-radius,2*radius, 2*radius));
    }
}
