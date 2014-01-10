package frameTable;

import java.util.ArrayList;
import java.util.HashMap;

import commonMethods.IsWithinBoundingBox;
import commonMethods.TimeOperation;

import dataS.IFramePoint;
import dataS.IPGroup;
import dataS.PFramePoint;
import dataS.SamplePoint;
import dataS.TempFramePoint;
import dataS.Trajectory;
import advantageOperators.NNSearch;
import basicOperators.DeleteData;
import basicOperators.InsertData;
import basicOperators.SelectData;

public class IPFrameTable implements InsertData, SelectData, DeleteData, NNSearch{

	
	public long standardTime;
	public int interval;
	public int maxJumpSlot;
	public DualFrameList dfl;
	public AuxTable at;
	public int n;

	
	//gird index variable
	private GridIndex gindex;
	private double xcentroid;
	private double ycentroid;
	private double xlength;
	private double ylength;
	//
	

	/**
	 * 
	 * @param st the standard time
	 * @param intervalTime the time interval of time slot
	 * @param max maximum time slots allow to  interpolate
	 * @param n the number of frames for each I/P frame group
	 */
	
	public IPFrameTable(long st, int intervalTime, int max, int n){
		this.standardTime = st;
		this.interval = intervalTime;
		this.maxJumpSlot = max;
		this.n = n;
		this.dfl = new DualFrameList();
		this.at = new AuxTable();
		
	}
	
	/**
	 * 
	 * @param st the standard time
	 * @param intervalTime the time interval of time slot
	 * @param max maximum time slots allow to  interpolate
	 * @param n the number of frames for each I/P frame group
	 * @param xcentroid the gird center/start point location
	 * @param ycentroid the gird center/start point location
	 * @param xlength the length of each grid cell
	 * @param ylength the length of each grid cell
	 */
	public IPFrameTable(long st, int intervalTime, int max, int n, double xcentroid, double ycentroid, double xlength, double ylength){
		this.standardTime = st;
		this.interval = intervalTime;
		this.maxJumpSlot = max;
		this.n = n;
		this.dfl = new DualFrameList();
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
		ArrayList<IPGroup> groups = this.getGroup(tfplist);
		for (IPGroup ipg : groups){
			dfl.addIFramePoint(new IFramePoint(ipg.getF().getX(), ipg.getF().getY(), ipg.getF().getTid()), ipg.getF().getTimeslot());
			int startTimeSlot = (ipg.getF().getTimeslot() - 1)*(this.n - 1);
			for (PFramePoint p : ipg.getP()){
				dfl.addPFramePoint(p, startTimeSlot + 1);
				startTimeSlot++;
			}
			
		}
				
		AuxTableRow atr = new AuxTableRow(tid, groups.get(0).getF().getTimeslot(), groups.get(groups.size() - 1).getF().getTimeslot(), 0, 0);
		at.addNewRecord(atr);
		
		return true;
	}

	@Override
	public boolean InsertMultipleRecords() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Insert a trajectory into database with indexing
	 * @param tid the id of trajectory
	 * @param data the sample point of inserting trajectory
	 */
	public void InsertSingleRecordByIndex(int tid,  ArrayList<SamplePoint> data){
		
		FrameConvert ac = new FrameConvert(standardTime, interval);
		ArrayList<TempFramePoint> tfplist = ac.accConvert(tid, data, maxJumpSlot);
		ArrayList<IPGroup> groups = this.getGroup(tfplist);
		for (IPGroup ipg : groups){
			int findex = dfl.addIFramePoint(new IFramePoint(ipg.getF().getX(), ipg.getF().getY(), ipg.getF().getTid()), ipg.getF().getTimeslot());
			this.gindex.indexApoint(ipg.getF().getTimeslot(), findex, ipg.getF().getX(), ipg.getF().getY());
			int startTimeSlot = (ipg.getF().getTimeslot() - 1)*(this.n - 1);
			for (PFramePoint p : ipg.getP()){
				dfl.addPFramePoint(p, startTimeSlot + 1);
				startTimeSlot++;
			}
			
		}
				
		AuxTableRow atr = new AuxTableRow(tid, groups.get(0).getF().getTimeslot(), groups.get(groups.size() - 1).getF().getTimeslot(), 0, 0);
		at.addNewRecord(atr);
		
		
	}
	
