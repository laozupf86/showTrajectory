package parallelFrameTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.RecursiveTask;

import dataS.IFramePoint;

/**
 * The main NN search task with index for parallel computing
 * @author Haozhou
 *
 */
public class NNSearchTaskWithGridIndex extends RecursiveTask<NNResult>{
	
	
	
	
	
	private static final long serialVersionUID = 5222098316830312683L;
	private int interval;
	private DualFrameList dfl;
	private int n;
	private int sf;
	private int ef;
	private double maxSpeed;
	
	
	private double x;
	private double y;
	
	//gird index variable
	private GridIndex gindex;
	private double xcentroid;
	private double ycentroid;
	private double xlength;
	private double ylength;
	//
	

	/**
	 * initial the parallel compute with index for NN search
	 * @param dfl the I/P frame table reference
	 * @param interval the time interval of time slot
	 * @param standardTime the time interval of time slot
	 * @param n the number of frames for each I/P frame group
	 * @param startFrame the id of Iframe for start search
	 * @param endFrame the id of Iframe for end search
	 * @param maxSpeed the max speed of moving object
	 * @param x the x value of query point
	 * @param y the y value of query point
	 * @param gindex the grid index reference
	 * @param xcentroid the gird center/start point location
	 * @param ycentroid the gird center/start point location
	 * @param xlength the length of each grid cell
	 * @param ylength the length of each grid cell
	 */
	public NNSearchTaskWithGridIndex(DualFrameList dfl, int interval, long standardTime, int n, int startFrame, int endFrame, 
			double maxSpeed, double x, double y, 
			GridIndex gindex, double xcentroid, double ycentroid, double xlength, double ylength){
		
		super();
		this.dfl = dfl;
		this.interval = interval;
		this.n = n;
		this.sf = startFrame;
		this.ef = endFrame;
		this.maxSpeed = maxSpeed;
		this.x = x;
		this.y = y;
		
		this.gindex = gindex;
		this.xcentroid = xcentroid;
		this.ycentroid = ycentroid;
		this.xlength = xlength;
		this.ylength = ylength;
		
		
		
	}
	
	
	@Override
	public NNResult compute() {
		// TODO Auto-generated method stub
		
		double distanceBestSoFar = Double.POSITIVE_INFINITY;
		int bestSoFarTid = -1;
		NNResult result;
		
		
		double maxMovedistance = (this.interval/1000) * (this.n - 1) * maxSpeed;
		
		HashMap<Integer, Double> seekedGrid;
		ArrayList<Integer> waitingList;
		
		
		int xGrid = (int)((x - this.xcentroid)/this.xlength);
		int yGrid = (int)((y - this.ycentroid)/this.ylength);
		
		
		for (int i = this.sf; i < this.ef; i++){
			
			if(!dfl.containIframeByFID(i)){
				continue;
			}
			
			seekedGrid = new HashMap<Integer, Double>();
			waitingList = new ArrayList<Integer>();
			
			int startGrid = this.buildIndexCode(xGrid, yGrid);
			
			
			
			
			waitingList.add(startGrid);
			seekedGrid.put(startGrid, 0.0);
			
			
			while(!waitingList.isEmpty()){
				
				int searchGrid = waitingList.remove(0);
				ArrayList<Integer> iframePointIndex = this.gindex.getPointArray(i, searchGrid);
				
				
				//search
				if (iframePointIndex != null){
					ArrayList<IFramePoint> fpl = dfl.getIFrameByFID(i).getPoints();
					
					for (int findex : iframePointIndex){
						
						
						
						IFramePoint fp = fpl.get(findex);
						double distance = commonMethods.Distance.getDistance(x, y, fp.getX(), fp.getY());
						
						if (distance  < distanceBestSoFar){
							
							int pSlot = (i - 1)*(this.n - 1);
							
							distanceBestSoFar = distance;
							bestSoFarTid = fp.getTid();
							
							for (int j = 1; j < this.n; j++){
								int shiftingCode = dfl.getPFrameByPID(pSlot + j).getPointByPID(findex);
								
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
								int shiftingCode = dfl.getPFrameByPID(pSlot + j).getPointByPID(findex);
								
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
						}else{
							//continue;
						}
						
					}
					
				}
				
				
				//put other grid/s into waiting list, if it better than best so far
				
				int searchx = searchGrid >> 16;
				int searchy = searchGrid & 0xFFFF;
				
				double searchGridXBottomLeft = this.xcentroid + searchx*this.xlength;
				double searchGridYBottomLeft = this.ycentroid + searchy*this.ylength;
				
				double searchGridXTopLeft = searchGridXBottomLeft;
				double searchGridYTopLeft = this.ycentroid + (searchy + 1)*this.ylength;
				
				double searchGridXTopRight = this.xcentroid + (searchx + 1)*this.xlength;
				double searchGridYTopRight = this.ycentroid + (searchy + 1)*this.ylength;
				
				double searchGridXBottomRight = this.xcentroid + (searchx + 1)*this.xlength;;
				double searchGridYBottomRight = searchGridYBottomLeft;
				
				double d1 = commonMethods.Distance.getDistance(x, y, searchGridXBottomLeft, searchGridYBottomLeft);
				double d3 = commonMethods.Distance.getDistance(x, y, searchGridXTopLeft, searchGridYTopLeft);
				double d5 = commonMethods.Distance.getDistance(x, y, searchGridXTopRight, searchGridYTopRight);
				double d7 = commonMethods.Distance.getDistance(x, y, searchGridXBottomRight, searchGridYBottomRight);
				
				double d2 = commonMethods.Distance.calculateLineDistance(x, y, searchGridXBottomLeft, searchGridYBottomLeft, searchGridXTopLeft, searchGridYTopLeft);
				double d4 = commonMethods.Distance.calculateLineDistance(x, y, searchGridXTopLeft, searchGridYTopLeft, searchGridXTopRight, searchGridYTopRight);
				double d6 = commonMethods.Distance.calculateLineDistance(x, y, searchGridXTopRight, searchGridYTopRight, searchGridXBottomRight, searchGridYBottomRight);
				double d8 = commonMethods.Distance.calculateLineDistance(x, y, searchGridXBottomRight, searchGridYBottomRight, searchGridXBottomLeft, searchGridYBottomLeft);
				
				int indexcode;
				
				//double bestDistance;
				
				
				
				//System.out.println("best dis is " + maxMovedistance);
				if (d1 - distanceBestSoFar <= maxMovedistance){
					indexcode = this.buildIndexCode(searchx - 1, searchy - 1);
					if ((searchx - 1) >= 0 && (searchy - 1) >= 0){
						if (!seekedGrid.containsKey(indexcode)){
							waitingList.add(indexcode);
						}
						
					}
					if (!seekedGrid.containsKey(indexcode)){
						seekedGrid.put(indexcode, d1);
					}
					
					
				}else{
					indexcode = this.buildIndexCode(searchx - 1, searchy - 1);
					if (!seekedGrid.containsKey(indexcode)){
						seekedGrid.put(indexcode, d1);
					}
					
				}
				
				
				if (d2 - distanceBestSoFar <= maxMovedistance){
					indexcode = this.buildIndexCode(searchx - 1, searchy);
					if ((searchx - 1) >= 0 ){
						if (!seekedGrid.containsKey(indexcode)){
							waitingList.add(indexcode);
						}
					}
					if (!seekedGrid.containsKey(indexcode)){
						seekedGrid.put(indexcode, d2);
					}
					
					
				}else{
					indexcode = this.buildIndexCode(searchx - 1, searchy);
					if (!seekedGrid.containsKey(indexcode)){
						seekedGrid.put(indexcode, d2);
					}
					
				}
				
				
				if (d3 - distanceBestSoFar <= maxMovedistance){
					indexcode = this.buildIndexCode(searchx - 1, searchy + 1);
					if ((searchx - 1) >= 0 ){
						if (!seekedGrid.containsKey(indexcode)){
							waitingList.add(indexcode);
						}
					}
					if (!seekedGrid.containsKey(indexcode)){
						seekedGrid.put(indexcode, d3);
					}
					
					
				}else{
					indexcode = this.buildIndexCode(searchx - 1, searchy + 1);
					if (!seekedGrid.containsKey(indexcode)){
						seekedGrid.put(indexcode, d3);
					}
					
				}
				
				
				if (d4 - distanceBestSoFar <= maxMovedistance){
					indexcode = this.buildIndexCode(searchx, searchy + 1);
					if (!seekedGrid.containsKey(indexcode)){
						waitingList.add(indexcode);
					}
					if (!seekedGrid.containsKey(indexcode)){
						seekedGrid.put(indexcode, d4);
					}
					
					
				}else{
					indexcode = this.buildIndexCode(searchx , searchy + 1);
					if (!seekedGrid.containsKey(indexcode)){
						seekedGrid.put(indexcode, d4);
					}
					
				}
				
				
				if (d5 - distanceBestSoFar <= maxMovedistance){
					indexcode = this.buildIndexCode(searchx + 1, searchy + 1);
					if (!seekedGrid.containsKey(indexcode)){
						waitingList.add(indexcode);
					}
					if (!seekedGrid.containsKey(indexcode)){
						seekedGrid.put(indexcode, d5);
					}
					
					
				}else{
					indexcode = this.buildIndexCode(searchx + 1 , searchy + 1);
					if (!seekedGrid.containsKey(indexcode)){
						seekedGrid.put(indexcode, d5);
					}
					
				}
				
				
				if (d6 - distanceBestSoFar <= maxMovedistance){
					indexcode = this.buildIndexCode(searchx + 1, searchy);
					if (!seekedGrid.containsKey(indexcode)){
						waitingList.add(indexcode);
					}
					if (!seekedGrid.containsKey(indexcode)){
						seekedGrid.put(indexcode, d6);
					}
					
					
				}else{
					indexcode = this.buildIndexCode(searchx + 1 , searchy);
					if (!seekedGrid.containsKey(indexcode)){
						seekedGrid.put(indexcode, d6);
					}
					
				}
				
				
				if (d7 - distanceBestSoFar <= maxMovedistance){
					indexcode = this.buildIndexCode(searchx + 1, searchy - 1);
					if ((searchy - 1) >= 0 ){
						if (!seekedGrid.containsKey(indexcode)){
							waitingList.add(indexcode);
						}
					}
					if (!seekedGrid.containsKey(indexcode)){
						seekedGrid.put(indexcode, d7);
					}
					
					
				}else{
					indexcode = this.buildIndexCode(searchx + 1, searchy - 1);
					if (!seekedGrid.containsKey(indexcode)){
						seekedGrid.put(indexcode, d7);
					}
					
				}
				
				
				if (d8 - distanceBestSoFar <= maxMovedistance){
					indexcode = this.buildIndexCode(searchx, searchy - 1);
					if ((searchy - 1) >= 0 ){
						if (!seekedGrid.containsKey(indexcode)){
							waitingList.add(indexcode);
						}
					}
					if (!seekedGrid.containsKey(indexcode)){
						seekedGrid.put(indexcode, d8);
					}
					
					
				}else{
					indexcode = this.buildIndexCode(searchx, searchy - 1);
					if (!seekedGrid.containsKey(indexcode)){
						seekedGrid.put(indexcode, d8);
					}
					
				}
				
				
				
				
				
			}
			
			
		}
		
		result = new NNResult(distanceBestSoFar, bestSoFarTid);
		
		//System.out.println("tid is " + bestSoFarTid);
		//System.out.println("distance is " + distanceBestSoFar);
		//return this.selectOneRecordByTID(0);
		return result;
	}
	
	
	private int buildIndexCode(int x, int y){
		return (x << 16) | (y & 0xFFFF);
	}

}
