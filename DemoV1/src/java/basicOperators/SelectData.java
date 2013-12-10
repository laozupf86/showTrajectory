package basicOperators;

import dataS.Trajectory;

/**
 * A interface for selection operation 
 * @author uqhwan21
 *
 */

public interface SelectData {
	
	
	
	/**
	 * Select a trajectory by its id
	 * @param tid the id of trajectory
	 * @return trajectory
	 */
	public Trajectory selectOneRecordByTID(int tid);
	
	

}
