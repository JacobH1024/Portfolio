/*
This class creates the window in which the game will be housed
 */
import java.awt.*;
import javax.swing.*;

public class AppWindow extends Canvas{

	private static final long serialVersionUID = -240840600533725354L;
	public int width;
	public int height;
    JFrame frame;

    public AppWindow(int width, int height, String title,GameEngine game ) {
    	this.width = width;
    	this.height = height;
    	frame = new JFrame(title);
    	frame.setPreferredSize(new Dimension(width, height));
    	frame.setMaximumSize(new Dimension(width, height));
    	frame.setMinimumSize(new Dimension(width, height));
    	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    	frame.setResizable(false);
    	frame.setLocationRelativeTo(null);
    	frame.add(game);
    	frame.setVisible(true);
    	frame.setLayout(new FlowLayout());
    	game.start();
    }

}
