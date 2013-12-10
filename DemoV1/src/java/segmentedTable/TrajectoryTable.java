package segmentedTable;

import java.util.ArrayList;
/**
 * The trajectory table
 * @author Haozhou
 *
 */

public class TrajectoryTable {
	
	ArrayList<TrajectoryTableRow> data;
	
	public TrajectoryTable(){
		this.data = new ArrayList<TrajectoryTableRow>();
	}
	
	/**
	 * Add a new trajectory recode to this table
	 * @param newrecord
	 */
	public void addNewRecord(TrajectoryTableRow newrecord){
		this.data.add(newrecord);
	}

	/**
	 * Get table data
	 * @return table
	 */
	public ArrayList<TrajectoryTableRow> getData() {
		return data;
	}
	
	/**
	 * remove a record by given index
	 * @param index the recorder index
	 * @return trajectory table row
	 */
	public TrajectoryTableRow removeByIndex(int index){
		return this.data.remove(index);
	}

}
