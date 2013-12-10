package parallelFrameTable;

import java.util.concurrent.RecursiveTask;

import dataS.IFramePoint;

/**
 * The main NN search task for parallel computing
 * @author Haozhou
 *
 */
public class NNSearchTask extends RecursiveTask<NNResult>{

	
	private static final long serialVersionUID = -4865598885499824272L;
	private int interval;
	private DualFrameList dfl;
	private int n;
	private int sf;
	private int ef;
	private double maxSpeed;
	
	
	private double x;
	private double y;
	
	
	/**
	 * initial the parallel compute NN search
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
	public NNSearchTask(DualFrameList dfl, int interval, long standardTime, int n, int startFrame, int endFrame, 
			double maxSpeed, double x, double y){
		super();
		this.dfl = dfl;
		this.interval = interval;
		this.n = n;
		this.sf = startFrame;
		this.ef = endFrame;
		this.maxSpeed = maxSpeed;
		this.x = x;
		this.y = y;
		
	}
	
	
	
	@Override
	public NNResult compute() {
		
		double distanceBestSoFar = Double.POSITIVE_INFINITY;
		int bestSoFarTid = -1;
		
		double maxMovedistance = (this.interval/1000) * (this.n - 1) * maxSpeed;
		NNResult result;
		
		
		for (int i = this.sf; i < this.ef; i++){
			
			int c = 0;
			
			
			if(!dfl.containIframeByFID(i)){
				continue;
			}
			
			for (IFramePoint fp : dfl.getIFrameByFID(i).getPoints()){
				
				double distance = commonMethods.Distance.getDistance(x, y, fp.getX(), fp.getY());
				
				if (distance  < distanceBestSoFar){
					
					int pSlot = (i - 1)*(this.n - 1);
					
					distanceBestSoFar = distance;
					bestSoFarTid = fp.getTid();
					
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
						
						if (pDistance < distanceBestSoFar){
							distanceBestSoFar = pDistance;
							bestSoFarTid = fp.getTid();
						}
						
												
					}		
					
				}else if ((distance + maxMovedistance)< distanceBestSoFar){
					
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
						
						if (pDistance < distanceBestSoFar){
							distanceBestSoFar = pDistance;
							bestSoFarTid = fp.getTid();
						}
					}
				}
					
				c++;
			}
						
		}
		
		result = new NNResult(distanceBestSoFar, bestSoFarTid);
		
		
		return result;
	}

}
