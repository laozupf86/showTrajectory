package dataS;

/**
 * A data structure used to represent a sample point
 * @author Haozhou
 *
 */

public class SamplePoint {
	
	private double x;
	private double y;
	private long t;
	
	/**
	 * 
	 * @param x the coordinate
	 * @param y the coordinate
	 * @param t time stamp
	 */
	public SamplePoint(double x, double y, long t){
		this.x = x;
		this.y = y;
		this.t = t;
	}
	
        public SamplePoint(double x, double y){
            this.x = x;
            this.y = y;
        }
        
	/*
	 * A serious operators  for setting and getting
	 */

	public double getX() {
		return x;
	}


	public double getY() {
		return y;
	}


	public long getT() {
		return t;
	}

	

}
