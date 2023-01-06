import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener{
	
	static final int SCREEN_WIDTH = 700;
	static final int SCREEN_HEIGHT = 700;
	static final int UNIT_SIZE = 25; //how big do you want the items 
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 100; //see how fast the sanke is moving
	final int x[] = new int [GAME_UNITS];
	final int y[] = new int [GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R'; //snake starts right
	boolean running = false; //game isn't running initially
	Timer timer;
	Random random;
	
	//constructor
	GamePanel(){
		//create instance of this class
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.white); //sets the background color 
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this); //this because of action listener Interface
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
		
	}
	public void draw(Graphics g) {
		
		if(running) {
			/*
			//making it a griddd
			for(int i=0; i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			*/
			g.setColor(Color.RED);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); //draw the apple
			
			for(int i=0; i<bodyParts;i++) {
				if(i==0) {
					g.setColor(Color.blue);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(Color.blue);//TODO maybe change it to green here
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}		
			}
			g.setColor(Color.RED);
			g.setFont(new Font("INK Free",Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("SCORE: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("SCORE: "+applesEaten))/2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
			
	}//end of method
	
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
		
	}
	
	public void move() {
		for(int i= bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) 
		{
		case 'U':
			y[0] = y[0]-UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0]+UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0]-UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0]+UNIT_SIZE;
			break;
		}
		
	}
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	
	public void checkCollisions() 
	{
		//see if head collides with the body of the sanke
		for(int i= bodyParts; i<0;i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		//check if head touches the outisde border(left)
		if(x[0] < 0) {
			running = false;
		}
		//check if head touches the outisde border(right)
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		//check if head touches the outisde border(upper)
		if(y[0] < 0) {
			running = false;
		}
		//check if head touches the outisde border(bottom)
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		if(!running) {
			timer.stop();
		}
		
	} 
	
	public void gameOver(Graphics g) {
		//what to print once the game is over
		g.setColor(Color.RED);
		g.setFont(new Font("INK Free",Font.BOLD, 75));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
		
		g.setColor(Color.RED);
		g.setFont(new Font("INK Free",Font.BOLD, 40));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("SCORE: "+applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("SCORE: "+applesEaten))/2, g.getFont().getSize());
	}
	
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) 
			{
			case KeyEvent.VK_LEFT:
				if(direction != 'R')
					direction = 'L';
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L')
					direction = 'R';
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D')
					direction = 'U';
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U')
					direction = 'D';
				break;
			}
		}
	}
}
