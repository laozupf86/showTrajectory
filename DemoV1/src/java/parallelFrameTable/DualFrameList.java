package parallelFrameTable;

import java.util.HashMap;

import dataS.IFramePoint;
import dataS.PFramePoint;

public class DualFrameList {
	
	private HashMap<Integer, IFrame> iframes;
	private HashMap<Integer, PFrame> pframes;
	
	public DualFrameList(){
		
		this.iframes = new HashMap<Integer, IFrame>();
		this.pframes = new HashMap<Integer, PFrame>();
		
	}
	
	/**
	 * Add an Iframe point into selected Iframe
	 * @param f the Iframe point
	 * @param fid the id of Iframe
	 * @return the index of this Iframe point, after add to array
	 */
	public int addIFramePoint(IFramePoint f, int timecode){
		
		if (!this.iframes.containsKey(timecode)){
			this.iframes.put(timecode, new IFrame());
			return this.iframes.get(timecode).addNewPoint(f);
		}else{
			//this.iframes.s
			return this.iframes.get(timecode).addNewPoint(f);
		}
	}
	
	/**
	 * Add a Pframe point into selected Pframe
	 * @param p the Pframe point
	 * @param pid the id of Pframe
	 * @return true for success
	 */
	public boolean addPFramePoint(PFramePoint p, int timecode){
		
		if (!this.pframes.containsKey(timecode)){
			this.pframes.put(timecode, new PFrame());
			this.pframes.get(timecode).addNewPoint(p);
			return true;
		}else{
			this.pframes.get(timecode).addNewPoint(p);
			return true;
		}
	}
	
	/**
	 * Get the number of Iframes
	 * @return number
	 */
	public int getNumOfIfames(){
		return this.iframes.size();
	}
	
	/**
	 * Get the number of Pframes
	 * @return number
	 */
	public int getNumOfPfames(){
		return this.pframes.size();
	}
	
	/**
	 * Get the Iframe array by fid
	 * @param fid id of Iframe
	 * @return Iframe array
	 */
	public IFrame getIFrameByFID(int fid){
		
		return this.iframes.get(fid);
		
	}
	
	/**
	 * Remove an Iframe point with fid and its index
	 * @param fid id of Iframe
	 * @param index index of that Iframe point
	 * @return removed Iframe point
	 */
	
	public IFramePoint removeIFrameByLocation(int fid, int index){
		
		return this.iframes.get(fid).removePointbyIndex(index);
		
	}
	/**
	 * Check whether there is Iframe with given fid
	 * @param fid id of Iframe
	 * @return true for exist
	 */
	
	public boolean containIframeByFID(int fid){
		return this.iframes.containsKey(fid);
	}
	
	/**
	 * Get the Pframe array by pid
	 * @param pid pid id of Pframe
	 * @return Pframe array
	 */
	public PFrame getPFrameByPID(int pid){
		
		return this.pframes.get(pid);
		
	}
	
	/**
	 * Remove a Pframe point with pid and its index
	 * @param pid pid id of Pframe
	 * @param index index of that Pframe point
	 * @return removed Pframe point
	 */
	public int removePframeByLocation(int pid, int index){
		return this.pframes.get(pid).removePointByPID(index);
	}
	

}
