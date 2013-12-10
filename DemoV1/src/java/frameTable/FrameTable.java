package frameTable;

import java.util.ArrayList;
import java.util.HashMap;

import commonMethods.IsWithinBoundingBox;
import commonMethods.TimeOperation;

import dataS.IFramePoint;
import dataS.QueryResultDataStruc;
import dataS.SamplePoint;
import dataS.TempFramePoint;
import dataS.Trajectory;
import advantageOperators.NNSearch;
import advantageOperators.WindowQueryInterface;
import basicOperators.DeleteData;
import basicOperators.InsertData;
import basicOperators.SelectData;

/**
 * A database used to frame table with operators.
 * @author Haozhou
 *
 */

public class FrameTable implements InsertData, SelectData, DeleteData, NNSearch{

	private long standardTime;
	private int interval;
	private int maxJumpSlot;
	protected FrameList fl;
	private AuxTable at;
	
	//gird index variable
	private GridIndex gindex;
	private double xcentroid;
	private double ycentroid;
	private double xlength;
	private double ylength;
	//
	
	/**
	 * initial frame table without index
	 * @param st the standard time
	 * @param intervalTime the time interval of time slot
	 * @param max maximum time slots allow to  interpolate
	 */
	
	public FrameTable(long st, int intervalTime, int max){
		standardTime = st;
		interval = intervalTime;
		maxJumpSlot = max;
		this.fl = new FrameList(standardTime, interval);
		this.at = new AuxTable();
				
	}
	/**
	 * initial frame table with index
	 * @param st the standard time
	 * @param intervalTime the time interval of time slot
	 * @param max maximum time slots allow to  interpolate
	 * @param xcentroid the gird centre/start point location
	 * @param ycentroid the gird centre/start point location
	 * @param xlength the length of each grid cell
	 * @param ylength the length of each grid cell
	 */
	public FrameTable(long st, int intervalTime, int max, double xcentroid, double ycentroid, double xlength, double ylength){
		standardTime = st;
		interval = intervalTime;
		maxJumpSlot = max;
		this.fl = new FrameList(standardTime, interval);
		this.at = new AuxTable();
		
		this.xcentroid = xcentroid;
		this.ycentroid = ycentroid;
		this.xlength = xlength;
		this.ylength = ylength;
		this.gindex = new GridIndex(this.xcentroid, this.ycentroid, this.xlength, this.ylength);
		
		
		
	}
	
	
	@Override
	public boolean InsertSingleRecord(int tid, ArrayList<SamplePoint> data) {
		// TODO Auto-generated method stub
		
		FrameConvert ac = new FrameConvert(standardTime, interval);
		ArrayList<TempFramePoint> tfplist = ac.accConvert(tid, data, maxJumpSlot);
		for (TempFramePoint tfp : tfplist){
			fl.addNewPointToFrame(tfp.getTimeslot(), new IFramePoint(tfp.getX(), tfp.getY(), tfp.getTid()));
		}
		
		AuxTableRow atr = new AuxTableRow(tid, tfplist.get(0).getTimeslot(), tfplist.get(tfplist.size() - 1).getTimeslot(), 0, 0);
		at.addNewRecord(atr);	
		
		
		return true;
	}
	
	/**
	 * insert a single record with index
	 * @param tid id of trajectory
	 * @param data trajectory
	 */
	public void InsertSingleRecordByIndex(int tid,  ArrayList<SamplePoint> data){
		
		FrameConvert ac = new FrameConvert(standardTime, interval);
		ArrayList<TempFramePoint> tfplist = ac.accConvert(tid, data, maxJumpSlot);
		for (TempFramePoint tfp : tfplist){
			int findex = -1;
			fl.addNewPointToFrame(tfp.getTimeslot(), new IFramePoint(tfp.getX(), tfp.getY(), tfp.getTid()));
			if (findex != -1){
				gindex.indexApoint(tfp.getTimeslot(), findex, tfp.getX(), tfp.getY());
			}
		}
		
		AuxTableRow atr = new AuxTableRow(tid, tfplist.get(0).getTimeslot(), tfplist.get(tfplist.size() - 1).getTimeslot(), 0, 0);
		at.addNewRecord(atr);	
		
		
	}

	@Override
	public boolean InsertMultipleRecords() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * insert a set of trajectory 
	 * @param data the set of trajectory
	 */

