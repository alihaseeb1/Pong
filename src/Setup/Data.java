package Setup;


public class Data {
	protected int score_p1 = 0;
	protected int score_p2 = 0;
	protected int score_limit = 0;
	private int difficulty;
	public boolean isP1Mouse;
	protected Data(int score_limit, int difficulty, boolean p1Mouse)
	{
		this.difficulty = difficulty;
		this.isP1Mouse = p1Mouse;
		this.score_limit = score_limit;
		System.out.println("Data created with following details : " + this);
	}
	public void point_p1() {
		score_p1++;
	}
	public void point_p2() {
		score_p2++;
	}
	public int[] getScore() {
		return new int[] {score_p1, score_p2};
	}
	public int getDifficulty() {
		return difficulty;
		
	}
	public int getScoreLimit() {
		return score_limit;
	}
	public String toString() {
		return "difficulty : " + difficulty + " time_limit : " + score_limit + " isP1Mouse : " + isP1Mouse;
	}
}
