package dataS;

/**
 * A delta data structure used to store delta shifting
 * @author Haozhou
 *
 */

public class DeltaCode {
	
	private short dx;
	private short dy;
	private short dt;
	
	/**
	 * 
	 * @param x the delta shifting of x
	 * @param y the delta shifting of y
	 * @param t the delta shifting of t
	 */
	public DeltaCode(short x, short y, short t){
		this.dx = x;
		this.dy = y;
		this.dt = t;
	}


	public short getDx() {
		return dx;
	}


	public void setDx(short dx) {
		this.dx = dx;
	}


	public short getDy() {
		return dy;
	}


	public void setDy(short dy) {
		this.dy = dy;
	}


	public short getDt() {
		return dt;
	}


	public void setDt(short dt) {
		this.dt = dt;
	}


	

	
	
	

}
