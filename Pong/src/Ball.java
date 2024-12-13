import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Ball {
	
	static final int ballSize = 10;
	int x;
	int y;
	
	int xDir;
	int yDir;
	
	public void spawnBall(int width, int height) 
	{
		x = width;
		y = height;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillOval(x, y, ballSize, ballSize);
	}
	
	public void move(int xDir_, int yDir_) {
		x = x+xDir_;
		y = y+yDir_;
	}
	
	public int returnX() {
		return x;
	}
	
	public int returnY() {
		return y;
	}
}
