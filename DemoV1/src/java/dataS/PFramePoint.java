package dataS;

/**
 * Used to store the Pframe point temporarily.
 * @author uqhwan21
 *
 */

public class PFramePoint {
	
	private int tid;
	private short x;
	private short y;
	
	
	/**
	 * 
	 * @param tid the id of trajectory that this point belongs to 
	 * @param x the delta x
	 * @param y the delta y
	 */
	public PFramePoint(int tid, short x, short y){
		this.tid = tid;
		this.x = x;
		this.y = y;
				
		
	}

	public int getTid() {
		return tid;
	}

	public short getX() {
		return x;
	}

	
	public short getY() {
		return y;
	}

	

	
	
	

}
