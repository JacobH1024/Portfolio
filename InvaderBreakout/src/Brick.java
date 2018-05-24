/*
 This class is the bricks that need to be broken
 This class has many static elements to it as it serves to create and somewhat manage the bricks 
 */
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.IOException;




public class Brick extends GameObject{
public static int brickHeight = 25;
public static int brickWidth = 50;
private Color brickTempColor;
public static final int columns = 20;
public static final int rows = 5;
public static Brick[][] bricks = new Brick[columns][rows];//holds all bricks
public static BufferedImage img = null;
public static int brickDomain;
public boolean destroyed = false;

    public Brick(double x,double y) {
    	super(x,y,GameObject.ID.brick);
    	brickTempColor = Color.getHSBColor((float)Math.random(),1,1);
    }
    public static void setImg(BufferedImage brickImg){
        img = brickImg;
    }
    public static int[] buildBrick(int i, int j){
    	Brick newBrick = new Brick(i*brickWidth,j*brickHeight);
    	bricks[i][j] = newBrick;
    	return  null;
    }
    public double[] collide(GameObject obj, Handler handler){//called when ball hits brick, returns balls new velocity
    	destroyed = true;
        handler.removeStaticObject(this);//destroy brick
    	double[] resultantVect = new double[2];//velocity is reflected from bouncing off the brick
        if(((obj.getY()-y)>Math.abs((brickHeight/brickWidth)*((obj.getX()-x)-brickWidth/2))+brickHeight/2)) {
            resultantVect[0] = (obj.getVelX());
            resultantVect[1] = (-obj.getVelY());
        }else{
            resultantVect[0] = (-obj.getVelX());
            resultantVect[1] = (obj.getVelY());
        }
        Enemy e1 = new Enemy((int)(x+brickWidth/2),(int)(y+brickHeight),handler);
        handler.addObject(e1);
    	return resultantVect;
    }
    public Area getCollisionBounds(){
        return new Area(new Rectangle((int)x,(int)y,brickWidth, brickHeight));
    }
    public void tick(){}//currently Bricks have been made static game objects and do not tick
    public void render(Graphics g){
    	g.setColor(brickTempColor);
    	g.fillRect((int)x,(int)y,brickWidth,brickHeight);
    	g.setColor(Color.black);
    	g.draw3DRect((int)x, (int)y, brickWidth, brickHeight,true);
        //g.drawImage(img, (int)x, (int)y, brickWidth, brickHeight, null);
    }



    public static void buildMap(Handler handler,int windowWidth){
        brickWidth = windowWidth/20;//Makes the bricks width  1/20 of screen size
        brickHeight = brickWidth/2;//makes the bricks height 1/2 of bricks width
        for (int i = 0;i<columns;i++){//makes 20 columns of bricks
            for(int j = 0;j<rows;j++){//makes 6 rows of bricks
                buildBrick(i,j);//creates brick
                handler.addStaticObject((Brick.bricks[i][j]));//adds the brick to the handlers object list
            }
        }
        brickDomain = brickHeight*5;
    }
}
