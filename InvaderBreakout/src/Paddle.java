/**
 * @(#)Paddle.java
 *
 *
 * @author Jacob Hertl
 * @version 0.60 2017/4/31
 * This is the player class that is the paddle the player controlls
 * It is responsible for displaying the paddle and for colliding with the ball
 */
import javafx.scene.shape.Arc;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;


public class Paddle extends GameObject{
private int paddleWidth = 0;
private int paddleHeight = 0;
private int paddleMoveSpeed;//Plan to restrict paddle movement speed
private BufferedImage[] spriteBook = new BufferedImage[1];//Texture for paddle
private int animCounter = 0;
private BufferedImage img = null;//Texture for paddle
private Handler handler;
private AppWindow window;
private double xAdj;

    public Paddle (double x, double y, ID id, Handler handler, AppWindow window){
    	super(x,y,id);
    	this.window = window;
    	paddleWidth = window.width/5;
    	paddleHeight = window.height/10;
        xAdj = window.width/2;//*Math.cos(10*Math.PI/180)/2;
        this.handler = handler;
        handler.addObject(this);
        try {
            String imgPath = "SpacePaddleTest3Tex1.png";
            spriteBook[0] = ImageIO.read(getClass().getResourceAsStream(imgPath));

        } catch (IOException e) {
            System.out.println("Image did not load. error is: " + e);
        }

    }
    //Right now the paddle does not currently have any functionality requiring tick
    public void tick(){
    }
    //Used stop player from going off the screen
    public Area getCollisionBounds(){
    	return new Area(
    	        //new Rectangle((int)(x-paddleWidth/2),(int)(y-paddleHeight/2),paddleWidth,paddleHeight));
                new Arc2D.Double((x-(xAdj)), (y-paddleHeight/2),
                        window.width, window.height, 80, 20,Arc2D.CHORD));
                // (int)x,(int)y,paddleWidth, paddleHeight
    }
    public BufferedImage animation(){
        if (animCounter>=spriteBook.length){animCounter=0;}
        return spriteBook[animCounter];
    }

    public void render(Graphics g){
        img = animation();
        //g.drawImage(img, (int)(x-paddleWidth/2), (int)(y-paddleHeight/2),
				//paddleWidth, paddleHeight, null);
        if(true) {//display collision for debugging
            Color paddleColor = new Color(0, 1, 1, .8f); //Red
            g.setColor(paddleColor);
            g.fillOval((int)x-10,(int)y-10,20,20);
            g.fillArc((int)(x-(xAdj)), (int)(y-paddleHeight/2), window.width, window.height, 80, 20);
        }
    }
    public double[] collide(GameObject obj, Handler handler){ 
    	double[] resultantVect = new double[2];//vector that will contain the new reflected velocity
    	double xp = obj.getX() - (x); //convert world impact x loc to relative(to Paddle)
    	double yp = obj.getY() - (y - paddleHeight/2 + window.height) ; //convert world impact y loc to relative(to Paddle)
        /*(xp,yp) is point of impact relative to paddle
        (obj.getX,obj.getY) is world impact location(Ball location)
        (x + paddleWidth/2,y + paddleHeight) is center of the arc of paddle */
    	double xn = (xp/ (Math.sqrt(xp*xp + yp*yp)));//normalize xp
    	double yn = (yp/ (Math.sqrt(xp*xp + yp*yp)));//normalize yp
        //(xn,yn) is the normal of the impact
    	double xv = obj.getVelX();//x velocity of ball just before impact
    	double yv = obj.getVelY();//y velocity of ball just before impact
        /*Formula to find reflection of a vector across a normal
        Reflection = V - ((2V<dot>N)/(||N||^2))N // where V is the Vector and N is the normal
        A lot of simplification has occurred to obtain formula below mainly due to magnitude of the normal being 1
        Reflection = V - 2(V<dot>N)*N*/
    	resultantVect[0] = xv - 2*(xv*xn+yv*yn)*xn;//Reflect x part of vector over normal
        resultantVect[1] = yv - 2*(xv*xn+yv*yn)*yn;//Reflect y part of vector over normal
    	return resultantVect;
    }
    public int getPaddleWidth(){
    	return paddleWidth;
    }
    public int getPaddleHeight(){
        return paddleHeight;
    }
}
