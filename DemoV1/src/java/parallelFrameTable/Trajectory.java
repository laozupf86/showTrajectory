package parallelFrameTable;

import dataS.SamplePoint;

/**
 * A data package used as a returned object as the query result/s, for parallel computing only 
 * @author Haozhou
 *
 */

public class Trajectory {
	
	private int tid;
	private SamplePoint[] data;
	
	/**
	 * initial the object with tid and trajectory size
	 * @param tid the id of trajectory
	 * @param size the number of sample points in this trajectory
	 */
	public Trajectory(int tid, int size){
		this.tid = tid;
		data = new SamplePoint[size];
	}
	
	/**
	 * get the size of this trajectory
	 * @return the size of the trajectory
	 */
	public int getTrajectotySize(){
		return this.data.length;
	}

	/**
	 * get the array of sample points in this trajectory
	 * @return the sample points
	 */

	public SamplePoint[] getTrajectory() {
		return data;
	}

	
	public int getTid() {
		return tid;
	}
	/**
	 * set the point by given a index
	 * @param index the index of the point 
	 * @param p the point will set the current sample point
	 */
	public void setPoint(int index, SamplePoint p){
		
		this.data[index] = p;
	}
	

}
