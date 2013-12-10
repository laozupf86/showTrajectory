package parallelFrameTable;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveTask;

import commonMethods.IsWithinBoundingBox;
import commonMethods.TimeOperation;

import dataS.IFramePoint;
import dataS.SamplePoint;

public class STWindowQuerySearchTask extends RecursiveTask<Integer>{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5541066639193528635L;
	private int interval;
	private DualFrameList dfl;
	private int n;
	private int sf;
	private int ef;
	private double maxSpeed;
	
	
	private double x;
	private double y;
	
	
	private ConcurrentHashMap<Integer, Trajectory> result;
	private int numberOfPoint;
	private double radius;
	private int index;
	private long standardTime;
	/**
	 * initial the parallel compute spatial-temporal window query search
	 * @param dfl the I/P frame table reference
	 * @param interval the time interval of time slot
	 * @param standardTime the time interval of time slot
	 * @param n the number of frames for each I/P frame group
	 * @param startFrame the id of Iframe for start search
	 * @param endFrame the id of Iframe for end search
	 * @param x the x value of query point
	 * @param y the y value of query point
	 * @param maxSpeed the max speed of moving object
	 */
	public STWindowQuerySearchTask(DualFrameList dfl, int interval, long standardTime, int n, int startFrame, int endFrame, 
			double maxSpeed, double x, double y, ConcurrentHashMap<Integer, Trajectory> result, int numberOfPoint, double radius, int index){
		super();
		this.dfl = dfl;
		this.interval = interval;
		this.n = n;
		this.sf = startFrame;
		this.ef = endFrame;
		this.maxSpeed = maxSpeed;
		this.x = x;
		this.y = y;
		this.result = result;
		this.numberOfPoint = numberOfPoint;
		this.radius = radius;
		this.index = index;
		this.standardTime = standardTime;
		
	}
	
	
	
	@Override
	public Integer compute() {
		
		
		double maxMovedistance = (this.interval/1000) * (this.n - 1) * maxSpeed;
		
		for (int i = this.sf; i < this.ef; i++){
			int c = 0;
			
			
			if(!dfl.containIframeByFID(i)){
				continue;
			}
			
			for (IFramePoint fp : dfl.getIFrameByFID(i).getPoints()){
				int currentTimeSlot = (i - 1)*this.n;
				
				if(IsWithinBoundingBox.isWithinBoundingBox(fp.getX(), fp.getY(), x, y, radius)){
					if(commonMethods.Distance.getDistance(x, y, fp.getX(), fp.getY()) < radius){
						if (result.containsKey(fp.getTid())){
							result.get(fp.getTid()).setPoint(index, new SamplePoint(fp.getX(), fp.getY(), 
									commonMethods.TimeOperation.getTimeBySlot(this.standardTime, this.interval ,currentTimeSlot)));
							
							index++;
						}else{
							Trajectory temp = new Trajectory(fp.getTid(), this.numberOfPoint);
							temp.setPoint(index, new SamplePoint(fp.getX(), fp.getY(), 
									commonMethods.TimeOperation.getTimeBySlot(this.standardTime, this.interval ,currentTimeSlot)));
							result.put(fp.getTid(), temp);
							index++;
						}
					}
				}else if(IsWithinBoundingBox.isWithinBoundingBox(fp.getX(), fp.getY(), x, y, radius + maxMovedistance)){
					
					int pSlot = (i - 1)*(this.n - 1);
					
					
					for (int j = 1; j < this.n; j++){
						
						int shiftingCode = dfl.getPFrameByPID(pSlot + j).getPointByPID(c);
						
						short dx = (short) (shiftingCode >> 16);
						short dy = (short) (shiftingCode & 0xFFFF);					
						
						if (dx == Short.MAX_VALUE && dy == Short.MIN_VALUE){
							continue;
						}
						
						double px = fp.getX() + ((double)(dx)/1000000);
						double py = fp.getY() + ((double)(dy)/1000000);
						
						double pDistance = commonMethods.Distance.getDistance(x, y, px, py);
						
						if(pDistance < radius){
							if (result.containsKey(fp.getTid())){
								result.get(fp.getTid()).setPoint(index, new SamplePoint(x, y, 
										commonMethods.TimeOperation.getTimeBySlot(this.standardTime, this.interval ,currentTimeSlot)));
								
								index++;
							}else{
								Trajectory temp = new Trajectory(fp.getTid(), this.numberOfPoint);
								temp.setPoint(index, new SamplePoint(x, y, 
										commonMethods.TimeOperation.getTimeBySlot(this.standardTime, this.interval ,currentTimeSlot)));
								result.put(fp.getTid(), temp);
								index++;
							}
						}
						
						
					}
					
				}
				
				c++;
				
			}
		}
		
		return 0;
	}

}
