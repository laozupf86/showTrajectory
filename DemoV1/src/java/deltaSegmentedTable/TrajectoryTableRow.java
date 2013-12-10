package deltaSegmentedTable;


/**
 * delta encoded Trajectory table row, same with segment table row
 * @author Haozhou
 *
 */
public class TrajectoryTableRow {
	
	private int tid;
	private int sindex;
	private int sid;
	
	
	public TrajectoryTableRow(int tid, int sindex, int sid){
		this.tid = tid;
		this.sindex = sindex;
		this.sid = sid;
	}


	public int getTid() {
		return tid;
	}


	public void setTid(int tid) {
		this.tid = tid;
	}


	public int getSindex() {
		return sindex;
	}


	public void setSindex(int sindex) {
		this.sindex = sindex;
	}


	public int getSid() {
		return sid;
	}


	public void setSid(int sid) {
		this.sid = sid;
	}
	
	

}
