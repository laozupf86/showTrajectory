package frameTable;

import java.util.ArrayList;

import dataS.IFramePoint;

/**
 * A single Iframe
 * @author Haozhou
 *
 */

public class IFrame {
	
	
	private ArrayList<IFramePoint> points;
	
	public IFrame(){
		this.points = new ArrayList<IFramePoint>(10000);
	}
	
	/**
	 * Add an new Iframe point
	 * @param p Iframe point
	 * @return the index of this new Iframe point
	 */
	public int addNewPoint(IFramePoint p){
		this.points.add(p);
		return this.points.size() - 1;
	}

	/**
	 * get this Iframe array
	 * @return Iframe array
	 */
	public ArrayList<IFramePoint> getPoints() {
		return points;
	}
	
	/**
	 * Remove an Iframe point by given index
	 * @param index the index of this Iframe point
	 * @return removed Iframe points
	 */
	public IFramePoint removePointbyIndex(int index){
		return this.points.remove(index);
	}
	

}
