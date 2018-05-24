/*
This class functions as both the game engine and the game mode since there is only one
This class drives the game by creating ticks update the world
This class also renders the game by creating the background and calling hte render command to be propagated to objects
This class also takes the input from the players mouse
As of now the game runs on the single thread, most of the time this fine, it runs with no lag
	if I update the graphics in the game I will have the game run on two threads one for game ticks and the other for rendering
*/
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @GameEngine
 *
 *
 * @author Jacob Hertl
 * @version 0.3.1 3/9/2017
 */
public class GameEngine extends Canvas implements Runnable, MouseMotionListener, MouseListener {
private static final long serialVersionUID = 155069109782471818L;

	
	private int width;//will be width of window
	private int height;//will be height if window
	private Thread thread;
	private boolean running = false;//indicates if thread is running
	private Handler handler;//  handler for all game objects
	private Graphics g;//graphics to display
	private Paddle player1; //player
	private Ball ball1;//players initial ball
	private BufferedImage bkgImg = null; //background image
    private BufferedImage brickImg = null;
	private AppWindow window;
    private BufferStrategy bs;
    private boolean gameOver;
    private double mX = 0;
    private double mY = 0;

    public GameEngine() {
    	handler = new Handler();
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();//gets screen size of the display 
    	width = (int)screenSize.getWidth();//sets width to width of display
    	height = (int)screenSize.getHeight();//sets height to height of the display
    	window = new AppWindow(width, height, "Invader Breakout" , this);//creates the app window the game is rendered in
        try {
            String imgPath = "SandStoneBrick1.png";
            brickImg = ImageIO.read(getClass().getResourceAsStream(imgPath));
            imgPath = "pEeUsp1.jpg";
            bkgImg = ImageIO.read(getClass().getResourceAsStream(imgPath));
        } catch (IOException e) {
            System.out.println("Image did not load. error is: " + e);
        }
        createGameObjects();
        addMouseMotionListener(this);//adds mouse motion listener to the game engine
        addMouseListener(this);//adds mouse listener to the game engine

    }
    private void createGameObjects(){
        player1 = new Paddle(width/2,height - height/20, GameObject.ID.player, handler,window);//creates the player at the bottom center of the window
        Brick.setImg(brickImg);
        Brick.buildMap(handler,width);// creates bricks for the map and adds them to the handlers object list
        ball1 = new Ball(player1.getX(),player1.getY()-player1.getPaddleHeight(),GameObject.ID.ball,handler, window);//creates the ball slightly in front of the player
        ball1.engine = this;
        handler.player = player1;
        handler.window = window;
    }

    public void restart(){
        handler.restart();
        createGameObjects();
        start();
    }

    public void gameOver(){
        gameOver = true;

        //restart();

    }
	
	public synchronized void start(){//thread starts
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	public synchronized void stop(){//thread stops
		try{
			thread.join();
			running = false;
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void run(){
        int fps = 60;
		while(running){
			long time =  System.currentTimeMillis();
			tick();
			render();
			time = (1000/fps)- (System.currentTimeMillis() - time);
			if (time > 0){
                //System.out.println("fps+  "+time);
				try{
					Thread.sleep(time);
				}catch(Exception e){
					System.out.println("An error has occurred " + e);
				}
			}else{
                //System.out.println("lag-  "+time);
            }
		}
	}
	
	
	
	private void tick(){//game tick
		handler.tick();
	}
	
	private void render(){//renders graphics in window
		bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(2);
			return;
		}//double buffer keeps image smooth
		g = bs.getDrawGraphics();

		g.drawImage(bkgImg, 0, 0, width, height,null);//creates the background image
        handler.render(g);//calls handler to render all objects
        if(gameOver) {
            g.setFont(new Font("Serif", Font.BOLD, 96));
            g.drawString("Game Over", width / 3, height / 2);
        }
		bs.show();//makes the next buffer visible

        g.dispose();// disposes of graphics objects to free resources

	}

    public static void main(String Args[]){

		new GameEngine();
    }
    


	@Override
	public void mouseDragged(MouseEvent arg0) {//sets players positions to the cursors x position given that it is in range
		if(arg0.getX() >= 0 & arg0.getX()  <= width){
		player1.setX(arg0.getX());
		}
        mX = arg0.getX();
        mY = arg0.getY();
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {//sets players position to the cursors x positions given that it is in range
		if(arg0.getX() >= 0 & arg0.getX()  <= width){
			player1.setX(arg0.getX());	
		}
		mX = arg0.getX();
		mY = arg0.getY();
	}
    @Override
    public void mousePressed(MouseEvent e) {
    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }
    @Override
    public void mouseClicked(MouseEvent e) {//creates a projectile to destroy enemies
        double pVelX = mX- player1.getX();
        double pVelY = mY - player1.getY();
        double mag = Math.sqrt(pVelX*pVelX+pVelY*pVelY);
        double speed = (25*window.height/1000);
        pVelX=speed*pVelX/mag;
        pVelY=speed*pVelY/mag;
        Projectile laser = new Projectile((int)player1.getX(),(int)player1.getY(),pVelX,pVelY,handler);
        handler.addObject(laser);
        //System.out.println("Mouse has been clicked");
    }

	public Graphics getGraphics(){
		return g;
	}
	public Handler getHandler(){
		return handler;
	}
	public Paddle getPlayer(){ //returns player
    	return player1;
    }
}
