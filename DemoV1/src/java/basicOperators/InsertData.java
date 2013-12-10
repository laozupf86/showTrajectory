package basicOperators;

import java.util.ArrayList;

import dataS.SamplePoint;

/**
 * An interface used to insert record
 * @author uqhwan21
 *
 */

public interface InsertData {
	
	
	
	/**
	 * 
	 * Insert a trajectory into database
	 * @param tid the id of trajectory
	 * @param data the sample point of inserting trajectory
	 * @return true for success
	 */
	public boolean InsertSingleRecord(int tid, ArrayList<SamplePoint> data);
	
	
	/**
	 * Not used yet
	 * @return
	 */
	
	public boolean InsertMultipleRecords();
	/**
	 * used for import a lot of data once
	 * @param data a bulk of data read from other source
	 * @return true, if importing is successfully 
	 */
	public boolean importBulkofData(ArrayList<ArrayList<SamplePoint>> data);

}
