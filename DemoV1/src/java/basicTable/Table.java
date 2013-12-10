package basicTable;

import java.util.ArrayList;
import dataS.SamplePoint;

/**
 * The basic table, include <tid, mbr, #point, trajectory>
 * @author Haozhou
 *
 */
public class Table {
	
	private ArrayList<TableRow> rowRecord;
	
	/**
	 * A array list used to store the data, the primary key is the tid.
	 */
	public Table(){
		this.rowRecord = new ArrayList<TableRow>();
	}
	
	
	/**
	 * Insert a new record
	 * @param tid id of trajectory
	 * @param newrecord the inserted data
	 * 
	 */
	public void addNewRecord(TableRow newrecord){
		
		this.rowRecord.add(newrecord);
		
	}
	
	
	/**
	 * Select a single row by tid
	 * @param tid id of trajectory
	 * @return the data of trajectory with that tid
	 * 
	 */
	public ArrayList<SamplePoint> getPointsByTID(int tid){
		for (TableRow rr : this.rowRecord){
			if (rr.getTid() == tid){
				return rr.getData();
			}
		}
		
		return null;
		
		
	}
	
	/**
	 * Remove a single row by tid
	 * @param tid id of trajectory
	 * @return the data of trajectory with that tid
	 */
	
	public ArrayList<SamplePoint> removePointsByTID(int tid){
		for (int i = 0; i < this.rowRecord.size(); i++){
			if (this.rowRecord.get(i).getTid() == tid){
				return this.rowRecord.remove(i).getData();
			}
		}
		
		return null;
	}

	/**
	 * get all table contents
	 * @return table
	 */

	public ArrayList<TableRow> getRowRecord() {
		return rowRecord;
	}
	
	

}
