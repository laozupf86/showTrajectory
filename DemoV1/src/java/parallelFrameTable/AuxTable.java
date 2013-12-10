package parallelFrameTable;

import java.util.ArrayList;

/**
 * An auxiliary table used to store additional information.
 * @author Haozhou
 *
 */

public class AuxTable {
	
	private ArrayList<AuxTableRow> data;
	
	public AuxTable(){
		data = new ArrayList<AuxTableRow>();
	}
	
	/**
	 * Add an auxiliary row
	 * @param atr the auxiliary row
	 */
	public void addNewRecord(AuxTableRow atr){
		this.data.add(atr);
	}
	
	/**
	 * Get an auxiliary row by tid 
	 * @param tid the trajectory id
	 * @return the auxiliary row
	 */
	public AuxTableRow getRowbyTid(int tid){
		for (AuxTableRow atr : this.data){
			if (atr.getTid() == tid){
				return atr;
			}
		}
		return null;
	}
	
	/**
	 * Remove an auxiliary row by tid 
	 * @param tid the trajectory id
	 * @return the auxiliary row
	 */
	public AuxTableRow removeRowByTid(int tid){
		
		for (int i = 0; i < this.data.size(); i++){
			if (this.data.get(i).getTid() == tid){
				return this.data.remove(i);
			}
		}
		
		return null;
	}
	

}
