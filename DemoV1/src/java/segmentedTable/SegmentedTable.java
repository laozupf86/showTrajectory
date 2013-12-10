package segmentedTable;

import java.util.ArrayList;

import dataS.MBR;
import dataS.SamplePoint;
import dataS.Trajectory;

import advantageOperators.NNSearch;
import basicOperators.DeleteData;
import basicOperators.InsertData;
import basicOperators.SelectData;
import commonMethods.IsColliding;
import java.util.HashMap;


/**
 * The segmented database with operators.
 * There are two tables, segment table and trajectory table
 * 
 * @author Haozhou
 *
 */


public class SegmentedTable implements InsertData, SelectData, DeleteData, NNSearch{

	
	protected SegmentTable st;
	protected TrajectoryTable tt;
	//the points limitation for a trajectory segment
	protected int segmentCount = 200;
	
	/**
	 * initial with default number of points for each segment
	 */
	
	public SegmentedTable(){
		this.st = new SegmentTable();
		this.tt = new TrajectoryTable();
	}
	
	
	/**
	 * initial with user defined number of points for each segment
	 * @param segmentCount
	 */
	public SegmentedTable(int segmentCount){
		this.segmentCount = segmentCount;
		this.st = new SegmentTable();
		this.tt = new TrajectoryTable();
	}
	
	@Override
	public boolean InsertSingleRecord(int tid, ArrayList<SamplePoint> sp) {
		
		int sid = this.st.getLastSID() + 1;
		
		double lowx = Double.POSITIVE_INFINITY, lowy = Double.POSITIVE_INFINITY;
		double highx = Double.NEGATIVE_INFINITY, highy = Double.NEGATIVE_INFINITY;
		
		MBR mbr;
	
		int count = 0;		
		int sindex = 0;
		
		ArrayList<SamplePoint> newSp = new ArrayList<SamplePoint>(this.segmentCount);
		
		/*
		 * split a input trajectory into segments
		 */
		for (int i = 0; i < sp.size(); i++){
			if (count < this.segmentCount){
				newSp.add(sp.get(i));
				if (sp.get(i).getX() < lowx){
					lowx = sp.get(i).getX();
				}else if (sp.get(i).getX() > highx){
					highx = sp.get(i).getX();
				}
				
				if (sp.get(i).getY() < lowy){
					lowy = sp.get(i).getY();
				}else if (sp.get(i).getY() > highy){
					highy = sp.get(i).getY();
				}
				
				count++;
				
			}else{
				/*
				 * Reach the number limitation, create a new row and write into segment table.
				 * Meanwhile, insert a new row into trajectory table with segment information.
				 */
				
				
				mbr = new MBR(lowx, lowy, highx, highy, newSp.get(0).getT(), newSp.get(newSp.size() - 1).getT());
				newSp.trimToSize();
				SegmentTableRow str = new SegmentTableRow(sid, mbr, newSp);
				this.st.addNewRecord(str);
				TrajectoryTableRow ttr = new TrajectoryTableRow(tid, sindex, sid);
				this.tt.addNewRecord(ttr);
				
				i--;
				sid++;
				sindex++;
				
				lowx = Double.POSITIVE_INFINITY; 
				lowy = Double.POSITIVE_INFINITY;
				highx = Double.NEGATIVE_INFINITY; 
				highy = Double.NEGATIVE_INFINITY;
				
				count = 0;
				newSp = new ArrayList<SamplePoint>();
				
			}
			
			
		}
		/*
		 * create a new row and put the rest of trajectory in it and insert to segment table
		 */
		
		mbr = new MBR(lowx, lowy, highx, highy, newSp.get(0).getT(), newSp.get(newSp.size() - 1).getT());
		newSp.trimToSize();
		SegmentTableRow str = new SegmentTableRow(sid, mbr, newSp);
		this.st.addNewRecord(str);
		TrajectoryTableRow ttr = new TrajectoryTableRow(tid, sindex, sid);
		this.tt.addNewRecord(ttr);
		
		
		return true;
	}

	@Override
	public boolean InsertMultipleRecords() {
		// TODO Auto-generated method stub	
		return false;
	}

