package dataS;

/**
 * A data structure used to the point information in each iframe
 * @author Haozhou
 *
 */

public class IFramePoint {
	
	private double x, y;
	private int tid;
	
	/**
	 * 
	 * @param x the coordinate of point
	 * @param y the coordinate of point
	 * @param tid the id of trajectory
	 */
	public IFramePoint(double x, double y, int tid){
		this.x = x;
		this.y = y;
		this.tid = tid;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}
	
	

}
