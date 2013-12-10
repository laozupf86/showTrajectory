package dataS;

import java.util.ArrayList;

/**
 * A data package used as a returned object as the query result/s 
 * 
 * @author uqhwan21
 *
 */

public class Trajectory {
	
	private int tid;
	private ArrayList<SamplePoint> data;
	private int startSlot = 0;
	
	/**
	 * initial the object with tid only
	 * @param tid the id of trajectory
	 */
	public Trajectory(int tid){
		this.tid = tid;
		data = new ArrayList<SamplePoint>();
	}
	
	/**
	 * initial the object with tid and trajectory size
	 * @param tid the id of trajectory
	 * @param size the number of sample points in this trajectory
	 */
	public Trajectory(int tid, int size){
		this.tid = tid;
		data = new ArrayList<SamplePoint>(size);
	}
	
	/**
	 * initial the object with tid, trajectory size and 
	 * @param tid the id of trajectory
	 * @param size size the number of sample points in this trajectory
	 * @param startSlot the time slot id of this trajectory start with
	 */
	public Trajectory (int tid, int size, int startSlot){
		this.tid = tid;
		data = new ArrayList<SamplePoint>(size);
		this.startSlot = startSlot;
	}
	
	/**
	 * Add a result sample point into this package
	 * @param sp a sample point
	 */
	public void addSamplePoint(SamplePoint sp){
		this.data.add(sp);
	}
	
	/**
	 * trim the list
	 */
	public void trimList(){
		this.data.trimToSize();
	}
	
	/**
	 * get the size of this trajectory
	 * @return the size of the trajectory
	 */
	public int getTrajectotySize(){
		return this.data.size();
	}
	
	/**
	 * get the array of sample points in this trajectory
	 * @return the sample points
	 */

	public ArrayList<SamplePoint> getTrajectory() {
		return data;
	}

	public int getTid() {
		return tid;
	}
	
	/**
	 * set the point by given a index
	 * @param index the index of the point 
	 * @param p the point will exchange the current sample point
	 */
	
	public void setPoint(int index, SamplePoint p){
		this.data.set(index, p);
	}

	/**
	 * get the start time slot id
	 * @return time slot id
	 */
	public int getStartSlot() {
		return startSlot;
	}
	
	
	

}