	/**
	 * implement from basicOperators
	 */
	@Override
	public boolean importBulkofData(ArrayList<ArrayList<SamplePoint>> data) {
		// TODO Auto-generated method stub
		
		int tid = 0;
		int sid = 0;	
		
		MBR mbr;
		
		
		for (ArrayList<SamplePoint> sp : data){
			
			int count = 0;		
			int sindex = 0;
			
			double lowx = Double.POSITIVE_INFINITY, lowy = Double.POSITIVE_INFINITY;
			double highx = Double.NEGATIVE_INFINITY, highy = Double.NEGATIVE_INFINITY;
			
			ArrayList<SamplePoint> newSp = new ArrayList<SamplePoint>(this.segmentCount);
			
			/*
			 * split a input trajectory into segments
			 */
			for (int i = 0; i < sp.size(); i++){
				if (count < this.segmentCount){
					newSp.add(sp.get(i));
					if (sp.get(i).getX() < lowx){
						lowx = sp.get(i).getX();
					}else if (sp.get(i).getX() > highx){
						highx = sp.get(i).getX();
					}
					
					if (sp.get(i).getY() < lowy){
						lowy = sp.get(i).getY();
					}else if (sp.get(i).getY() > highy){
						highy = sp.get(i).getY();
					}
					
					count++;
					
				}else{
					/*
					 * Reach the number limitation, create a new row and write into segment table.
					 * Meanwhile, insert a new row into trajectory table with segment information.
					 */
					
					mbr = new MBR(lowx, lowy, highx, highy, newSp.get(0).getT(), newSp.get(newSp.size() - 1).getT());
					SegmentTableRow str = new SegmentTableRow(sid, mbr, newSp);
					this.st.addNewRecord(str);
					TrajectoryTableRow ttr = new TrajectoryTableRow(tid, sindex, sid);
					this.tt.addNewRecord(ttr);
					
					i--;
					sid++;
					sindex++;
					
					lowx = Double.POSITIVE_INFINITY; 
					lowy = Double.POSITIVE_INFINITY;
					highx = Double.NEGATIVE_INFINITY; 
					highy = Double.NEGATIVE_INFINITY;
					
					count = 0;
					newSp = new ArrayList<SamplePoint>();
					
				}
				
				
			}
			/*
			 * create a new row and put the rest of trajectory in it and insert to segment table
			 */
			
			mbr = new MBR(lowx, lowy, highx, highy, newSp.get(0).getT(), newSp.get(newSp.size() - 1).getT());
			SegmentTableRow str = new SegmentTableRow(sid, mbr, newSp);
			this.st.addNewRecord(str);
			TrajectoryTableRow ttr = new TrajectoryTableRow(tid, sindex, sid);
			this.tt.addNewRecord(ttr);
			
			sid++;				
			tid++;
			
		}
		
		return true;
	}

	@Override
	public Trajectory selectOneRecordByTID(int tid) {
		
		ArrayList<ArrayList<SamplePoint>> spl = new ArrayList<ArrayList<SamplePoint>>();
		Trajectory tr = new Trajectory(tid);
		//find all sids that relate to this trajectory
		for (int sid : this.selectTableRowByTid(tid)){
			//get the trajectory segments
			for (SegmentTableRow st : this.st.getData()){
				if (st.getSid() == sid){
					spl.add(st.getData());
				}
			}
		}
		
		//combine the segments
		for (ArrayList<SamplePoint> sp : spl){
			for (SamplePoint p : sp){
				tr.addSamplePoint(new SamplePoint(p.getX(), p.getY(), p.getT()));
			}
		}
		
		
		return tr;
	}
	
	/**
	 * Find all sids that relate to this tid
	 * @param tid the trajectory id
	 * @return sids
	 */
	private ArrayList<Integer> selectTableRowByTid(int tid){
		
		ArrayList<Integer> sids = new ArrayList<Integer>();
		
		for (TrajectoryTableRow tt : this.tt.getData()){
			if (tt.getTid() == tid){
				sids.add(tt.getSid());
			}
		}
		
		return sids;
		
		
		
	}
	
	/**
	 * Delete all sid that relate to given tid
	 * @param tid the trajectory id
	 * @return sids
	 */
	private ArrayList<Integer> removeTableRowByTid(int tid){
		
		ArrayList<Integer> sids = new ArrayList<Integer>();
		
		for (int i = 0; i < this.tt.getData().size(); i++){
			if (tt.getData().get(i).getTid() == tid){
				sids.add(this.tt.removeByIndex(i).getSid());
				i--;
			}
		}
		
		return sids;
		
		
		
	}
	

	@Override
	public Trajectory deleteOneRecordByTID(int tid) {
		// TODO Auto-generated method stub
		ArrayList<ArrayList<SamplePoint>> spl = new ArrayList<ArrayList<SamplePoint>>();
		Trajectory tr = new Trajectory(tid);
		
		ArrayList<Integer> sids = this.removeTableRowByTid(tid);
		
		for (int sid : sids){
			for (int i = 0; i < this.st.getData().size(); i++){
				if (st.getData().get(i).getSid() == sid){
					
					spl.add(this.st.removeRowByIndex(i).getData());
					break;
				}
			}
		}
		
		
		for (ArrayList<SamplePoint> sp : spl){
			for (SamplePoint p : sp){
				tr.addSamplePoint(new SamplePoint(p.getX(), p.getY(), p.getT()));
			}
		}
		
		
		return tr;
	}

