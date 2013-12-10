package frameTable;

import java.util.HashMap;
import java.util.Set;

import dataS.IFramePoint;
import dataS.PFramePoint;

/**
 * The I/P frame table structure
 * @author uqhwan21
 *
 */
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
	public int addIFramePoint(IFramePoint f, int fid){
		
		if (!this.iframes.containsKey(fid)){
			this.iframes.put(fid, new IFrame());
			return this.iframes.get(fid).addNewPoint(f);
		}else{
			
			return this.iframes.get(fid).addNewPoint(f);
		}
	}
	
	/**
	 * Add a Pframe point into selected Pframe
	 * @param p the Pframe point
	 * @param pid the id of Pframe
	 * @return true for success
	 */
	public boolean addPFramePoint(PFramePoint p, int pid){
		
		if (!this.pframes.containsKey(pid)){
			this.pframes.put(pid, new PFrame());
			this.pframes.get(pid).addNewPoint(p);
			return true;
		}else{
			this.pframes.get(pid).addNewPoint(p);
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
	 * Check whether there is Iframe with given fid
	 * @param fid id of Iframe
	 * @return true for exist
	 */
	public boolean containIframeByFID(int fid){
		return this.iframes.containsKey(fid);
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
	 * Check whether there is Pframe with given pid
	 * @param pid id of Pframe
	 * @return true for exist
	 */
	public boolean containPframeByPID(int pid){
		return this.pframes.containsKey(pid);
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
	
	public Set<Integer> getIframeTimeSlotSet(){
		return this.iframes.keySet();
	}
	
	public Set<Integer> getPframeTimeSlotSet(){
		return this.pframes.keySet();
	}

}