	/**
	 * used for import a lot of data once with indexing
	 * @param data a bulk of data read from other source
	 * 
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
	
	/**
	 * Encode the frame points into I/P frame groups, both Iframe and Pframe id are start at 1
	 * @param tfplist a array of frame points
	 * @return the array of I/P frame groups
	 */
	private ArrayList<IPGroup> getGroup(ArrayList<TempFramePoint> tfplist){
		
		ArrayList<IPGroup> ipglist = new ArrayList<IPGroup>();
		int i = 0;
		int count = this.n;
		IPGroup ipg = new IPGroup(); 
		if (tfplist.get(i).getTimeslot() % this.n != 0){
			TempFramePoint newiframe = new TempFramePoint(tfplist.get(i).getTid(), 
					tfplist.get(i).getX(), tfplist.get(i).getY(), (tfplist.get(i).getTimeslot()/this.n + 1), tfplist.get(i).getT());
			count--;
			ipg.setF(newiframe);
			int addNumOfFrame = tfplist.get(i).getTimeslot() - (tfplist.get(i).getTimeslot()/this.n * this.n);
			for (int k = 0; k < addNumOfFrame; k++){
				
				PFramePoint p = new PFramePoint(tfplist.get(i).getTid(), (short)0, (short)0);
				
				ipg.addNewPframePoint(p);
				count--;
			}
			i++;
			for (int k = 0; k < count - 1; k++){
				if (i >=  tfplist.size()){
					break;
				}
				
				
				
				double x = (tfplist.get(i).getX() - newiframe.getX())*1000000;
				double y = (tfplist.get(i).getY() - newiframe.getY())*1000000;
				
				short dx = (short)x;
				short dy = (short)y;
				
				
				
				PFramePoint p = new PFramePoint(newiframe.getTid(), dx, dy);
				ipg.addNewPframePoint(p);
				i++;
				
			}
			
			ipglist.add(ipg);
		}
		
		for (; i < tfplist.size();){
			
			ipg = new IPGroup(); 
			
			TempFramePoint newiframe = new TempFramePoint(tfplist.get(i).getTid(), 
					tfplist.get(i).getX(), tfplist.get(i).getY(), (tfplist.get(i).getTimeslot()/this.n + 1), tfplist.get(i).getT());
			
			i++;
			ipg.setF(newiframe);
			for (int k = 0; k < (this.n - 1); k++){
				if (i >=  tfplist.size()){
					
					short idx = Short.MAX_VALUE;
					short idy = Short.MIN_VALUE;
					PFramePoint p = new PFramePoint(newiframe.getTid(), idx, idy);
					ipg.addNewPframePoint(p);
					i++;
					
				}else{
					
					double x = (tfplist.get(i).getX() - newiframe.getX())*1000000;
					double y = (tfplist.get(i).getY() - newiframe.getY())*1000000;
					
					short dx = (short)x;
					short dy = (short)y;
					
					
					PFramePoint p = new PFramePoint(newiframe.getTid(), dx, dy);
					ipg.addNewPframePoint(p);
					i++;
					
				}
				
			}
			ipglist.add(ipg);
		}
		
		return ipglist;
		
		
	}

	public long getStandardTime() {
		return standardTime;
	}
	
	

	public int getInterval() {
		return interval;
	}

