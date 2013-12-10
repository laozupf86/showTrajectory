package segmentedTable;

import java.util.ArrayList;

import dataS.MBR;
import dataS.SamplePoint;

/**
 * A row of the segment table, <sid, mbr, trajectory segment>
 * @author Haozhou
 *
 */

public class SegmentTableRow {
	
	private int sid;
	private MBR mbr;
	private ArrayList<SamplePoint> data;
	
	/**
	 * 
	 * @param sid the segment id
	 * @param mbr MBR
	 * @param data the trajectory segment
	 */
	public SegmentTableRow(int sid, MBR mbr, ArrayList<SamplePoint> data){
		
		this.sid = sid;
		this.mbr = mbr;
		this.data = data;
		
	}


	public int getSid() {
		return sid;
	}




	public MBR getMbr() {
		return mbr;
	}


	public ArrayList<SamplePoint> getData() {
		return data;
	}

	

}
