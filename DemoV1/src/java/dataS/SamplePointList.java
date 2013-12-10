package dataS;

import java.util.ArrayList;

/**
 * a data structure used to store the delta encoded sample point 
 * @author Haozhou
 *
 */

public class SamplePointList {
	
	
	private SamplePoint startPoint;
	private ArrayList<DeltaCode> dc;
	
	/**
	 * 
	 * @param startPoint the original sample point
	 * @param dc the delta encoded point list based on startPoint
	 */
	
	public SamplePointList(SamplePoint startPoint, ArrayList<DeltaCode> dc){
		this.startPoint = startPoint;
		this.dc = dc;
	}

	public SamplePoint getStartPoint() {
		return startPoint;
	}

	public ArrayList<DeltaCode> getDc() {
		return dc;
	}
	
	

}