	public void importBulkofDataWithIndex(ArrayList<ArrayList<SamplePoint>> data){
		
		int tid = 0;
		
		for (ArrayList<SamplePoint> splist : data){
			this.InsertSingleRecordByIndex(tid, splist);
			tid++;
		}
		
		
	}

	@Override
	public boolean importBulkofData(ArrayList<ArrayList<SamplePoint>> data) {
		// TODO Auto-generated method stub
		
		int tid = 0;
		
		for (ArrayList<SamplePoint> splist : data){
			this.InsertSingleRecord(tid, splist);
			tid++;				
		}
		
		return true;
	}




	@Override
	public Trajectory selectOneRecordByTID(int tid) {
		// TODO Auto-generated method stub
		
		int startFrame = this.getFrameIDbyTID(tid).get(0);
		int endFrame = this.getFrameIDbyTID(tid).get(1);
		
		Trajectory tr = new Trajectory(tid, endFrame - startFrame + 10);
		
		for (int i = startFrame; i <= endFrame; i++){
			
			 for (IFramePoint p :fl.getFrameByTimeSlot(i).getPoints()){
				 if (p.getTid() == tid){
					 tr.addSamplePoint(new SamplePoint(p.getX(), p.getY(), 
							 commonMethods.TimeOperation.getTimeBySlot(this.standardTime, this.interval,i)));
					 break;
				 }
			 }			
			
		}
		
		return tr;
	}
	/**
	 * Get the ids of frame the relate to the given tid
	 * @param tid id of trajectory
	 * @return the frame ids
	 */
	
	private ArrayList<Integer> getFrameIDbyTID(int tid){
		ArrayList<Integer> frameNum = new ArrayList<Integer>(2);
		
		AuxTableRow atr = this.at.getRowbyTid(tid);
		
		if (atr != null){
			frameNum.add(atr.getStartFrame());
			frameNum.add(atr.getEndFrame());
		}else{
			frameNum.add(Integer.MIN_VALUE);
			frameNum.add(Integer.MAX_VALUE);
		}
		
		return frameNum;
		
	}


	/**
	 * Remove the record from AuxTable by tid
	 * @param tid id of trajectory
	 * @return the ids of frame that relate to the given tid
	 */
	private ArrayList<Integer> removeFrameIDByTID(int tid){
		
		ArrayList<Integer> frameNum = new ArrayList<Integer>(2);
		
		AuxTableRow atr = this.at.removeRowByTid(tid);
		
		if (atr != null){
			frameNum.add(atr.getStartFrame());
			frameNum.add(atr.getEndFrame());
			
		}else{
			
			frameNum.add(Integer.MIN_VALUE);
			frameNum.add(Integer.MAX_VALUE);
		}
		
		return frameNum;
		
		
		
	}


	@Override
	public Trajectory deleteOneRecordByTID(int tid) {
		
		// TODO Auto-generated method stub
		
		ArrayList<Integer> se =  this.removeFrameIDByTID(tid);
		
		int startFrame = se.get(0);
		int endFrame = se.get(1);
		
		Trajectory tr = new Trajectory(tid);
		
		
		for (int i = startFrame; i <= endFrame; i++){
			int j =0;
			for (IFramePoint fp :fl.getFrameByTimeSlot(i).getPoints()){
				if (fp.getTid() == tid){
					IFramePoint p = fl.removeFramePointByLocation(i, j);
					tr.addSamplePoint(new SamplePoint(p.getX(), p.getY(), 
							 commonMethods.TimeOperation.getTimeBySlot(this.standardTime, this.interval,i)));
					p = null;
					break;
				}
				j++;
			}			
			
		}
		return tr;
		
	}




	@Override
	public Trajectory findNN(double x, double y, long startTime, long endTime,
			double maxSpeed) {
		// TODO Auto-generated method stub
		
		int startSlot = commonMethods.TimeOperation.getSlot(this.standardTime, this.interval, startTime);
		int endSlot = commonMethods.TimeOperation.getSlot(this.standardTime, this.interval, endTime);
		
		double distanceBestSoFar = Double.POSITIVE_INFINITY;
		int bestSoFarTid = -1;
		
		for (int i = startSlot; i <= (endSlot + 1); i++){
			if(!fl.checkFrame(i)){
				continue;
			}
			
			for (IFramePoint fp : fl.getFrameByTimeSlot(i).getPoints()){
				double distance = commonMethods.Distance.getDistance(x, y, fp.getX(), fp.getY());
				if (distance < distanceBestSoFar){
					distanceBestSoFar = distance;
					bestSoFarTid = fp.getTid();	
				}else{
					continue;
				}
			}
		}
		//System.out.println("tid is " + bestSoFarTid);
		return this.selectOneRecordByTID(bestSoFarTid);
		//return null;
	}