	@Override
	public Trajectory findNN(double x, double y, long startTime, long endTime,
			double maxSpeed) {
		// TODO Auto-generated method stub
		double distanceBestSoFar = Double.POSITIVE_INFINITY;
		int bestSoFarSid = -1;
		//exam each segments
		for (SegmentTableRow str : this.st.getData()){
			
			if (str.getMbr().getTs() > endTime){
				continue;
			}else if (str.getMbr().getTe() < startTime){
				continue;
			}else{
				if (commonMethods.Distance.checkArea(x, y, str.getMbr())){
					if (commonMethods.Distance.getMinMaxDistance(x, y, str.getMbr()) < distanceBestSoFar){
						ArrayList<SamplePoint> spl = str.getData();
						
						for (SamplePoint sp : spl){
							
							double distance = commonMethods.Distance.getDistance(x, y, sp.getX(), sp.getY());
						
							if (distance < distanceBestSoFar){
								
								if (sp.getT() >= startTime){
									if (sp.getT() <= endTime){
										distanceBestSoFar = distance;
										bestSoFarSid = str.getSid();
									}
								}
								
							}
												
						}				
						
					}else{
						continue;
					}
				}else{
					
					ArrayList<SamplePoint> spl = str.getData();
					for (SamplePoint sp : spl){
						double distance = commonMethods.Distance.getDistance(x, y, sp.getX(), sp.getY());
						
						if (distance < distanceBestSoFar){
							
							if (sp.getT() >= startTime){
								if (sp.getT() <= endTime){
									distanceBestSoFar = distance;
									bestSoFarSid = str.getSid();
								}
							}
							
						}
											
					}				
					
					
				}
			}
			
		}
		//find the tid that relate to the result sid
		int tid = this.findTidBySid(bestSoFarSid);
		//System.out.println("tid is " + tid);
		return this.selectOneRecordByTID(tid);
		//return null;
	}
	
	/**
	 * find the tid that relate to the result sid
	 * @param sid segment id
	 * @return tid
	 */
	private int findTidBySid(int sid){
		
		for (TrajectoryTableRow ttr : this.tt.getData()){
			if (ttr.getSid() == sid){
				return ttr.getTid();
			}
		}
		
		return -1;
		
	}
	

	@Override
	public Trajectory findNNByLine(double x, double y, long startTime,
			long endTime, double maxSpeed) {
		// TODO Auto-generated method stub
		return null;
	}
        
        
        public HashMap<Integer, ArrayList<SamplePoint>> SpatialTemporalWindowQuery(long midTime, long tRadius, double longitude, double latitude, double sRadius) {
		// TODO Auto-generated method stub
                                    HashMap<Integer, ArrayList<SamplePoint>> result = new HashMap<>();
		int segmentID;
		
		for (int i=0;i<this.tt.getData().size();i++){
			segmentID=this.tt.getData().get(i).getSid();
			if (this.st.getData().get(segmentID).getMbr().getTe() < (midTime-tRadius) 
					|| this.st.getData().get(segmentID).getMbr().getTs() > (midTime+tRadius)){
				continue;				
			}
			if(!IsColliding.isCollidingCircleRectangle(longitude,latitude, sRadius,
					(this.st.getData().get(segmentID).getMbr().getXhigh()+this.st.getData().get(segmentID).getMbr().getXlow())/2, 
					(this.st.getData().get(segmentID).getMbr().getYhigh()+this.st.getData().get(segmentID).getMbr().getYlow())/2, 
					this.st.getData().get(segmentID).getMbr().getXhigh()-this.st.getData().get(segmentID).getMbr().getXlow(),
					this.st.getData().get(segmentID).getMbr().getYhigh()-this.st.getData().get(segmentID).getMbr().getYlow())){
				continue;
			}
			for (int j=0;j<this.st.getData().get(segmentID).getData().size();j++){
				if(this.st.getData().get(segmentID).getData().get(j).getT()<=(midTime+tRadius)
						&& this.st.getData().get(segmentID).getData().get(j).getT()>=(midTime-tRadius)
						&& commonMethods.Distance.getDistance(longitude,latitude,this.st.getData().get(segmentID).getData().get(j).getX(),this.st.getData().get(segmentID).getData().get(j).getY())<=sRadius){
					 if (result.containsKey(this.tt.getData().get(i).getTid())){
							result.get(this.tt.getData().get(i).getTid()).add
                                                                                                                              (new SamplePoint(this.st.getData().get(segmentID).getData().get(j).getX(), 
									this.st.getData().get(segmentID).getData().get(j).getY(), 
									this.st.getData().get(segmentID).getData().get(j).getT()));
						}else{
							ArrayList<SamplePoint> temp = new ArrayList<>();
							temp.add(new SamplePoint(this.st.getData().get(segmentID).getData().get(j).getX(), 
									this.st.getData().get(segmentID).getData().get(j).getY(), 
									this.st.getData().get(segmentID).getData().get(j).getT()));
                                                             
						result.put(this.tt.getData().get(i).getTid(), temp);
						}
				}
			}
		}
		return result;
	}

}
