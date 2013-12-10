package advantageOperators;

import java.util.ArrayList;

import dataS.QueryResultDataStruc;
import dataS.SamplePoint;
import java.util.HashMap;

/**
 * A interface for window query
 * @author Xuefei Li
 *
 */

public interface WindowQueryInterface {
	/**
	 * temporal window query
	 * @param midTime
	 * @param radius
	 */
	public ArrayList<QueryResultDataStruc> TimeWindowQuery(long midTime, long radius);
	
	/**
	 * spatial window query
	 * @param longitude
	 * @param latitude
	 * @param radius
	 */
	public ArrayList<QueryResultDataStruc> SpatialWindowQuery(double longitude, double latitude, double radius) ;
	
	/**
	 * spatial-temporal window query
	 * @param midTime
	 * @param tRadius
	 * @param longitude
	 * @param latitude
	 * @param sRadius
	 */
	public HashMap<Integer, ArrayList<SamplePoint>> SpatialTemporalWindowQuery(long midTime, long tRadius,double longitude, double latitude, double sRadius);

}
