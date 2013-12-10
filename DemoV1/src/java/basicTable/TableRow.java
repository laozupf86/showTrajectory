package basicTable;

import java.util.ArrayList;

import dataS.MBR;
import dataS.SamplePoint;

/**
 * A single row of the table
 * @author Haozhou
 *
 */
public class TableRow {
	
	private int tid;
	private int numofPoints;
	private MBR mbr;
	private ArrayList<SamplePoint> data;
	
	
	/**
	 * 
	 * @param numofPoints the total sample points of this trajectory
	 * @param mbr a MBR of this trajectory
	 * @param data trajectory
	 */

	public TableRow(int numofPoints, MBR mbr, ArrayList<SamplePoint> data, int tid){
		this.tid = tid;
		this.numofPoints = numofPoints;
		this.mbr = mbr;
		this.data = data;
		
	}
	
	
	
	
	public ArrayList<SamplePoint> getData() {
		return data;
	}




	public void setData(ArrayList<SamplePoint> data) {
		this.data = data;
	}




	public int getNumofPoints() {
		return numofPoints;
	}


	public void setNumofPoints(int numofPoints) {
		this.numofPoints = numofPoints;
	}


	public MBR getMbr() {
		return mbr;
	}


	public void setMbr(MBR mbr) {
		this.mbr = mbr;
	}




	public int getTid() {
		return tid;
	}

	

}
