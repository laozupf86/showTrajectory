package advantageOperators;

import dataS.Trajectory;
/**
 * A interface to identify the temporal NN search
 * @author WANG Haozhou
 *
 */


public interface NNSearch {
	
	/**
	 * Processing temporal NN search by detecting the nearest sampling point 
	 * @param x the x value of query point
	 * @param y the y value of query point
	 * @param startTime the start time of search time interval
	 * @param endTime the end time of search time interval
	 * @param maxSpeed the max speed of moving object
	 * @return the nearest trajectory
	 */
	public Trajectory findNN(double x, double y, long startTime, long endTime, double maxSpeed);
	
	/**
	 * Processing temporal NN search by line interpolation, the input and output is same as findNN 
	 * @param x
	 * @param y
	 * @param startTime
	 * @param endTime
	 * @param maxSpeed
	 * @return the nearest trajectory
	 */
	
	
	public Trajectory findNNByLine(double x, double y, long startTime, long endTime, double maxSpeed);

}
