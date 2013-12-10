package parallelFrameTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import dataS.IFramePoint;
import dataS.IPGroup;
import dataS.PFramePoint;
import dataS.SamplePoint;
import dataS.TempFramePoint;
import basicOperators.InsertData;

public class IPFrameTable implements InsertData{

	
	private long standardTime;
	private int interval;
	private int maxJumpSlot;
	private DualFrameList dfl;
	private AuxTable at;
	private int n;
	private final ForkJoinPool forkJoinPool = new ForkJoinPool();
	private final ForkJoinPool NNforkJoinPool = new ForkJoinPool(18);
	//private final ForkJoinPool NNGIndexJoinPool = new ForkJoinPool();
	
	
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
	 * @param xcentroid the gird centre/start point location
	 * @param ycentroid the gird centre/start point location
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
	
	@Override
	public boolean InsertSingleRecord(int tid, ArrayList<SamplePoint> data) {
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
				//System.out.println("before: dx is " + 0+ " dy is " + 0);
				ipg.addNewPframePoint(p);
				count--;
			}
			i++;
			for (int k = 0; k < count; k++){
				if (i >=  tfplist.size()){
					break;
				}
				
				
				
				double x = (tfplist.get(i).getX() - newiframe.getX())*1000000;
				double y = (tfplist.get(i).getY() - newiframe.getY())*1000000;
				
				short dx = (short)x;
				short dy = (short)y;
				
				//System.out.println("before: dx is " + dx + " dy is " + dy);
				
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
			//count--;
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
					
					//System.out.println("before: dx is " + dx + " dy is " + dy);
					
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

	/**
	 * Select a trajectory by its id, not implement from interface since different return structure
	 * @param tid the id of trajectory
	 * @return trajectory
	 */
	
	public Trajectory selectOneRecordByTID(int tid) {
		// TODO Auto-generated method stub
		
		ArrayList<Integer> sd = this.getFrameIDbyTID(tid);
		
		int startFrame = sd.get(0);
		int endFrame = sd.get(1);
		
		//ArrayList<Frame> candidateSet = this.getCandidateFrameList(startFrame, endFrame);
		
		//System.out.println("startframe is " + startFrame + " endFrame is " + endFrame);
		
		return forkJoinPool.invoke(new startWorkTask(this.dfl, this.interval, this.standardTime, this.n, startFrame, endFrame, tid));
		
		
		
		
	}
	

	/**
	 * Processing temporal NN search by detecting the nearest sampling point, using parallel computing and grid indexing
	 * @param x the x value of query point
	 * @param y the y value of query point
	 * @param startTime the start time of search time interval
	 * @param endTime the end time of search time interval
	 * @param maxSpeed the max speed of moving object
	 * @return the nearest trajectory
	 */
	public Trajectory findNNByGridIndex(double x, double y, long startTime, long endTime, double maxSpeed){
		
		int startSlot = commonMethods.TimeOperation.getSlot(this.standardTime, this.interval, startTime);
		int endSlot = commonMethods.TimeOperation.getSlot(this.standardTime, this.interval, endTime);
		
		//double distanceBestSoFar = Double.POSITIVE_INFINITY;
		int bestSoFarTid = -1;
		
		//double maxMovedistance = this.interval * this.n * maxSpeed;
		
		int startIframeSlot = startSlot/this.n + 1;
		int endIframeSlot = endSlot/this.n + 2;
		
		
		bestSoFarTid = this.NNforkJoinPool.invoke(new startNNSearchWithGridIndex(this.dfl, this.interval, this.standardTime, this.n, startIframeSlot, 
				endIframeSlot, maxSpeed, x, y, this.gindex, this.xcentroid, this.ycentroid, this.xlength, this.ylength));
		
		//System.out.println("tid is " + bestSoFarTid);
		
		return this.selectOneRecordByTID(bestSoFarTid);
		//return null;
		
	}
	
	/**
	 * Processing temporal NN search by detecting the nearest sampling point, using parallel computing
	 * @param x the x value of query point
	 * @param y the y value of query point
	 * @param startTime the start time of search time interval
	 * @param endTime the end time of search time interval
	 * @param maxSpeed the max speed of moving object
	 * @return the nearest trajectory
	 */
	public Trajectory findNN(double x, double y, long startTime,
			long endTime, double maxSpeed) {
		
		
		int startSlot = commonMethods.TimeOperation.getSlot(this.standardTime, this.interval, startTime);
		int endSlot = commonMethods.TimeOperation.getSlot(this.standardTime, this.interval, endTime);
		
		//double distanceBestSoFar = Double.POSITIVE_INFINITY;
		int bestSoFarTid = -1;
		
		//double maxMovedistance = this.interval * this.n * maxSpeed;
		
		int startIframeSlot = startSlot/this.n + 1;
		int endIframeSlot = endSlot/this.n + 2;
		
		
		bestSoFarTid = this.NNforkJoinPool.invoke(new startNNSearch(this.dfl, this.interval, this.standardTime, this.n, startIframeSlot, 
				endIframeSlot, x, y, maxSpeed));
		
		//System.out.println("tid is " + bestSoFarTid);
		
		return this.selectOneRecordByTID(bestSoFarTid);
		//return null;
		
		
	}
	
	public ConcurrentHashMap<Integer, Trajectory> STWindowQuery(double x, double y, long startTime,
			long endTime, double radius, double maxSpeed) {
		
		int startSlot = commonMethods.TimeOperation.getSlot(this.standardTime, this.interval, startTime);
		int endSlot = commonMethods.TimeOperation.getSlot(this.standardTime, this.interval, endTime);
		
		int startIframeSlot = startSlot/this.n + 1;
		int endIframeSlot = endSlot/this.n + 2;
		
		return this.NNforkJoinPool.invoke(new startSTWindowQueryTask(this.dfl, this.interval, this.standardTime, this.n, startIframeSlot, endIframeSlot
				, maxSpeed, x, y, radius));
		
		//return null;
		
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
	 * The core parallel computing class for processing NN search with index 
	 * @author Haozhou
	 *
	 */
	class startNNSearchWithGridIndex extends RecursiveTask<Integer>{

	
		/**
		 * 
		 */
		private static final long serialVersionUID = -5675733522206948449L;
		private long standardTime;
		private int interval;
		private DualFrameList dfl;
		private int n;
		private int sf;
		private int ef;
		private double x;
		private double y;
		private double maxSpeed;
		
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
		public startNNSearchWithGridIndex(DualFrameList dfl, int interval, long standardTime, int n, int startFrame, int endFrame, 
				double maxSpeed, double x, double y, 
				GridIndex gindex, double xcentroid, double ycentroid, double xlength, double ylength){
			
			
			super();
			this.dfl = dfl;
			this.interval = interval;
			this.n = n;
			this.standardTime = standardTime;
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
		public Integer compute() {
			// TODO Auto-generated method stub
			LinkedList<RecursiveTask<NNResult>> forks = new LinkedList<RecursiveTask<NNResult>>();
			
			int numofPoint = (this.ef - this.sf) + 1;
			numofPoint = numofPoint*this.n;
			
			
			double distanceBestSoFar = Double.POSITIVE_INFINITY;
			int bestSoFarTid = -1;
			
			
			
			for (int i = this.sf; i <= this.ef; i = i + 10){
				
				int endf = 0;
				if (i + 10 <= ef){
					endf = i + 10;
				}else{
					endf = ef + 1;
				}
				
				NNSearchTaskWithGridIndex task = new NNSearchTaskWithGridIndex(this.dfl, this.interval, this.standardTime, 
						this.n, i, endf, this.maxSpeed, this.x, this.y, 
						this.gindex, this.xcentroid, this.ycentroid, this.xlength, this.ylength);
				
				forks.add(task);
				task.fork();
				
			}
			
			
			for (RecursiveTask<NNResult> task : forks){
				NNResult r = task.join();
				if (r.getBestDistanceSoFar() < distanceBestSoFar){
					bestSoFarTid = r.getBestSoFarTid();
				}
				
			}
			
			
			
			return bestSoFarTid;
		}
		
	}
	
	
	
	
	class startNNSearch extends RecursiveTask<Integer>{

		
		
		
		
		
		private static final long serialVersionUID = 197073400519892591L;
		private long standardTime;
		private int interval;
		private DualFrameList dfl;
		private int n;
		private int sf;
		private int ef;
		private double x;
		private double y;
		private double maxSpeed;
		
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
		public startNNSearch(DualFrameList dfl, int interval, long standardTime, int n, int startFrame, 
				int endFrame, double x, double y, double maxSpeed){
			super();
			this.dfl = dfl;
			this.interval = interval;
			this.n = n;
			this.standardTime = standardTime;
			this.sf = startFrame;
			this.ef = endFrame;
			this.x = x;
			this.y = y;
			this.maxSpeed = maxSpeed;
			
			
		}
		
		
		
		

		@Override
		public Integer compute() {
			// TODO Auto-generated method stub
			
			LinkedList<RecursiveTask<NNResult>> forks = new LinkedList<RecursiveTask<NNResult>>();
			
			int numofPoint = (this.ef - this.sf) + 1;
			numofPoint = numofPoint*this.n;
			
			
			double distanceBestSoFar = Double.POSITIVE_INFINITY;
			int bestSoFarTid = -1;
			
			
			
			for (int i = this.sf; i <= this.ef; i = i + 10){
				
				int endf = 0;
				if (i + 10 <= ef){
					endf = i + 10;
				}else{
					endf = ef + 1;
				}
				
				
				
				NNSearchTask task = new NNSearchTask(this.dfl, this.interval, this.standardTime, 
						this.n, i, endf, this.maxSpeed, this.x, this.y);
				
				forks.add(task);
				task.fork();
				
			}
			
			
			for (RecursiveTask<NNResult> task : forks){
				NNResult r = task.join();
				if (r.getBestDistanceSoFar() < distanceBestSoFar){
					bestSoFarTid = r.getBestSoFarTid();
				}
				
			}
			
			
			//tr = null;
			//return tr;
			
			
			return bestSoFarTid;
		}
		
	}
	
	
	class startWorkTask extends RecursiveTask<Trajectory>{

		
		/**
		 * 
		 */
		private static final long serialVersionUID = 557799822876606275L;
		private long standardTime;
		private int interval;
		private DualFrameList dfl;
		private int n;
		private int sf;
		private int ef;
		private Trajectory tr;
		private int tid;
		
		/**
		 * initial the parallel compute for selection
		 * @param dfl the I/P frame table reference
		 * @param interval the time interval of time slot
		 * @param standardTime the time interval of time slot
		 * @param n the number of frames for each I/P frame group
		 * @param startFrame the id of Iframe for start search
		 * @param endFrame the id of Iframe for end search
		 * @param tid the id of trajectory
		 */
		public startWorkTask(DualFrameList dfl, int interval, long standardTime, int n, int startFrame, int endFrame,
				 int tid){
			super();
			this.dfl = dfl;
			this.interval = interval;
			this.n = n;
			this.standardTime = standardTime;
			this.sf = startFrame;
			this.ef = endFrame;
			this.tid = tid;
		
			
		}
		@Override
		protected Trajectory compute() {
			
			LinkedList<RecursiveTask<Integer>> forks = new LinkedList<RecursiveTask<Integer>>();
			
			int numofPoint = (this.ef - this.sf) + 1;
			numofPoint = numofPoint*this.n;
			
			this.tr = new Trajectory(tid, numofPoint);
			
			for (int i = this.sf; i <= this.ef; i = i+10){
				int endf = 0;
				if (i + 10 <= ef){
					endf = i + 10;
				}else{
					endf = ef + 1;
				}
				
				int index = i - sf;
				
				FrameSearchTask task = new FrameSearchTask(this.dfl, this.interval, this.standardTime, 
						this.n, i, endf, tr, tid, index*this.n);
				
				forks.add(task);
				task.fork();
				
			}
			
			
			for (RecursiveTask<Integer> task : forks){
				task.join();
			}
			
			return tr;
		}
		
	}
	
	
	class startSTWindowQueryTask extends RecursiveTask<ConcurrentHashMap<Integer, Trajectory>>{

		
		/**
		 * 
		 */
		private static final long serialVersionUID = -9066091047540668156L;
		private int interval;
		private DualFrameList dfl;
		private int n;
		private int sf;
		private int ef;
		private double maxSpeed;
		
		
		private double x;
		private double y;
		
		
		private ConcurrentHashMap<Integer, Trajectory> result;
		//private int numberOfPoint;
		private double radius;
		//private int index;
		private long standardTime;

		public startSTWindowQueryTask(DualFrameList dfl, int interval, long standardTime, int n, int startFrame, int endFrame, 
				double maxSpeed, double x, double y,  double radius){
			
			super();
			this.dfl = dfl;
			this.interval = interval;
			this.n = n;
			this.sf = startFrame;
			this.ef = endFrame;
			this.maxSpeed = maxSpeed;
			this.x = x;
			this.y = y;
			this.radius = radius;
			this.standardTime = standardTime;
			
			
			
		}
		
		@Override
		public ConcurrentHashMap<Integer, Trajectory> compute() {
			// TODO Auto-generated method stub
			LinkedList<RecursiveTask<Integer>> forks = new LinkedList<RecursiveTask<Integer>>();
			
			int numofPoint = (this.ef - this.sf) + 1;
			numofPoint = numofPoint*this.n;
			
			result = new ConcurrentHashMap<Integer, Trajectory>();
			
			for (int i = this.sf; i <= this.ef; i = i+10){
				int endf = 0;
				if (i + 10 <= ef){
					endf = i + 10;
				}else{
					endf = ef + 1;
				}
				
				int index = i - sf;
				
				STWindowQuerySearchTask task = new STWindowQuerySearchTask(this.dfl, this.interval, this.standardTime, 
						this.n, i, endf, this.maxSpeed, this.x, this.y, this.result, numofPoint, this.radius, index*this.n);
				
				forks.add(task);
				task.fork();
				
			}
			
			
			for (RecursiveTask<Integer> task : forks){
				task.join();
			}
			
			return result;
		}
		
	}

}



