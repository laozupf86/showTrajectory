package deltaBasicTable;

import dataS.MBR;
import dataS.SamplePointList;

/**
 * The attribute of the table, excludes tid.
 * @author Haozhou
 *
 */

public class TableRow {
	
	private int tid;
	private int numofPoints;
	private MBR mbr;
	private SamplePointList data;
	
	/**
	 * 
	 * @param numofPoints the number of point in this trajectory
	 * @param mbr MBR
	 * @param data the delta encoded trajectory, the next point are delta encoded by its previous point 
	 */


	public TableRow(int numofPoints, MBR mbr, SamplePointList data, int tid){
		
		this.tid = tid;
		this.numofPoints = numofPoints;
		this.mbr = mbr;
		this.data = data;
		
	}
	
	
	
	
	public SamplePointList getData() {
		return data;
	}


	public int getNumofPoints() {
		return numofPoints;
	}


	public MBR getMbr() {
		return mbr;
	}

	public int getTid() {
		return tid;
	}

	

}
