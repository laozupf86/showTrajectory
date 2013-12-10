package segmentedTable;


/**
 * A trajectory table row, <tid, sindex, sid>
 * @author Haozhou
 *
 */
public class TrajectoryTableRow {
	
	private int tid;
	private int sindex;
	private int sid;
	
	/**
	 * 
	 * @param tid the trajectory id
	 * @param sindex the segment index of this trajectory id
	 * @param sid the segment point
	 */
	public TrajectoryTableRow(int tid, int sindex, int sid){
		this.tid = tid;
		this.sindex = sindex;
		this.sid = sid;
	}


	public int getTid() {
		return tid;
	}


	public int getSindex() {
		return sindex;
	}



	public int getSid() {
		return sid;
	}
	

}
