package frameTable;

import java.util.ArrayList;

import commonMethods.TimeOperation;


import dataS.SamplePoint;
import dataS.TempFramePoint;
/**
 * Convert a trajectory into frame based trajectory
 * @author Haozhou
 *
 */
public class FrameConvert {
	
	private long startTime = 0;
	private int interval = 0;
	
	/**
	 * 
	 * @param startTime the standard time based on user input. 
	 * @param interval the interval of each time slots
	 */
	
	public FrameConvert(long startTime, int interval){
		this.startTime = startTime;
		this.interval = interval;
				
	}
	
	
	
	/**
	 * Main method to allocate each trajectory into relate time slots. 
	 * @param tid trajectory id
	 * @param sp input trajectory
	 * @param maxJumpSlot maximum slot allowed to do interpolate between two points, if exceeds a jumped information need to insert into.
	 * @return aligned trajectory
	 */
	
	public ArrayList<TempFramePoint> accConvert(int tid, ArrayList<SamplePoint> sp, int maxJumpSlot){
	
		ArrayList<TempFramePoint> iframes = new ArrayList<TempFramePoint>();
		int currentTimeslot = Integer.MIN_VALUE;
		
		for(int i = 0; i < sp.size(); ){
			int tempSlot = TimeOperation.getSlot(this.startTime, this.interval, sp.get(i).getT());
			if (tempSlot <= currentTimeslot){
				i++;
				continue;
			}else{
				if (i == 0){
					//first point
					TempFramePoint tfp = new TempFramePoint(tid, sp.get(i).getX(), sp.get(i).getY(), tempSlot, sp.get(i).getT());
					iframes.add(tfp);
					
					currentTimeslot = tempSlot;
					i++;
				}else if (tempSlot - currentTimeslot == 1){
					//following point
					TempFramePoint tfp = new TempFramePoint(tid, sp.get(i).getX(), sp.get(i).getY(), tempSlot, sp.get(i).getT());
					iframes.add(tfp);
					currentTimeslot++;
					i++;
				}else if (tempSlot - currentTimeslot > 1){
					//jumped point, need interpolate new point
					TempFramePoint tfp = this.addNewFrame(tid, sp, i, currentTimeslot + 1);
					iframes.add(tfp);
					currentTimeslot++;
							
				}else{
					//otherwise
					i++;
				}
			}
		};
		
		
		return iframes;
	}
	


	/**
	 * interpolate new point
	 * @param tid trajectory id 
	 * @param sp input trajectory
	 * @param i index of insert location
	 * @param timeSlot id of time slot
	 * @return new Frame point
	 */

	private TempFramePoint addNewFrame(int tid, ArrayList<SamplePoint> sp, int i, int timeSlot){
		return new TempFramePoint(tid, (sp.get(i - 1).getX() + sp.get(i).getX())/2, (sp.get(i - 1).getY() + sp.get(i).getY())/2, 
				timeSlot, commonMethods.TimeOperation.getTimeBySlot(this.startTime, this.interval, timeSlot));
	}
	
}
