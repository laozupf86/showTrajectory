package dataS;

/**
 * A temporary data structure used to store the Frame point during converting from trajetcory
 * into frame structure.
 * @author Haozhou
 *
 */


public class TempFramePoint {
	
	private double x;
	private double y;
	private int tid;
	private int timeslot;
	private long t;
	
	/**
	 * 
	 * @param tid the id of trajectory that this point belongs to 
	 * @param x the coordinate
	 * @param y the coordinate
	 * @param timeslot the time slot of this point belongs to
	 * @param t time stamp
	 */
	
	public TempFramePoint(int tid, double x, double y, int timeslot, long t){
		
		this.x = x;
		this.y = y;
		this.tid = tid;
		this.timeslot = timeslot;
		this.t = t;
		
		
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getTid() {
		return tid;
	}

	public int getTimeslot() {
		return timeslot;
	}

	public long getT() {
		return t;
	}
	
	

}
