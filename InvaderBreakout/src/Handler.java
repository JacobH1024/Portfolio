/*
This class does a lot of work for the engine
	essentially this class serves to propagate commands from the engine and keep track of all game objects
This class holds all game objects in one of two arraylists to keep a reference to them 
	The class separates these into 2 different arrays because it makes collision less costly
	we know where static objects will be so be know if well need to colide with them
	dynamic objects could be anywhere and they all need to check collsion
This the engine calls a tick or render it sends it to the handler which then delegates the task to each object
*/
import java.awt.Graphics;
import java.util.LinkedList;


public class Handler {

	public LinkedList<GameObject> object = new LinkedList<>();//holds objects that move and are dynamic
	public LinkedList<GameObject> staticObjects = new LinkedList<>();//holds objects that do not change after creation unless destroyed (bricks)
	public Paddle player;
	public AppWindow window;
	//call tick for dynamic game objects
	public void tick(){
		for (int i = 0; i < object.size(); i++){ 
			GameObject tempObject = object.get(i);
			tempObject.tick();
		}
	}
	public void render(Graphics g){
	    //render static game objects first
            for (int i = 0; i < staticObjects.size(); i++) {
                GameObject tempObject = staticObjects.get(i);
                tempObject.render(g);
            }

        //Render dynamic game objects  next
        for (int i = 0; i < object.size(); i++){
            GameObject tempObject = object.get(i);
            tempObject.render(g);
        }

	}
	public void restart(){
		object = new LinkedList<>();
		staticObjects = new LinkedList<>();
	}

    public void addStaticObject(GameObject obj){
        this.staticObjects.add(obj);
    }
    public void removeStaticObject(GameObject obj){
        this.staticObjects.remove(obj);
    }

	public void addObject(GameObject obj){
		this.object.add(obj);
	}
	public void removeObject(GameObject obj){
		this.object.remove(obj);
	}

	
}
