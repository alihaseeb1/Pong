package GameGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.ImageObserver;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import Setup.Data;
import Setup.GameSetup;

public class Game extends JPanel implements KeyListener, MouseMotionListener, ActionListener, WindowListener{
	JFrame frame = new JFrame();
	Random rand = new Random();
	Timer timer = new Timer(5, this);
	
	Dimension dim = this.getSize();
	double X = dim.getWidth();
	double Y = dim.getHeight();
	
	private final int START_SCREEN_X = 1000;
	private final int START_SCREEN_Y = 600;
	
	int bar_width = (int) (X * 0.005);
	int bar_height = (int) (Y * 0.25); //explicit casting checklist 2.
	private int[] bar_y = new int[2]; //y location of bar 
	private int bar_vel = 5;	
 
	private int[] ball_vel = {rand.nextInt(5 + 5) - 5, rand.nextInt(5 + 5) -5};
	private int ball_rad = 20;
	int[] ball_loc = new int[2];
	boolean initial = true;
	boolean continueGame = true;
		
	GameMechanics gameMech;
	
	public Game(GameMechanics gm) {
		System.out.println("IN GAME");
		this.gameMech = gm;
		setFocusable(true);
		this.addKeyListener(this);
		this.addMouseMotionListener(this);
		frame.add(this, BorderLayout.CENTER);
		frame.addWindowListener(this);
		
		frame.setSize(START_SCREEN_X, START_SCREEN_Y);
		frame.setVisible(true);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//System.out.println(ball_vel[0] + "  " + ball_vel[1]);
		dim = this.getSize(); //get the size of the JPanel
		
		//X, Y can change if we resize screen
		X = dim.getWidth(); 
		Y = dim.getHeight();
		bar_width = (int) (X * 0.005);
		bar_height = (int) (Y * 0.25);
		g.setColor(Color.blue);
		
		//draw scores
		Font myFont1 = new Font("Serif", Font.BOLD, 30);
		g.setFont(myFont1);
		int[] score = gameMech.getScore();
		g.drawString("" + score[0], (int) (X/2 - 50), 30); //implicit casting
		g.drawString("" + score[1], (int) (X/2 + 50), 30);
		
		//Start Game Conditions
		if (initial) { //initial is changed to false if there is keyEvent/mouseEvent
			g.setFont(myFont1);
			String startText = "PRESS W/S OR MOUSE TO START GAME";
			g.drawString(startText, (int) X/2 - startText.length() * 8, (int) Y/2 - 5);
			
			//set the location of the bar at the mid of the screen
			bar_y[1] = (int) (Y/2 - bar_height / 2);
			bar_y[0] = (int) (Y / 2 - bar_height / 2);
			
			//randomly spawn ball
			ball_spawn();
		}
		else {
			//start ball movement
			timer.start();
			//checking if ball touches bar
			//for p1
			int p1_collide = GameMechanics.checkCollision(1 ,bar_y[0], bar_width, bar_height, ball_rad, ball_loc, (int) X, (int) Y);
			int p2_collide = GameMechanics.checkCollision(2, bar_y[1], bar_width, bar_height, ball_rad, ball_loc, (int) X, (int) Y);
			
			if (p1_collide != 0 || p2_collide != 0) //check if the ball touches either of the sides
			{
				//increase speed of ball according to difficulty
				increase_speed();
			}
			//System.out.println("p1 : " + p1_collide + "   p2 " + p2_collide);
			
			if (continueGame) //if score limit hasn't been reached yet
			{
				continueGame = gameMech.scorer(p1_collide, p2_collide);
			} else {
				//if score limit is reached
				ball_vel[0] = 0; ball_vel[1] = 0;
				g.setColor(Color.red); //changes component color to red
				g.setFont(myFont1);
				String startText = "GAME OVER";
				if (score[0] > score [1]) startText += " PLAYER 1 WINS";
				else startText += " PLAYER 2 WINS";
				g.drawString(startText, (int) X/2 - startText.length() * 10, (int) Y/2 - 5);
				String endText = "Press Esc to start New Game";
				g.drawString(endText, (int) X/2 - startText.length() * 10, (int) Y/2 + 20);
			}
		}
		//System.out.println(ball_vel[0] + " , " + ball_vel[1]);
		//drawing center line and oval
		g.drawLine((int) (X * 0.5), 0, (int) (X * 0.5), (int) Y);
		int rad_center_oval = (int) (0.25 * Y );
		g.drawOval((int)(0.5 * X - rad_center_oval), (int) (0.25 * Y), rad_center_oval  * 2, rad_center_oval * 2);
		
		//drawing bars
		//p1
		g.setColor(Color.green);
		g.fillRect(0, bar_y[0], bar_width, bar_height);
		
		//p2
		g.setColor(Color.red);
		g.fillRect((int)(X - bar_width), bar_y[1], bar_width, bar_height);
		
		//ball
		g.setColor(Color.blue);
		g.fillOval(ball_loc[0], ball_loc[1], ball_rad * 2, ball_rad * 2);
	}
	
