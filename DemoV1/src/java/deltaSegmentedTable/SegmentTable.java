package deltaSegmentedTable;

import java.util.ArrayList;


/**
 * The segment table
 * @author Haozhou
 *
 */

public class SegmentTable {
	
	private ArrayList<SegmentTableRow> data;
	
	
	
	public SegmentTable(){
		this.data = new ArrayList<SegmentTableRow>();
	}
	
	public void addNewRecord(SegmentTableRow newrecord){
		this.data.add(newrecord);
	}
	
	public int getLastSID(){
		if (this.data.isEmpty()){
			return 0;
		}
		return this.data.get(this.data.size() - 1).getSid();
	}
	
	public ArrayList<SegmentTableRow> getData(){
		return this.data;
	}
	
	public SegmentTableRow removeRowByIndex(int index){
		return this.data.remove(index);
	}

}
