/*
This class ensures all game objects have the necessary functions and variables
*/
import java.awt.*;
import java.awt.geom.Area;

public class GameObject {

	protected double x = 0;// x location in game world space
	protected double y = 0;// y location in game world space
	public enum ID {
		player, enemy, brick, ball,special
	}
	protected ID id;
	protected double velX, velY;
	
	public GameObject(double x, double y, ID id){
		this.x = x;
		this.y = y;
		this.id = id;
	}
	public double[] collide(GameObject obj, Handler handler){
		return null;
	}
	public Area getCollisionBounds(){
		return null;
	}
	public void tick(){
	}
	public void render(Graphics g){

	}
	public void setX(double x){
		this.x = x;
	}
	public void setY(double y){
		this.y = y;
	}
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public void setID(ID id){
		this.id = id;
	}
	public ID getID(){
		return id;
	}
	public void setVelX(double velX){
		this.velX = velX;
	}
	public void setVelY(double velY){
		this.velY = velY;
	}
	public double getVelX(){
		return velX;
	}
	public double getVelY(){
		return velY;
	}
}
