package frameTable;

import java.util.HashMap;

import dataS.IFramePoint;

/**
 * A "table" stores all frames for frame table.
 * @author Haozhou
 *
 */


public class FrameList {
	
	protected HashMap<Integer, IFrame> frames;
	private long standardTime;
	/**
	 * 
	 * @param inputTime the standard time
	 * @param inputInterval the interval of time slot
	 */
	
	public FrameList(long inputTime, int inputInterval){
		this.standardTime = inputTime;
		this.frames = new HashMap<Integer, IFrame>();
		
		
	}

	public long getStandardTime() {
		return standardTime;
	}
	
	/**
	 * Add a frame point into selected frame
	 * @param fid id of frame
	 * @param f the frame point
	 * @return the index of this frame point, after add to array
	 */
	public int addNewPointToFrame(int fid, IFramePoint f){
		if (!this.frames.containsKey(fid)){
			this.frames.put(fid, new IFrame());
			return this.frames.get(fid).addNewPoint(f);
		}else{	
			return this.frames.get(fid).addNewPoint(f);
		}
	}
	
	/**
	 * Check whether there is frame with given fid
	 * @param fid id of frame
	 * @return true for exist
	 */
	public boolean checkFrame(int fid){
		return frames.containsKey(fid);
	}
	
	
	/**
	 * Get the frame array with fid
	 * @param fid id of frame
	 * @return array
	 */
	public IFrame getFrameByTimeSlot(int fid){
		
		return this.frames.get(fid);
		
	}
	/**
	 * Remove an frame point with fid and its index
	 * @param fid id of frame
	 * @param index id of frame
	 * @return removed frame point
	 */
	public IFramePoint removeFramePointByLocation(int fid, int index){
		return this.frames.get(fid).removePointbyIndex(index);
	}
	
	
	
	

}