	/**
	 * Find a temporal NN query with index
	 * @param x the x value of query point
	 * @param y the y value of query point
	 * @param startTime the start time of search time interval
	 * @param endTime the end time of search time interval
	 * @param maxSpeed the max speed of moving object
	 * @return the nearest trajectory
	 */


	public Trajectory findNNByGridIndex(double x, double y, long startTime,
			long endTime, double maxSpeed) {
		
		int startSlot = commonMethods.TimeOperation.getSlot(this.standardTime, this.interval, startTime);
		int endSlot = commonMethods.TimeOperation.getSlot(this.standardTime, this.interval, endTime);
		
		double distanceBestSoFar = Double.POSITIVE_INFINITY;
		int bestSoFarTid = -1;
		
		HashMap<Integer, Double> seekedGrid;
		ArrayList<Integer> waitingList;
		
		int xGrid = (int)((x - this.xcentroid)/this.xlength);
		int yGrid = (int)((y - this.ycentroid)/this.ylength);
		
		for (int i = startSlot; i <= (endSlot + 1); i++){
			if(!fl.checkFrame(i)){
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
				
				if (iframePointIndex != null){
					ArrayList<IFramePoint> fpl = fl.getFrameByTimeSlot(i).getPoints();
					
					for (int findex : iframePointIndex){
								
						IFramePoint fp = fpl.get(findex);
						
						double distance = commonMethods.Distance.getDistance(x, y, fp.getX(), fp.getY());
						
						if (distance < distanceBestSoFar){
							distanceBestSoFar = distance;
							bestSoFarTid = fp.getTid();	
						}else{
							continue;
						}
						
						
					}
				}
				
				int searchx = searchGrid >> 16;
				int searchy = searchGrid & 0xFFFF;
						
				double searchGridXBottomLeft = this.xcentroid + searchx*this.xlength;
				double searchGridYBottomLeft = this.ycentroid + searchy*this.ylength;
						
				double searchGridXTopLeft = searchGridXBottomLeft;
				double searchGridYTopLeft = this.ycentroid + (searchy + 1) * this.ylength;

				double searchGridXTopRight = this.xcentroid + (searchx + 1) * this.xlength;
				double searchGridYTopRight = this.ycentroid + (searchy + 1) * this.ylength;

				double searchGridXBottomRight = this.xcentroid + (searchx + 1)* this.xlength;
				double searchGridYBottomRight = searchGridYBottomLeft;

				double d1 = commonMethods.Distance.getDistance(x, y,
						searchGridXBottomLeft, searchGridYBottomLeft);
				double d3 = commonMethods.Distance.getDistance(x, y,
						searchGridXTopLeft, searchGridYTopLeft);
				double d5 = commonMethods.Distance.getDistance(x, y,
						searchGridXTopRight, searchGridYTopRight);
				double d7 = commonMethods.Distance.getDistance(x, y,
						searchGridXBottomRight, searchGridYBottomRight);

				double d2 = commonMethods.Distance.calculateLineDistance(x, y,
						searchGridXBottomLeft, searchGridYBottomLeft,
						searchGridXTopLeft, searchGridYTopLeft);
				double d4 = commonMethods.Distance.calculateLineDistance(x, y,
						searchGridXTopLeft, searchGridYTopLeft,
						searchGridXTopRight, searchGridYTopRight);
				double d6 = commonMethods.Distance.calculateLineDistance(x, y,
						searchGridXTopRight, searchGridYTopRight,
						searchGridXBottomRight, searchGridYBottomRight);
				double d8 = commonMethods.Distance.calculateLineDistance(x, y,
						searchGridXBottomRight, searchGridYBottomRight,
						searchGridXBottomLeft, searchGridYBottomLeft);

				int indexcode;

				if (d1 - distanceBestSoFar <= 0.1) {
					indexcode = this.buildIndexCode(searchx - 1, searchy - 1);
					if ((searchx - 1) >= 0 && (searchy - 1) >= 0) {
						if (!seekedGrid.containsKey(indexcode)) {
							waitingList.add(indexcode);
						}

					}
					if (!seekedGrid.containsKey(indexcode)) {
						seekedGrid.put(indexcode, d1);
					}

				} else {
					indexcode = this.buildIndexCode(searchx - 1, searchy - 1);
					if (!seekedGrid.containsKey(indexcode)) {
						seekedGrid.put(indexcode, d1);
					}

				}

				if (d2 - distanceBestSoFar <= 0.1) {
					indexcode = this.buildIndexCode(searchx - 1, searchy);
					if ((searchx - 1) >= 0) {
						if (!seekedGrid.containsKey(indexcode)) {
							waitingList.add(indexcode);
						}
					}
					if (!seekedGrid.containsKey(indexcode)) {
						seekedGrid.put(indexcode, d2);
					}

				} else {
					indexcode = this.buildIndexCode(searchx - 1, searchy);
					if (!seekedGrid.containsKey(indexcode)) {
						seekedGrid.put(indexcode, d2);
					}

				}

				if (d3 - distanceBestSoFar <= 0.1) {
					indexcode = this.buildIndexCode(searchx - 1, searchy + 1);
					if ((searchx - 1) >= 0) {
						if (!seekedGrid.containsKey(indexcode)) {
							waitingList.add(indexcode);
						}
					}
					if (!seekedGrid.containsKey(indexcode)) {
						seekedGrid.put(indexcode, d3);
					}

				} else {
					indexcode = this.buildIndexCode(searchx - 1, searchy + 1);
					if (!seekedGrid.containsKey(indexcode)) {
						seekedGrid.put(indexcode, d3);
					}

				}

				if (d4 - distanceBestSoFar <= 0.1) {
					indexcode = this.buildIndexCode(searchx, searchy + 1);
					if (!seekedGrid.containsKey(indexcode)) {
						waitingList.add(indexcode);
					}
					if (!seekedGrid.containsKey(indexcode)) {
						seekedGrid.put(indexcode, d4);
					}

				} else {
					indexcode = this.buildIndexCode(searchx, searchy + 1);
					if (!seekedGrid.containsKey(indexcode)) {
						seekedGrid.put(indexcode, d4);
					}

				}

				if (d5 - distanceBestSoFar <= 0.1) {
					indexcode = this.buildIndexCode(searchx + 1, searchy + 1);
					if (!seekedGrid.containsKey(indexcode)) {
						waitingList.add(indexcode);
					}
					if (!seekedGrid.containsKey(indexcode)) {
						seekedGrid.put(indexcode, d5);
					}

				} else {
					indexcode = this.buildIndexCode(searchx + 1, searchy + 1);
					if (!seekedGrid.containsKey(indexcode)) {
						seekedGrid.put(indexcode, d5);
					}

				}

				if (d6 - distanceBestSoFar <= 0.1) {
					indexcode = this.buildIndexCode(searchx + 1, searchy);
					if (!seekedGrid.containsKey(indexcode)) {
						waitingList.add(indexcode);
					}
					if (!seekedGrid.containsKey(indexcode)) {
						seekedGrid.put(indexcode, d6);
					}

				} else {
					indexcode = this.buildIndexCode(searchx + 1, searchy);
					if (!seekedGrid.containsKey(indexcode)) {
						seekedGrid.put(indexcode, d6);
					}

				}

				if (d7 - distanceBestSoFar <= 0.1) {
					indexcode = this.buildIndexCode(searchx + 1, searchy - 1);
					if ((searchy - 1) >= 0) {
						if (!seekedGrid.containsKey(indexcode)) {
							waitingList.add(indexcode);
						}
					}
					if (!seekedGrid.containsKey(indexcode)) {
						seekedGrid.put(indexcode, d7);
					}

				} else {
					indexcode = this.buildIndexCode(searchx + 1, searchy - 1);
					if (!seekedGrid.containsKey(indexcode)) {
						seekedGrid.put(indexcode, d7);
					}

				}

				if (d8 - distanceBestSoFar <= 0.1) {
					indexcode = this.buildIndexCode(searchx, searchy - 1);
					if ((searchy - 1) >= 0) {
						if (!seekedGrid.containsKey(indexcode)) {
							waitingList.add(indexcode);
						}
					}
					if (!seekedGrid.containsKey(indexcode)) {
						seekedGrid.put(indexcode, d8);
					}

				} else {
					indexcode = this.buildIndexCode(searchx, searchy - 1);
					if (!seekedGrid.containsKey(indexcode)) {
						seekedGrid.put(indexcode, d8);
					}

				}

			}
			
			
		}
		
		
		//System.out.println("tid is " + bestSoFarTid);
		//return this.selectOneRecordByTID(bestSoFarTid);
		return null;
		
		
		
		
	}
	
