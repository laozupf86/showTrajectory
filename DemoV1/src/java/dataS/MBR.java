package dataS;

/**
 * A MBR used to represent a trajectory or a trajectory segment
 * @author Haozhou
 *
 */

public class MBR {
	
	private double xlow, ylow, xhigh, yhigh;
	private long ts, te;
	
	/**
	 * 
	 * @param xlow the lowest value of x
	 * @param ylow the lowest value of y
	 * @param xhigh the highest value of x
	 * @param yhigh the highest value of y
	 * @param ts the start time of trajectory
	 * @param te the end time of trajectory
	 */
	
	public MBR(double xlow, double ylow, double xhigh, double yhigh, long ts, long te){
		this.xlow = xlow;
		this.xhigh = xhigh;
		this.ylow = ylow;
		this.yhigh = yhigh;
		this.te = te;
		this.ts = ts;
	}

	
	/*
	 * A serious operators for set/get MBR's values
	 * 
	 */

	public double getXhigh() {
		return xhigh;
	}


	public void setXhigh(double xhigh) {
		this.xhigh = xhigh;
	}


	public double getYhigh() {
		return yhigh;
	}


	public void setYhigh(double yhigh) {
		this.yhigh = yhigh;
	}


	public long getTs() {
		return ts;
	}


	public void setTs(long ts) {
		this.ts = ts;
	}


	public long getTe() {
		return te;
	}


	public void setTe(long te) {
		this.te = te;
	}


	public double getXlow() {
		return xlow;
	}


	public double getYlow() {
		return ylow;
	}

}
