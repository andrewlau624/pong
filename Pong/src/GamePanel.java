import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;

import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener, KeyListener{
	static final int SCREEN_WIDTH = 1000;
	static final int SCREEN_HEIGHT = 720;
	static final int PADDLE_WIDTH = SCREEN_WIDTH/100;
	static final int PADDLE_HEIGHT = SCREEN_HEIGHT/7;
	static final int PADDLE_SPEED = 5; 
	static final int DELAY = 0;
	
	int PADDLE_VELOCITY = 0;
	
	int paddleOneX = 10;
	int paddleTwoX = SCREEN_WIDTH - PADDLE_WIDTH - 10;
	
	int paddleOneY = SCREEN_HEIGHT/2;
	int paddleTwoY = SCREEN_HEIGHT/2;
	
	int ballX;
	int ballY;
	
	int ballSpeed = 6;
	
	int xDir = -ballSpeed;
	int yDir = ballSpeed;
	
	int yMove;
	int yMoveP;
	
	int p2Score = 0;
	int p1Score = 0;
	
	int opacity = 0;
	int opacityAdd = 5;
	
	Timer timer;
	Ball ball;
	
	GamePanel(){
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(this);
		startGame();
	}

	public void startGame() {
		timer = new Timer(DELAY, this);
		ball = new Ball();
		ball.spawnBall(SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
		ball.draw(g);
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(paddleOneX, paddleOneY, PADDLE_WIDTH, PADDLE_HEIGHT);
		
		g.setColor(Color.white);
		g.fillRect(paddleTwoX, paddleTwoY, PADDLE_WIDTH, PADDLE_HEIGHT);
		
		Graphics2D g2d = (Graphics2D)g;
			
		float[] pattern = {20f, 20f}; 
		Stroke stroke1 = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,1.0f, pattern, 1.0f);
		g2d.setStroke(stroke1);
		g2d.drawLine(SCREEN_WIDTH/2, 0, SCREEN_WIDTH/2, SCREEN_HEIGHT);
		
		g.setFont(new Font("SansSerif", Font.PLAIN, 50));
		g.drawString(Integer.toString(p1Score), SCREEN_WIDTH/4-15, SCREEN_HEIGHT/10);
		
		g.setFont(new Font("SansSerif", Font.PLAIN, 50));
		g.drawString(Integer.toString(p2Score), SCREEN_WIDTH-SCREEN_WIDTH/4-15, SCREEN_HEIGHT/10);
		
		g.setFont(new Font("SansSerif", Font.PLAIN, 20));
		g.drawString("'E' to autoplay", 10, SCREEN_HEIGHT-10);
		
		if(cheat) {
			if(opacity >= 0) {
				opacity += opacityAdd;
			}
			
			if(opacity >= 255) {
				opacity = 255;
				opacityAdd *= -1;
			}
			if(opacity <= 0) {
				opacity = 0;
				opacityAdd *= -1;
			}
			
			
			g2d.setFont(new Font("SansSerif", Font.PLAIN, 50));
			g2d.setColor(new Color(255, 0, 0, opacity));
			g2d.drawString("AUTO",SCREEN_WIDTH/2-70, SCREEN_HEIGHT-10);
		}
	}
	
	public void checkEdges() {
		ballX = ball.returnX();
		ballY = ball.returnY();
		
		if(ballY <= 0) {
			yDir *= -1;
		}
		if(ballX <= 0) {
			ball.spawnBall(SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
			p2Score++;
		}
		if(ballY >= SCREEN_HEIGHT) {
			yDir *= -1;
		}	
		if(ballX >= SCREEN_WIDTH) {
			ball.spawnBall(SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
			p1Score++;
		}
		
		if(ballY >= paddleOneY && ballY <= paddleOneY+PADDLE_HEIGHT && ballX <= paddleOneX+PADDLE_WIDTH) {
			xDir *= -1;
		}
		
		if(ballY >= paddleTwoY && ballY <= paddleTwoY+PADDLE_HEIGHT && ballX >= paddleTwoX) {
			xDir *= -1;
		}
	}
	
	boolean topBounce;
	
	public void paddleAi() {
		
		if(xDir != ballSpeed) {
			return;
		}
		
		if(ballY <= 0) {
			yMove = SCREEN_WIDTH-ballX;
		}
		
		if(ballY >= SCREEN_HEIGHT) {
			yMove = ballY-(SCREEN_WIDTH-ballX);
		}
		
		if(paddleTwoY+PADDLE_HEIGHT/2 < yMove && paddleTwoY+PADDLE_HEIGHT <= SCREEN_HEIGHT)
			paddleTwoY += PADDLE_SPEED; 
		if(paddleTwoY+PADDLE_HEIGHT/2 > yMove && paddleTwoY >= 0)
			paddleTwoY -= PADDLE_SPEED; 
		
	}
	
	boolean cheat = false;
	
	public void playerAi() {
		if(cheat) {
		if(-xDir != ballSpeed) {
			return;
		}
		
		if(ballY <= 0) {
			yMoveP = ballX;
		}
		
		if(ballY >= SCREEN_HEIGHT) {
			yMoveP = ballY-ballX;
		}
		
		if(paddleOneY+PADDLE_HEIGHT/2 < yMoveP && paddleOneY+PADDLE_HEIGHT <= SCREEN_HEIGHT)
			paddleOneY += PADDLE_SPEED; 
		if(paddleOneY+PADDLE_HEIGHT/2 > yMoveP && paddleOneY >= 0)
			paddleOneY -= PADDLE_SPEED; 
		}
		
	}
	
	public void playerMove() {
		
		if(paddleOneY < 0) {
			paddleOneY = 0;
			PADDLE_VELOCITY = 0;
		}
		
		if(paddleOneY+PADDLE_HEIGHT > SCREEN_HEIGHT) {
			paddleOneY = SCREEN_HEIGHT-PADDLE_HEIGHT;
			PADDLE_VELOCITY = 0;
		}
		
		paddleOneY += PADDLE_VELOCITY;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ball.move(xDir, yDir);
		playerMove();
		paddleAi();
		playerAi();
		checkEdges();
		repaint();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int p = e.getKeyCode();
		if(p == KeyEvent.VK_W && paddleOneY != 0 && !cheat) {
			PADDLE_VELOCITY = -PADDLE_SPEED;
		} else if(p == KeyEvent.VK_S && paddleOneY+PADDLE_HEIGHT != SCREEN_HEIGHT && !cheat) {
			PADDLE_VELOCITY = PADDLE_SPEED;
		}
		if(p == KeyEvent.VK_E) {
			cheat = !cheat;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		int p = e.getKeyCode();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int p = e.getKeyCode();
		if(p == KeyEvent.VK_W) {
			PADDLE_VELOCITY = 0;
		} else if(p == KeyEvent.VK_S) {
			PADDLE_VELOCITY = 0;
		}
	}
}
