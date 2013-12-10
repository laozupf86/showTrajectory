package deltaSegmentedTable;

import java.util.ArrayList;

import dataS.DeltaCode;
import dataS.MBR;
import dataS.SamplePoint;
import dataS.SamplePointList;
import dataS.Trajectory;
import advantageOperators.NNSearch;
import basicOperators.DeleteData;
import basicOperators.InsertData;
import basicOperators.SelectData;
import commonMethods.IsColliding;
import java.util.HashMap;

/**
 * The delta encoded segmented database with operators.
 * @author Haozhou
 *
 */


public class SegmentedTable implements InsertData, SelectData, DeleteData, NNSearch{

	
	protected SegmentTable st;
	protected TrajectoryTable tt;
	//the points limitation for a trajectory segment
	private int segmentCount = 200;
	
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
		this.st = new SegmentTable();
		this.tt = new TrajectoryTable();
		this.segmentCount = segmentCount;
	}
	
	@Override
	public boolean InsertSingleRecord(int tid, ArrayList<SamplePoint> sp) {
		
		int sid = st.getLastSID() + 1;
		
		double lowx = Double.POSITIVE_INFINITY, lowy = Double.POSITIVE_INFINITY;
		double highx = Double.NEGATIVE_INFINITY, highy = Double.NEGATIVE_INFINITY;
		
		MBR mbr;
		
		int count = 0;		
		int sindex = 0;
		
		ArrayList<DeltaCode> newSp = new ArrayList<DeltaCode>(segmentCount);
		//set the start point for encoding
		SamplePoint startPoint = sp.get(0); 
		
		if (startPoint.getX() < lowx){
			lowx = startPoint.getX();
		}else if (startPoint.getX() > highx){
			highx = startPoint.getX();
		}
		
		if (startPoint.getY() < lowy){
			lowy = startPoint.getY();
		}else if (startPoint.getY() > highy){
			highy = startPoint.getY();
		}
		
		/*
		 * split a input trajectory into segments
		 */
		for (int i = 1; i < sp.size(); i++){
			if (count < segmentCount){
				//encoding
				double x = (sp.get(i).getX() - startPoint.getX())*1000000;
				double y = (sp.get(i).getY() - startPoint.getY())*1000000;
				
				short dx = (short)x;
				short dy = (short)y;
				short dt = (short)((sp.get(i).getT() - startPoint.getT())/1000);
				newSp.add(new DeltaCode(dx, dy, dt));
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
				short dt = newSp.get(newSp.size() - 1).getDt();
				mbr = new MBR(lowx, lowy, highx, highy, startPoint.getT() , (startPoint.getT() + (long)(dt)));
				SegmentTableRow str = new SegmentTableRow(sid, mbr, new SamplePointList(startPoint, newSp));
				this.st.addNewRecord(str);
				TrajectoryTableRow ttr = new TrajectoryTableRow(tid, sindex, sid);
				this.tt.addNewRecord(ttr);
				
				sid++;
				sindex++;
				
				lowx = Double.POSITIVE_INFINITY; 
				lowy = Double.POSITIVE_INFINITY;
				highx = Double.NEGATIVE_INFINITY; 
				highy = Double.NEGATIVE_INFINITY;
				
				
				startPoint = sp.get(i);
				if (startPoint.getX() < lowx){
					lowx = startPoint.getX();
				}else if (startPoint.getX() > highx){
					highx = startPoint.getX();
				}
				
				if (startPoint.getY() < lowy){
					lowy = startPoint.getY();
				}else if (startPoint.getY() > highy){
					highy = startPoint.getY();
				}
				
				
				count = 0;
				newSp = new ArrayList<DeltaCode>(segmentCount);
				
			}
			
		}
		
		if (newSp.isEmpty()){
			// only one point left
			mbr = new MBR(lowx, lowy, highx, highy, startPoint.getT() , startPoint.getT());
		}else{
			short dt = newSp.get(newSp.size() - 1).getDt();
			mbr = new MBR(lowx, lowy, highx, highy, startPoint.getT() , (startPoint.getT() + (long)dt));
		}
		
		
		SegmentTableRow str = new SegmentTableRow(sid, mbr, new SamplePointList(startPoint, newSp));
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

	@Override
	public boolean importBulkofData(ArrayList<ArrayList<SamplePoint>> data) {
		// TODO Auto-generated method stub
		
		int tid = 0;
		int sid = 0;	
		
		MBR mbr;
		
		for (ArrayList<SamplePoint> sp : data){
			
			/*
			 * split a input trajectory into segments
			 */
			
			double lowx = Double.POSITIVE_INFINITY, lowy = Double.POSITIVE_INFINITY;
			double highx = Double.NEGATIVE_INFINITY, highy = Double.NEGATIVE_INFINITY;
			
			
			int count = 0;		
			int sindex = 0;
			
			ArrayList<DeltaCode> newSp = new ArrayList<DeltaCode>(segmentCount);
			SamplePoint startPoint = sp.get(0); 
			
			if (startPoint.getX() < lowx){
				lowx = startPoint.getX();
			}else if (startPoint.getX() > highx){
				highx = startPoint.getX();
			}
			
			if (startPoint.getY() < lowy){
				lowy = startPoint.getY();
			}else if (startPoint.getY() > highy){
				highy = startPoint.getY();
			}
			
			for (int i = 1; i < sp.size(); i++){
				if (count < segmentCount){
					
					double x = (sp.get(i).getX() - startPoint.getX())*1000000;
					double y = (sp.get(i).getY() - startPoint.getY())*1000000;;
					
					short dx = (short)x;
					short dy = (short)y;
					short dt = (short)((sp.get(i).getT() - startPoint.getT())/1000);
					newSp.add(new DeltaCode(dx, dy, dt));
					if (sp.get(i).getX() < lowx){
						lowx = sp.get(i).getX();
					}
					
					if (sp.get(i).getX() > highx){
						highx = sp.get(i).getX();
					}
					
					if (sp.get(i).getY() < lowy){
						lowy = sp.get(i).getY();
					}
					
					if (sp.get(i).getY() > highy){
						highy = sp.get(i).getY();
					}
					
					count++;
					
				}else{
					/*
					 * split a input trajectory into segments
					 */
					
					long dt = (long)newSp.get(newSp.size() - 1).getDt();
					mbr = new MBR(lowx, lowy, highx, highy, startPoint.getT() , (startPoint.getT() + dt*1000));
					
					SegmentTableRow str = new SegmentTableRow(sid, mbr, new SamplePointList(startPoint, newSp));
					this.st.addNewRecord(str);
					TrajectoryTableRow ttr = new TrajectoryTableRow(tid, sindex, sid);
					this.tt.addNewRecord(ttr);
					
					sid++;
					sindex++;
					
					lowx = Double.POSITIVE_INFINITY; 
					lowy = Double.POSITIVE_INFINITY;
					highx = Double.NEGATIVE_INFINITY; 
					highy = Double.NEGATIVE_INFINITY;
					
					
					startPoint = sp.get(i);
					if (startPoint.getX() < lowx){
						lowx = startPoint.getX();
					}
					
					if (startPoint.getX() > highx){
						highx = startPoint.getX();
					}
					
					if (startPoint.getY() < lowy){
						lowy = startPoint.getY();
					}
					
					if (startPoint.getY() > highy){
						highy = startPoint.getY();
					}
					
					
					count = 0;
					newSp = new ArrayList<DeltaCode>(segmentCount);
					
				}
				
			}
			
			if (newSp.isEmpty()){
				mbr = new MBR(lowx, lowy, highx, highy, startPoint.getT() , startPoint.getT());
			}else{
				long dt = (long)newSp.get(newSp.size() - 1).getDt();
				mbr = new MBR(lowx, lowy, highx, highy, startPoint.getT() , (startPoint.getT() + dt*1000));
			}
			
			/*
			 * create a new row and put the rest of trajectory in it and insert to segment table
			 */
			SegmentTableRow str = new SegmentTableRow(sid, mbr, new SamplePointList(startPoint, newSp));
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
		// TODO Auto-generated method stub
		
		ArrayList<SamplePointList> spl = new ArrayList<SamplePointList>();
		Trajectory tr = new Trajectory(tid);
		
		for (int sid : this.selectTableRowByTid(tid)){
			for (SegmentTableRow st : this.st.getData()){
				if (st.getSid() == sid){
					spl.add(st.getData());
					break;
				}
			}
		}
		
		
		for (SamplePointList sp : spl){
			tr.addSamplePoint(sp.getStartPoint());
			for (DeltaCode dc : sp.getDc()){
				double x = ((double)dc.getDx()/1000000) + sp.getStartPoint().getX();
				double y = ((double)dc.getDy()/1000000) + sp.getStartPoint().getY();
				long t = sp.getStartPoint().getT() + dc.getDt()*1000;
				tr.addSamplePoint(new SamplePoint(x, y, t));
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
		ArrayList<SamplePointList> spl = new ArrayList<SamplePointList>();
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
		
		
		for (SamplePointList sp : spl){
			tr.addSamplePoint(sp.getStartPoint());
			for (DeltaCode dc : sp.getDc()){
				double x = ((double)dc.getDx()/1000000) + sp.getStartPoint().getX();
				double y = ((double)dc.getDy()/1000000) + sp.getStartPoint().getY();
				long t = sp.getStartPoint().getT() + dc.getDt()*1000;
				tr.addSamplePoint(new SamplePoint(x, y, t));
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
		
		for (SegmentTableRow str : this.st.getData()){
			
			
			if (str.getMbr().getTs() > endTime){
				continue;
			}else if (str.getMbr().getTe() < startTime){
				continue;
			}else{
				if (commonMethods.Distance.checkArea(x, y, str.getMbr())){
					if (commonMethods.Distance.getMinMaxDistance(x, y, str.getMbr()) < distanceBestSoFar){
						SamplePointList spl = str.getData();
						SamplePoint sp = spl.getStartPoint();
						double distance = commonMethods.Distance.getDistance(x, y, sp.getX(), sp.getY());
						
						if (distance < distanceBestSoFar){
							
							if (sp.getT() >= startTime){
								if (sp.getT() <= endTime){
									distanceBestSoFar = distance;
									bestSoFarSid = str.getSid();
								}
							}
							
						}
						
						//trTemp.add(sp);
						for (DeltaCode dc : spl.getDc()){
							double tx = spl.getStartPoint().getX() + ((double)dc.getDx()/1000000);
							double ty = spl.getStartPoint().getY() + ((double)dc.getDy()/1000000);
							long t = spl.getStartPoint().getT() + dc.getDt()*1000;
							
							distance = commonMethods.Distance.getDistance(x, y, tx, ty);
							if (distance < distanceBestSoFar){
								
								if (t >= startTime){
									if (t <= endTime){
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
					SamplePointList spl = str.getData();
					SamplePoint sp = spl.getStartPoint();
					double distance = commonMethods.Distance.getDistance(x, y, sp.getX(), sp.getY());
					if (distance < distanceBestSoFar){
						
						if (sp.getT() >= startTime){
							if (sp.getT() <= endTime){
								distanceBestSoFar = distance;
								bestSoFarSid = str.getSid();
							}
						}
						
					}
					for (DeltaCode dc : spl.getDc()){
						double tx = spl.getStartPoint().getX() + ((double)dc.getDx()/1000000);
						double ty = spl.getStartPoint().getY() + ((double)dc.getDy()/1000000);
						long t = spl.getStartPoint().getT() + dc.getDt()*1000;
						
						distance = commonMethods.Distance.getDistance(x, y, tx, ty);
						
						if (distance < distanceBestSoFar){
							
							if (t >= startTime){
								if (t <= endTime){
									distanceBestSoFar = distance;
									bestSoFarSid = str.getSid();
								}
							}
							
						}
											
					}
				}
			}
			
		}
		
		int tid = this.findTidBySid(bestSoFarSid);
		//System.out.println("tid is " + tid);
		//return null;
		return this.selectOneRecordByTID(tid);
	}
	
	
	
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
		long tempTime=0;
		double tempLongi=0;
		double tempLati=0;
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
			tempTime=this.st.getData().get(segmentID).getData().getStartPoint().getT();
			tempLongi=this.st.getData().get(segmentID).getData().getStartPoint().getX();
			tempLati=this.st.getData().get(segmentID).getData().getStartPoint().getY();
			
			if( tempTime <= (midTime+tRadius) && tempTime >= (midTime-tRadius) && commonMethods.Distance.getDistance(longitude, latitude, tempLongi, tempLati) <= sRadius){
				 if (result.containsKey(this.tt.getData().get(i).getTid())){
							result.get(this.tt.getData().get(i).getTid()).add
                                                                                                                              (new SamplePoint(tempLongi,tempLati,tempTime) );
						}else{
							ArrayList<SamplePoint> temp = new ArrayList<>();
							temp.add(new SamplePoint(tempLongi,tempLati,tempTime));
                                                             
						result.put(this.tt.getData().get(i).getTid(), temp);
						}
			}
			
			for (int j = 0; j < this.st.getData().get(segmentID).getData().getDc().size(); j++) {
				tempTime=tempTime+this.st.getData().get(segmentID).getData().getDc().get(j).getDt()*1000;
				tempLongi=tempLongi+(double)this.st.getData().get(segmentID).getData().getDc().get(j).getDx()/1000000;
				tempLati=tempLati+(double)this.st.getData().get(segmentID).getData().getDc().get(j).getDy()/1000000;
				
				if(tempTime<=(midTime+tRadius)&& tempTime>=(midTime-tRadius) && commonMethods.Distance.getDistance(longitude, latitude, tempLongi, tempLati) <= sRadius){					
					 if (result.containsKey(this.tt.getData().get(i).getTid())){
							result.get(this.tt.getData().get(i).getTid()).add
                                                                                                                              (new SamplePoint(tempLongi,tempLati,tempTime) );
						}else{
							ArrayList<SamplePoint> temp = new ArrayList<>();
							temp.add(new SamplePoint(tempLongi,tempLati,tempTime));
                                                             
						result.put(this.tt.getData().get(i).getTid(), temp);
						}
				}
			}
		}
		return result;
	}



}