	private void increase_speed() {
		/*
		 * This method increases the speed of ball everytime it is called according to the difficulty
		 * It also increases keyBoard movement speed of the bar as ball speed increases to keep up with the speed
		 */
		if (ball_vel[0] < 0) {
			ball_vel[0] -= gameMech.getDifficulty();
		} else {
			ball_vel[0] += gameMech.getDifficulty();
		}
		
		if (ball_vel[1] < 0) {
			ball_vel[1] -= gameMech.getDifficulty();
		} else {
			ball_vel[1] += gameMech.getDifficulty();
		}
		
		bar_vel += Math.ceil(gameMech.getDifficulty() * 0.5); 
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		initial = false; //start the game
		// TODO Auto-generated method stub
		
		//switches between key control for p1 and p2
		int selection = 0;
		if (gameMech.isP1Mouse) selection = 1;
		else selection = 0;
		
		if (e.getKeyCode() == KeyEvent.VK_UP) { //if up arrow key is pressed
			bar_y[selection] -= bar_vel;
			if (bar_y[selection] < 0) bar_y[selection] = 0;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) //if down arrow key is pressed
		{
			int frameH = (int) this.getSize().getHeight();
			int bar_height = (int) (frameH * 0.25);
			//System.out.println(frame.getSize().getHeight() + "  " + p2_bar_y);
			bar_y[selection] += bar_vel;
			//System.out.println(p2_bar_y);
			if (bar_y[selection] + bar_height> frameH) bar_y[selection] = frameH - bar_height;
		}
		if (e.getKeyCode() ==  KeyEvent.VK_ESCAPE) { //if escape key is pressed
			frame.dispose();
		}
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	private void ball_spawn() {
		/*
		 * this method sets the ball radius as well as randomly generates a y location for the ball to be spawned
		 * generates a random velocity for the ball as well
		 * ensures travel direction is not horizontal or vertical
		 */
		
		//ball radius relative to screen height Y
		ball_rad = (int) (Y * 0.03);
		ball_loc[0] = (int) (X/2 - ball_rad); ball_loc[1] = rand.nextInt((int) Y - ball_rad * 2);

		ball_vel[0] = rand.nextInt(5 + 5) - 5; ball_vel[1] = rand.nextInt(5 + 5) -5;
		while (ball_vel[0] == 0 || ball_vel[1] == 0) //ensure that ball travels in a slant line
		{

			ball_vel[0] = rand.nextInt(5 + 5) - 5; ball_vel[1] = rand.nextInt(5 + 5) -5;
			
		}

	}
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		//switches mouse control for p1 and p2
		int selection = 0;
		if (gameMech.isP1Mouse) selection = 0;
		else selection = 1;
		
		initial = false; //starts the game
		int frameH = (int) this.getSize().getHeight();
		int bar_height = (int) (frameH * 0.25);
		int y = e.getY() - bar_height / 2;
		if (y > 0 && y + bar_height < (int) (this.getSize().getHeight()))
			bar_y[selection] = y;
		repaint();
	}
	

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		 * Gets called by timer after initial = false to start movement of ball
		 */
		ball_loc[0] = Math.addExact(ball_loc[0], ball_vel[0]); //checklist 12
		ball_loc[1] = Math.addExact(ball_loc[1], ball_vel[1]);
		//ball_loc[0] += ball_vel[0];
		//ball_loc[1] += ball_vel[1];
		if (ball_loc[0] <= bar_width || ball_loc[0] + ball_rad * 2 >= X - bar_width) { //horizontal bounce
			ball_vel[0] = - ball_vel[0];
		}
		if (ball_loc[1] <= 0 || ball_loc[1] + ball_rad * 2 >= Y) { //vertical bounce
			ball_vel[1] = - ball_vel[1];
		}
		
		repaint();

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		//start new game
		new GameSetup();
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
	}
	
}