	@Override
	public Trajectory selectOneRecordByTID(int tid) {
		
		
		ArrayList<Integer> sd = this.getFrameIDbyTID(tid);
		
		int startFrame = sd.get(0);
		int endFrame = sd.get(1);
	
		Trajectory tr = new Trajectory(tid);
	
		for (int i = startFrame; i <= endFrame; i++){
			int c = 0;
			
			for (IFramePoint fp : dfl.getIFrameByFID(i).getPoints()){
				
				if(fp.getTid() == tid){
					
					int currentTimeSlot = (i - 1)*this.n;
					int pSlot = (i - 1)*(this.n - 1);
					
					tr.addSamplePoint(new SamplePoint(fp.getX(), fp.getY(), 
							commonMethods.TimeOperation.getTimeBySlot(this.standardTime, this.interval ,currentTimeSlot)));
					
					//decode
					for (int j = 1; j < this.n; j++){
						
						int shiftingCode = dfl.getPFrameByPID(pSlot + j).getPointByPID(c);
						short dx = (short) (shiftingCode >> 16);
						short dy = (short) (shiftingCode & 0xFFFF);
						
						
						
						if (dx == Short.MAX_VALUE && dy == Short.MIN_VALUE){
							continue;
						}
						
						
						double x = fp.getX() + ((double)(dx)/1000000);
						double y = fp.getY() + ((double)(dy)/1000000);
						
						tr.addSamplePoint(new SamplePoint(x, y, 
								commonMethods.TimeOperation.getTimeBySlot(this.standardTime, this.interval ,currentTimeSlot + j)));
						
						
					}
					
					break;
					
				}
				c++;
			
			}
			
		
		}
		
		return tr;
		
		
		
		
	}
	/**
	 * Get the ids of Iframe the relate to the given tid
	 * @param tid id of trajectory
	 * @return the Iframe ids
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
		ArrayList<Integer> sd = this.removeFrameIDByTID(tid);
		
		int startFrame = sd.get(0);
		int endFrame = sd.get(1);
		
	
		
		Trajectory tr = new Trajectory(tid);
		
	
		
		for (int i = startFrame; i <= endFrame; i++){
			int c = 0;
			
			for (IFramePoint fp : dfl.getIFrameByFID(i).getPoints()){
				
				if(fp.getTid() == tid){
					
					int currentTimeSlot = (i - 1)*this.n;
					int pSlot = (i - 1)*(this.n - 1);
					
					IFramePoint p = dfl.removeIFrameByLocation(i, c);
					tr.addSamplePoint(new SamplePoint(p.getX(), p.getY(), 
							 commonMethods.TimeOperation.getTimeBySlot(this.standardTime, this.interval, currentTimeSlot)));
					for (int j = 1; j < this.n; j++){
						
						int shiftingCode = dfl.removePframeByLocation(pSlot + j, c);
						
						short dx = (short) (shiftingCode >> 16);
						short dy = (short) (shiftingCode & 0xFFFF);
						
						if (dx == Short.MAX_VALUE && dy == Short.MIN_VALUE){
							continue;
						}
											
						double x = p.getX() + ((double)(dx)/1000000);
						double y = p.getY() + ((double)(dy)/1000000);
						
						tr.addSamplePoint(new SamplePoint(x, y, 
								commonMethods.TimeOperation.getTimeBySlot(this.standardTime, this.interval ,currentTimeSlot + j)));
												
					}
					
					p = null;
					
					break;
					
				}
				c++;
				
				
			}
			
			
			
		}
		
		return tr;
	}

	@Override
	public Trajectory findNN(double x, double y, long startTime, long endTime, double maxSpeed) {
		// TODO Auto-generated method stub
		int startSlot = commonMethods.TimeOperation.getSlot(this.standardTime, this.interval, startTime);
		int endSlot = commonMethods.TimeOperation.getSlot(this.standardTime, this.interval, endTime);
		
		double distanceBestSoFar = Double.POSITIVE_INFINITY;
		int bestSoFarTid = -1;
		
		
		
		double maxMovedistance = (this.interval/1000) * (this.n - 1) * maxSpeed;
		
		int startIframeSlot = startSlot/this.n + 1;
		int endIframeSlot = endSlot/this.n + 2;
		
	
		
		for (int i = startIframeSlot; i <= endIframeSlot; i++){
				
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
		
		//System.out.println("tid is " + bestSoFarTid);
		//System.out.println("distance is " + distanceBestSoFar);
		return this.selectOneRecordByTID(bestSoFarTid);
		//return null;
	}

	@Override
	public Trajectory findNNByLine(double x, double y, long startTime,
			long endTime, double maxSpeed) {
		// TODO Auto-generated method stub
		return null;
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
		
		
		double maxMovedistance = (this.interval/1000) * (this.n - 1) * maxSpeed;
		
		int startIframeSlot = startSlot/this.n + 1;
		int endIframeSlot = endSlot/this.n + 2;
		
		HashMap<Integer, Double> seekedGrid;
		ArrayList<Integer> waitingList;
		
		
		int xGrid = (int)((x - this.xcentroid)/this.xlength);
		int yGrid = (int)((y - this.ycentroid)/this.ylength);
		
		
		
		for (int i = startIframeSlot; i <= endIframeSlot; i++){
			
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
								//long code = dfl.getPFrameByPID(pSlot + j).getPointByPID(c);
								int shiftingCode = dfl.getPFrameByPID(pSlot + j).getPointByPID(findex);
								
								short dx = (short) (shiftingCode >> 16);
								short dy = (short) (shiftingCode & 0xFFFF);					
								
								if (dx == Short.MAX_VALUE && dy == Short.MIN_VALUE){
									continue;
								}
								
								//System.out.println("dx is " + dx + " dy is " + dy);
								
								double px = fp.getX() + ((double)(dx)/1000000);
								double py = fp.getY() + ((double)(dy)/1000000);
								
								double pDistance = commonMethods.Distance.getDistance(x, y, px, py);
								
								if (pDistance < distanceBestSoFar){
									distanceBestSoFar = pDistance;
									bestSoFarTid = fp.getTid();
								}
							}
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
		
		//System.out.println("tid is " + bestSoFarTid);
		//System.out.println("distance is " + distanceBestSoFar);
		//return this.selectOneRecordByTID(0);
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
	
	public HashMap<Integer, ArrayList<SamplePoint>> STWindowQuery(double x, double y, long startTime,
			long endTime, double radius, double maxSpeed) {
		
		HashMap<Integer, ArrayList<SamplePoint>> result = new HashMap<Integer, ArrayList<SamplePoint>>();
		int startSlot = commonMethods.TimeOperation.getSlot(this.standardTime, this.interval, startTime);
		int endSlot = commonMethods.TimeOperation.getSlot(this.standardTime, this.interval, endTime);	
		
		double maxMovedistance = (this.interval/1000) * (this.n - 1) * maxSpeed;
		
		for (int i = startSlot; i <= (endSlot + 1); i++){
			int c = 0;
			
			
			if(!dfl.containIframeByFID(i)){
				continue;
			}
			
			for (IFramePoint fp : dfl.getIFrameByFID(i).getPoints()){
				int currentTimeSlot = (i - 1)*this.n;
				
				if(IsWithinBoundingBox.isWithinBoundingBox(fp.getX(), fp.getY(), x, y, radius)){
					if(commonMethods.Distance.getDistance(x, y, fp.getX(), fp.getY()) < radius){
						if (result.containsKey(fp.getTid())){
							result.get(fp.getTid()).add(new SamplePoint(fp.getX(), fp.getY(), TimeOperation.getTimeBySlot(this.getStandardTime(),this.getInterval(),currentTimeSlot)));
						}else{
							ArrayList<SamplePoint> temp = new ArrayList<SamplePoint>();
							temp.add(new SamplePoint(fp.getX(), fp.getY(), TimeOperation.getTimeBySlot(this.standardTime,this.interval,currentTimeSlot)));
							result.put(fp.getTid(), temp);
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
								result.get(fp.getTid()).add(new SamplePoint(x, y, TimeOperation.getTimeBySlot(this.getStandardTime(),this.getInterval(),currentTimeSlot + j)));
							}else{
								ArrayList<SamplePoint> temp = new ArrayList<SamplePoint>();
								temp.add(new SamplePoint(x, y, TimeOperation.getTimeBySlot(this.standardTime,this.interval,currentTimeSlot + j)));
								result.put(fp.getTid(), temp);
							}
						}
						
						
					}
					
				}
				
				c++;
				
			}
		}
			
		return result;
	}
	
	
	public HashMap<Integer, ArrayList<SamplePoint>> SOnlyWindowQuery(double x, double y, double radius, double maxSpeed) {
		
		HashMap<Integer, ArrayList<SamplePoint>> result = new HashMap<Integer, ArrayList<SamplePoint>>();
		
		double maxMovedistance = (this.interval/1000) * (this.n - 1) * maxSpeed;
		
		
		for (int i : this.dfl.getIframeTimeSlotSet()){
			int c = 0;
				
			for (IFramePoint fp : dfl.getIFrameByFID(i).getPoints()){
				int currentTimeSlot = (i - 1)*this.n;
				
				if(IsWithinBoundingBox.isWithinBoundingBox(fp.getX(), fp.getY(), x, y, radius)){
					if(commonMethods.Distance.getDistance(x, y, fp.getX(), fp.getY()) < radius){
						if (result.containsKey(fp.getTid())){
							result.get(fp.getTid()).add(new SamplePoint(fp.getX(), fp.getY(), TimeOperation.getTimeBySlot(this.getStandardTime(),this.getInterval(),currentTimeSlot)));
						}else{
							ArrayList<SamplePoint> temp = new ArrayList<SamplePoint>();
							temp.add(new SamplePoint(fp.getX(), fp.getY(), TimeOperation.getTimeBySlot(this.standardTime,this.interval,currentTimeSlot)));
							result.put(fp.getTid(), temp);
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
								result.get(fp.getTid()).add(new SamplePoint(x, y, TimeOperation.getTimeBySlot(this.getStandardTime(),this.getInterval(),currentTimeSlot + j)));
							}else{
								ArrayList<SamplePoint> temp = new ArrayList<SamplePoint>();
								temp.add(new SamplePoint(x, y, TimeOperation.getTimeBySlot(this.standardTime,this.interval,currentTimeSlot + j)));
								result.put(fp.getTid(), temp);
							}
						}
						
						
					}
					
				}else{
					continue;
				}
				
				c++;
				
			}
		}
			
		return result;
	}
	

}
