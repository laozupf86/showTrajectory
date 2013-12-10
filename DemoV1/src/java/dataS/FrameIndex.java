package dataS;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A grid index structure to index the points in each I frame
 * @author uqhwan21
 *
 */

public class FrameIndex {
	
	/*
	 * A HashMap used to store gird indexes for a Iframe in each time slot
	 * the key is the id of grid cell, and list is the index of the points in iframe.
	 */
	private HashMap<Integer, ArrayList<Integer>> indexI;
	
	
	public FrameIndex(){
		
		this.indexI = new HashMap<Integer, ArrayList<Integer>>(); 
		
	}

	
	/**
	 * Insert a point into the index with the id of grid cell
	 * @param cellID the id of grid cell
	 * @param findex the index of point in the frame list
	 */
	public void gridInsert(int cellID, int findex){
		if(this.indexI.containsKey(cellID)){
			this.indexI.get(cellID).add(findex);
		}else{
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.add(findex);
			this.indexI.put(cellID, temp);
		}
	}
	
	/**
	 * Get the list of indexes for given cell id
	 * @param cellID the id of grid cell
	 * @return the list of indexes
	 */
	public ArrayList<Integer> getFindex(int cellID){
		return this.indexI.get(cellID);
	}
	
	
	/**
	 * Check whether there is any index in the given grid cell
	 * @param cellID the id of grid cell
	 * @return true for there is at least one index in this grid cell, otherwise false.
	 */
	public boolean checkGridIndex(int cellID){
		return this.indexI.containsKey(cellID);
	}
	


}
