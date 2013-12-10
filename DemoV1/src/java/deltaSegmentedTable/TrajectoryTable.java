package deltaSegmentedTable;

import java.util.ArrayList;

/**
 * delta encoded Trajectory table, same with segment table
 * @author Haozhou
 *
 */

public class TrajectoryTable {
	
	private ArrayList<TrajectoryTableRow> data;
	
	public TrajectoryTable(){
		this.data = new ArrayList<TrajectoryTableRow>();
	}
	
	public void addNewRecord(TrajectoryTableRow newrecord){
		this.data.add(newrecord);
	}

	public ArrayList<TrajectoryTableRow> getData() {
		return data;
	}
	
	public TrajectoryTableRow removeByIndex(int index){
		return this.data.remove(index);
	}
	
	

}
