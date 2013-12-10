package segmentedTable;

import java.util.ArrayList;


/**
 * The segment table
 * @author Haozhou
 *
 */

public class SegmentTable {
	
	private ArrayList<SegmentTableRow> data;
	
	/**
	 * A array used to store all of rows in the table.
	 */
	
	public SegmentTable(){
		this.data = new ArrayList<SegmentTableRow>();
	}
	
	/**
	 * Add a new segment result
	 * @param newrecord the new row for segment table
	 */
	public void addNewRecord(SegmentTableRow newrecord){
		this.data.add(newrecord);
	}
	
	/**
	 * Get the last id used in segment table
	 * @return sid
	 */
	public int getLastSID(){
		if (this.data.isEmpty()){
			return 0;
		}
		return this.data.get(this.data.size() - 1).getSid();
	}

	/**
	 * Get whole table data
	 * @return table
	 */
	public ArrayList<SegmentTableRow> getData() {
		return data;
	}
	
	/**
	 * Remove a row by given sid
	 * @param sid sid
	 * @return table row
	 */
	public SegmentTableRow removeRowByIndex(int sid){
		return this.data.remove(sid);
	}

	
}