	/**
	 * encode the grid cell id
	 * @param x
	 * @param y
	 * @return the code
	 */
	
	private int buildIndexCode(int x, int y){
		return (x << 16) | (y & 0xFFFF);
	}

	@Override
	public Trajectory findNNByLine(double x, double y, long startTime,
			long endTime, double maxSpeed) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public long getStandardTime() {
		return standardTime;
	}
	
	public int getInterval() {
		return interval;
	}
	
	
	
	public HashMap<Integer, ArrayList<SamplePoint>> STWindowQuery(double x, double y, long startTime,
			long endTime, double radius) {
		
		HashMap<Integer, ArrayList<SamplePoint>> result = new HashMap<>();
		int startSlot = commonMethods.TimeOperation.getSlot(this.standardTime, this.interval, startTime);
		int endSlot = commonMethods.TimeOperation.getSlot(this.standardTime, this.interval, endTime);
                
                
		for (int i = startSlot; i <= (endSlot + 1); i++){
			if(!fl.checkFrame(i)){
				continue;
			}
			
			for (IFramePoint fp : fl.getFrameByTimeSlot(i).getPoints()){
                             
				if(IsWithinBoundingBox.isWithinBoundingBox(fp.getX(), fp.getY(), x, y, radius)){
					if(commonMethods.Distance.getDistance(x, y, fp.getX(), fp.getY()) < radius){
						if (result.containsKey(fp.getTid())){
							result.get(fp.getTid()).add(new SamplePoint(fp.getX(), fp.getY(), TimeOperation.getTimeBySlot(this.getStandardTime(),this.getInterval(),i)));
						}else{
							ArrayList<SamplePoint> temp = new ArrayList<SamplePoint>();
							temp.add(new SamplePoint(fp.getX(), fp.getY(), TimeOperation.getTimeBySlot(this.standardTime,this.interval,i)));
                                                                                                                              //System.out.println("find results");
							result.put(fp.getTid(), temp);
						}
					}
				}
				//double distance = commonMethods.Distance.getDistance(x, y, fp.getX(), fp.getY());
				
			}
		}
			
		return result;
	}
	
