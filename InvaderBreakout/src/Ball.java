/*
 This class is the players ball that is bounced back and forth to break bricks
 If the ball hits a brick it is destroyed and the ball bounces back 
 If the ball hits any wall but the buttom it bounces back
 If the hits the bottom wall its game over
 */
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;




public class Ball extends GameObject{
private double ballSpeed = 10;
private int ballRadius = 10;
Handler handler;
AppWindow window;
long collisionTime = 0;
private boolean displayCollision = false;
private BufferedImage[] spriteBook = new BufferedImage[3];
private int animCounter = 0;
private BufferedImage img1 = null;
public GameEngine engine = null;

   public Ball(double x,double y, GameObject.ID id, Handler handler, AppWindow window){
	   super(x,y,id);
	   this.handler = handler;
	   this.window = window;
	   ballRadius = (20*window.height/1000);
	   ballSpeed = (25*window.height/1000);
	   velX = 0; 
	   velY = -ballSpeed;
	   handler.addObject(this);
       try {
           String imgPath = "SpaceBall1.png";
           spriteBook[0] = ImageIO.read(getClass().getResourceAsStream(imgPath));
		   imgPath = "SpaceBall2.png";
		   spriteBook[1] = ImageIO.read(getClass().getResourceAsStream(imgPath));
		   imgPath = "SpaceBall3.png";
		   spriteBook[2] = ImageIO.read(getClass().getResourceAsStream(imgPath));

       } catch (IOException e) {
           System.out.println("Image did not load. error is: " + e);
       }
   }
   public void collision(){
	   double[] resultantVect;
	   for(int i = 0; i < handler.object.size(); i++){
		   GameObject tempObj = handler.object.get(i);
		    if (tempObj.id == ID.player){
                Area intersection = getCollisionBounds();
                intersection.intersect(tempObj.getCollisionBounds());
			   if(!intersection.isEmpty()){
				     	resultantVect = tempObj.collide(this, handler);
				   		velX = resultantVect[0];
				   		velY = resultantVect[1];
			   }
		   }
	   }
	   //collision with bricks
       if(y-ballRadius/2 < Brick.brickDomain) {//does not check collision if ball is not near bricks should be
           //calculate which bricks should be targeted
           int c = (int) x / Brick.brickWidth;
           int r = (int) y / Brick.brickHeight;
           boolean collided = false;
           if (0 <= c && c < Brick.columns && 0 <= r && r < Brick.rows) {
               if (!Brick.bricks[c][r].destroyed) {
                   collided = brickCollision(Brick.bricks[c][r]);
               }
           }
           if (0 <= c - 1 && c - 1 < Brick.columns && 0 <= r && r < Brick.rows && !collided) {
               if (!Brick.bricks[c-1][r].destroyed) {
                   collided =brickCollision(Brick.bricks[c-1][r]);
               }
           }
           if (0 <= c  && c  < Brick.columns && 0 <= r -1 && r-1 < Brick.rows && !collided) {
               if (!Brick.bricks[c][r-1].destroyed) {
                   collided = brickCollision(Brick.bricks[c][r-1]);
               }
           }
           if (0 <= c - 1 && c - 1 < Brick.columns && 0 <= r -1&& r-1 < Brick.rows && !collided) {
               if (!Brick.bricks[c-1][r-1].destroyed) {
                   brickCollision(Brick.bricks[c-1][r-1]);
               }
           }
       }
	   
       /*
       * Old collsion algorithm was prone to error and much more costly to perform 
           for (int i = handler.staticObjects.size() - 1; i >= 0; i--) {//checks for collision backwards
               //this is faster since lower bricks are more likely to be hit and prevent bricks behind others
               //from being broken
               GameObject tempObj = handler.staticObjects.get(i);
               Area intersection = getCollisionBounds();
               intersection.intersect(tempObj.getCollisionBounds());
               if (!intersection.isEmpty()) {
                   if ((System.currentTimeMillis() - collisionTime) >= 100) {
                       System.out.println((System.currentTimeMillis() - collisionTime) + "collisionTime");
                       collisionTime = System.currentTimeMillis();
                       resultantVect = tempObj.collide(this, handler);
                       velX = resultantVect[0];
                       velY = resultantVect[1];
                       break;
                   }
               }

           }
       }*/
	   //collision with edge of screen
	   if(x >= window.width ){
		   velX = -velX;
	   }else if (x <= 0){
		   velX = -velX;
	   }
	   if (y <= 0){
		  	velY = -velY;
	   }else if (y >= window.height){
           velY = -velY;
		   //engine.gameOver();

		   
	   }
   }
   /*ball does not need to use this method because it calls other objects collide method
   * */
   public boolean brickCollision(Brick tempObj){
       double[] resultantVect;
       Area intersection = getCollisionBounds();
       intersection.intersect(tempObj.getCollisionBounds());
       if (!intersection.isEmpty()) {
           if ((System.currentTimeMillis() - collisionTime) >= 100) {
               System.out.println((System.currentTimeMillis() - collisionTime) + "collisionTime");
               collisionTime = System.currentTimeMillis();
               resultantVect = tempObj.collide(this, handler);
               velX = resultantVect[0];
               velY = resultantVect[1];
               return true;
           }
       }
       return false;
   }
   public double[] collide(GameObject obj, Handler handler){
	   return null;
   }
   public Area getCollisionBounds()
   {
   	return new Area(
   	        //new Rectangle((int)x,(int)y,ballRadius*10/8, ballRadius*10/8));
            new Ellipse2D.Double((int)x-ballRadius,(int)y-ballRadius,2*ballRadius,2*ballRadius));
   }
   public void tick(){
	   x = (int)(x + velX);
	   y = (int)(y + velY);
	   try{
	   collision();
	   }catch(java.lang.NullPointerException e ){
		   System.out.println("Error");
	   }
   }
   public BufferedImage animation(){
       if (animCounter>=spriteBook.length){animCounter=0;}
       return spriteBook[animCounter++];
   }
   public void render(Graphics g){
       img1 = animation();
            g.drawImage(img1, (int) x - ballRadius, (int) y - ballRadius, 2*ballRadius, 2*ballRadius, null);
       if(displayCollision==true) {
           Color ballColor = new Color(1, 1, 1, .3f); //white
           g.setColor(ballColor);
           g.fillOval((int)(x-ballRadius), (int)(y-ballRadius),2* ballRadius,2* ballRadius);
       }
   }
}
