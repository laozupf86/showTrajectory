package deltaSegmentedTable;

import dataS.MBR;
import dataS.SamplePointList;

/**
 * A row of the segment table, <sid, mbr, trajectory segment>
 * @author Haozhou
 *
 */

public class SegmentTableRow {
	
	private int sid;
	private MBR mbr;
	private SamplePointList data;
	
	/**
	 * 
	 * @param sid segment id
	 * @param mbr MBR
	 * @param data the delta encoded trajectory, the next point are delta encoded by the first point in this segment 
	 */
	public SegmentTableRow(int sid, MBR mbr, SamplePointList data){
		
		this.sid = sid;
		this.mbr = mbr;
		this.data = data;
		
	}


	public int getSid() {
		return sid;
	}


	public void setSid(int sid) {
		this.sid = sid;
	}


	public MBR getMbr() {
		return mbr;
	}


	public void setMbr(MBR mbr) {
		this.mbr = mbr;
	}


	public SamplePointList getData() {
		return data;
	}


}