	public HashMap<Integer, ArrayList<SamplePoint>> SOnlyWindowQuery(double x, double y, double radius) {
		
		HashMap<Integer, ArrayList<SamplePoint>> result = new HashMap<Integer, ArrayList<SamplePoint>>();
		
		
		for (int i : this.fl.frames.keySet()){
			if(!fl.checkFrame(i)){
				continue;
			}
			
			for (IFramePoint fp : fl.getFrameByTimeSlot(i).getPoints()){
				if(IsWithinBoundingBox.isWithinBoundingBox(fp.getX(), fp.getY(), x, y, radius)){
					if(commonMethods.Distance.getDistance(x, y, fp.getX(), fp.getY()) < radius){
						if (result.containsKey(fp.getTid())){
							result.get(fp.getTid()).add(new SamplePoint(fp.getX(), fp.getY(), TimeOperation.getTimeBySlot(this.getStandardTime(),this.getInterval(),i)));
						}else{
							ArrayList<SamplePoint> temp = new ArrayList<SamplePoint>();
							temp.add(new SamplePoint(fp.getX(), fp.getY(), TimeOperation.getTimeBySlot(this.standardTime,this.interval,i)));
							result.put(fp.getTid(), temp);
						}
					}
				}
				//double distance = commonMethods.Distance.getDistance(x, y, fp.getX(), fp.getY());
				
			}
		}
			
		return result;
	}
	
	
	
}
