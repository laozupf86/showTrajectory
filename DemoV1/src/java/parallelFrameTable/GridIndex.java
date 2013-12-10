package parallelFrameTable;

import java.util.ArrayList;
import java.util.HashMap;

import dataS.FrameIndex;

public class GridIndex {
	
	//private HashMap<Integer, ArrayList<Integer>> indexI;
	private HashMap<Integer, FrameIndex> gridIndex;
	private double xcentroid;
	private double ycentroid;
	
	private double xsl;
	private double ysl;
	
	/**
	 * 
	 * @param xcentroid the gird center/start point location
	 * @param ycentroid the gird center/start point location
	 * @param xlength the length of each grid cell
	 * @param ylength the length of each grid cell
	 */
	public GridIndex(double xcentroid, double ycentroid,  double xlength, double ylength){
		this.xcentroid = xcentroid;
		this.ycentroid = ycentroid;
		this.xsl = xlength;
		this.ysl = ylength;
		
		this.gridIndex = new HashMap<Integer, FrameIndex>();

	}
	/**
	 * index a new IFrame point
	 * @param fid id of Iframe point
	 * @param findex the index of point in that Iframe
	 * @param x point's coordinate
	 * @param y point's coordinate
	 */
	public void indexApoint(int timeSlot, int findex, double x, double y){
		
		int gx = (int) ((x - this.xcentroid)/this.xsl);
		int gy = (int) ((y - this.ycentroid)/this.ysl);
		
		int index = (gx << 16) | (gy & 0xFFFF);
		
		
		if (gridIndex.containsKey(timeSlot)){
			gridIndex.get(timeSlot).gridInsert(index, findex);
		}else{
			FrameIndex fi = new FrameIndex();
			fi.gridInsert(index, findex);
			gridIndex.put(timeSlot, fi);
		}
	}
	
	/**
	 * get the point array of given fid and its grid cell code
	 * @param fid id of Iframe point
	 * @param cellCode the id of grid cell
	 * @return the array
	 */

	public ArrayList<Integer> getPointArray(int timeSlot, int index){
		return this.gridIndex.get(timeSlot).getFindex(index);
	}



	public double getXLengthPerSlot() {
		return xsl;
	}

	public double getYLengthPerSlot() {
		return ysl;
	}

	public double getXcentroid() {
		return xcentroid;
	}

	public double getYcentroid() {
		return ycentroid;
	}
	
	
	
	
	

}
