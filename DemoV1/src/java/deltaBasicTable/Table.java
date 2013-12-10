package deltaBasicTable;

//import java.util.HashMap;
import java.util.ArrayList;
import dataS.SamplePointList;

/**
 * The delta encoded basic table, include <tid, mbr, #point, encoded trajectory>
 * same with basicTable.Table
 * @author Haozhou
 *
 */


public class Table {
	
	private ArrayList<TableRow> rowRecord;
	
	public Table(){
		this.rowRecord = new ArrayList<TableRow>();
	}
	
	
	public void addNewRecord(TableRow newrecord){
		
		this.rowRecord.add(newrecord);
		
	}
	
	
	
	/**
	 * Select a single row by tid
	 * @param tid id of trajectory
	 * @return the data of trajectory with that tid
	 * 
	 */
	public SamplePointList getPointsByTID(int tid){
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
	public SamplePointList removePointsByTID(int tid){
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
