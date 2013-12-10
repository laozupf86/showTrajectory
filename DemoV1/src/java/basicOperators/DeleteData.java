package basicOperators;

import dataS.Trajectory;

public interface DeleteData {
	
	/**
	 * delete a record by tid
	 * @param tid the trajectory id for deletion
	 * @return the deleted trajectory
	 */
	
	public Trajectory deleteOneRecordByTID(int tid); 

}
