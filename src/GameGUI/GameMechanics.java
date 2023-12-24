package GameGUI;

import Setup.Data;

public class GameMechanics extends Data{
	
	public static boolean start = true;
	public static boolean score_update_1 = true;
	
	public GameMechanics(int score_limit, int difficulty, boolean p1Mouse) {
		super(score_limit, difficulty, p1Mouse);
		start = true;
		score_update_1 = true;
		Game g = new Game(this);
		// TODO Auto-generated constructor stub
	}
	public static int checkCollision(int player, int bar_y, int bar_width, int bar_height, int ball_rad, int[] ball_loc, int X, int Y) {
		/*
		 * checks if the ball collide with the bar
		 * if the ball is not at the boundary yet return 0
		 * if the ball is at boundary but doesnt colllide return -1
		 * if the ball is at boundary and collides return 1
		 * score_update_1 is a boolean which ensures that the score of one side is only updated once per bounce
		 */
		//System.out.println(score_update_1 + "  " + start);
		if (player == 1) {
			if (ball_loc[0] <= bar_width && (score_update_1 || start) ) //if ball is at boundary of X and its turn has come
			{
				//System.out.println(ball_loc[0]);
				score_update_1 = false;
				start = false;
				if (ball_loc[1] >= bar_y && ball_loc[1] <= bar_y + bar_height) //if ball touches bar
				{
					return 1;
				}
				else return -1;
			}
		}
		else {
			if (ball_loc[0] + ball_rad * 2 >= X - bar_width && (!score_update_1 || start)) //if ball is at boundary of X
			{
				start = false;
				score_update_1 = true;
				if (ball_loc[1] >= bar_y && ball_loc[1] <= bar_y + bar_height) //if ball touches bar
				{
					return 1;
				}
				else return -1;
			}
		}
		return 0;
	}
	public boolean scorer(int val1, int val2) {
		/*
		 * increases score according to collision data
		 * return true if the game can continue
		 * else returns false if score limit is reached.
		 */
		if (val1 == -1) {
			score_p2 ++;
		}
		if (val2 == -1) {
			score_p1 ++;
		}
			
	
		if (score_p1 == getScoreLimit() || score_p2 == getScoreLimit() )
		{
			return false;
		}
		return true;
	}
	
	
}
