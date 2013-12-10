package deltaBasicTable;

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
 * The in-memory delta encoded basic table with operators
 * @author uqhwan21
 *
 */

public class BasicTable implements InsertData, SelectData, DeleteData, NNSearch {
	
	protected Table table;
	
	public BasicTable(){
		this.table = new Table();
	}

	@Override
	public boolean InsertSingleRecord(int tid, ArrayList<SamplePoint> sp) {
		// TODO Auto-generated method stub
		
		double lowx = Double.POSITIVE_INFINITY, lowy = Double.POSITIVE_INFINITY;
		double highx = Double.NEGATIVE_INFINITY, highy = Double.NEGATIVE_INFINITY;
		
		MBR mbr;
		TableRow rr;
		
		int count = 0;
		ArrayList<DeltaCode> dc = new ArrayList<DeltaCode>();
		for (SamplePoint p : sp){
			
			/*
			 * encode the sample points by using delta 
			 */
			if (count != 0){
				double x = (sp.get(count).getX() - sp.get(count - 1).getX())*1000000;
				double y = (sp.get(count).getY() - sp.get(count - 1).getY())*1000000;
				
				short dx = (short)x;
				short dy = (short)y;
				short dt = (short)(sp.get(count).getT() - sp.get(count - 1).getT());
				dc.add(new DeltaCode(dx, dy, dt));
			}
			
			/*
			 * Get the MBR 
			 */
			
			if (p.getX() < lowx){
				lowx = p.getX();
			}
			
			if (p.getX() > highx){
				highx = p.getX();
			}
			
			if (p.getY() < lowy){
				lowy = p.getY();
			}
			
			if (p.getY() > highy){
				highy = p.getY();
			}
			
			count++;
			
		}
		
		mbr = new MBR(lowx, lowy, highx, highy, sp.get(0).getT(), sp.get(sp.size() - 1).getT());
		rr = new TableRow(sp.size(), mbr, new SamplePointList(sp.get(0), dc), tid);
		
		this.table.addNewRecord(rr);
		
		return true;
	}

	@Override
	public boolean InsertMultipleRecords() {
		// TODO Auto-generated method stub		
		return false;
	}
	
	
	/**
	 * used to upload bulk data, implement from basicOperators
	 */
	public boolean importBulkofData(ArrayList<ArrayList<SamplePoint>> data){
		int tid = 0;
		
		for (ArrayList<SamplePoint> sp : data){
			this.InsertSingleRecord(tid, sp);
			tid++;
			
		}
		return true;
		
	}

	@Override
	public Trajectory selectOneRecordByTID(int tid) {
		// TODO Auto-generated method stub
		
		Trajectory tr = new Trajectory(tid);
		
		SamplePointList spl = this.table.getPointsByTID(tid);
		
		SamplePoint sp = spl.getStartPoint();
		tr.addSamplePoint(sp);
		//decode the points
		for (DeltaCode dc : spl.getDc()){
			double x = sp.getX() + ((double)dc.getDx()/1000000);
			double y = sp.getY() + ((double)dc.getDy()/1000000);
			//recover the time to ms unit
			long t = sp.getT() + dc.getDt()*1000;
			sp = new SamplePoint(x, y, t);
			tr.addSamplePoint(sp);
		}
		
		
		return tr;
	}

	@Override
	public Trajectory deleteOneRecordByTID(int tid) {
		// TODO Auto-generated method stub
			
		Trajectory tr = new Trajectory(tid);
		
		SamplePointList spl = this.table.removePointsByTID(tid);
		
		SamplePoint sp = spl.getStartPoint();
		tr.addSamplePoint(sp);
		//decode the points
		for (DeltaCode dc : spl.getDc()){
			double x = sp.getX() + ((double)dc.getDx()/1000000);
			double y = sp.getY() + ((double)dc.getDy()/1000000);
			//recover the time to ms unit
			long t = sp.getT() + dc.getDt()*1000;
			sp = new SamplePoint(x, y, t);
			tr.addSamplePoint(sp);
		}	
		return tr;
	}

	@Override
	public Trajectory findNN(double x, double y, long startTime, long endTime,
			double maxSpeed) {
		// TODO Auto-generated method stub
		double distanceBestSoFar = Double.POSITIVE_INFINITY;
		int bestSoFarTid = -1;
		
		
		for (TableRow rr : this.table.getRowRecord()){
			//check the time interval
			if (rr.getMbr().getTs() > endTime){
				continue;
			}else if (rr.getMbr().getTe() < startTime){
				continue;
			}else{
				//check whether the query point is in MBR or not
				if (commonMethods.Distance.checkArea(x, y, rr.getMbr())){
					//not in the MBR, check MinMaxDistance
					if (commonMethods.Distance.getMinMaxDistance(x, y, rr.getMbr()) < distanceBestSoFar){
						SamplePointList spl = rr.getData();
						SamplePoint sp = spl.getStartPoint();
						double distance = commonMethods.Distance.getDistance(x, y, sp.getX(), sp.getY());
					
						if (distance < distanceBestSoFar){
							//check whether the candidate is pass during the query time
							if (sp.getT() >= startTime){
								if (sp.getT() <= endTime){
									//update the result
									distanceBestSoFar = distance;
									bestSoFarTid = rr.getTid();
								}
							}
							
						}
						
						//decode the trajectory
						for (DeltaCode dc : spl.getDc()){
							double tx = sp.getX() + ((double)dc.getDx()/1000000);
							double ty = sp.getY() + ((double)dc.getDy()/1000000);
							long t = sp.getT() + dc.getDt()*1000;
							
							sp = new SamplePoint(tx, ty, t);
							distance = commonMethods.Distance.getDistance(x, y, tx, ty);
							
							if (distance < distanceBestSoFar){
								
								if (t >= startTime){
									if (t <= endTime){
										
										distanceBestSoFar = distance;
										bestSoFarTid = rr.getTid();
									}
								}
								
							}
												
						}
						
					}
				}else{
					//inside the MBR, check directly
					SamplePointList spl = rr.getData();
					
					SamplePoint sp = spl.getStartPoint();
					double distance = commonMethods.Distance.getDistance(x, y, sp.getX(), sp.getY());
				
					if (distance < distanceBestSoFar){
						
						if (sp.getT() >= startTime){
							if (sp.getT() <= endTime){
								
								distanceBestSoFar = distance;
								bestSoFarTid = rr.getTid();
							}
						}
						
					}
					
					
					for (DeltaCode dc : spl.getDc()){
						double tx = sp.getX() + ((double)dc.getDx()/1000000);
						double ty = sp.getY() + ((double)dc.getDy()/1000000);
						long t = sp.getT() + dc.getDt()*1000;
						
						sp = new SamplePoint(tx, ty, t);
						distance = commonMethods.Distance.getDistance(x, y, tx, ty);
						
						if (distance < distanceBestSoFar){
							
							if (t >= startTime){
								if (t <= endTime){
									
									distanceBestSoFar = distance;
									bestSoFarTid = rr.getTid();
								}
							}
							
						}
											
					}
				}			
			}
			
		}
		
		if (bestSoFarTid == -1){
			return null;
		}
		
		//System.out.println("tid is " + bestSoFarTid);
		return this.selectOneRecordByTID(bestSoFarTid);
		//return null;
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
		double tempLongi=0;
		double tempLati=0;	
		long tempTime=0;
		
                // 
                  
                
		for (int i=0;i<this.table.getRowRecord().size();i++){
			if (this.table.getRowRecord().get(i).getMbr().getTe() < (midTime-tRadius) || this.table.getRowRecord().get(i).getMbr().getTs() > (midTime+tRadius)){	
                            continue;	
                                
			}
			if(!IsColliding.isCollidingCircleRectangle(longitude,latitude, sRadius,
					(this.table.getRowRecord().get(i).getMbr().getXhigh()+this.table.getRowRecord().get(i).getMbr().getXlow())/2, 
					(this.table.getRowRecord().get(i).getMbr().getYhigh()+this.table.getRowRecord().get(i).getMbr().getYlow())/2, 
					this.table.getRowRecord().get(i).getMbr().getXhigh()-this.table.getRowRecord().get(i).getMbr().getXlow(),
					this.table.getRowRecord().get(i).getMbr().getYhigh()-this.table.getRowRecord().get(i).getMbr().getYlow())){
				continue;
			}
			else{
				tempTime=this.table.getRowRecord().get(i).getData().getStartPoint().getT();
				tempLongi=this.table.getRowRecord().get(i).getData().getStartPoint().getX();
				tempLati=this.table.getRowRecord().get(i).getData().getStartPoint().getY();
				
				if( tempTime <= (midTime+tRadius) && tempTime >= (midTime-tRadius) && commonMethods.Distance.getDistance(longitude, latitude, tempLongi, tempLati) <= sRadius){
					 if (result.containsKey(this.table.getRowRecord().get(i).getTid())){
							result.get(this.table.getRowRecord().get(i).getTid()).add
                                                                                                                              (this.table.getRowRecord().get(i).getData().getStartPoint());
						}else{
							ArrayList<SamplePoint> temp = new ArrayList<>();
							temp.add(this.table.getRowRecord().get(i).getData().getStartPoint());
                                                             
						result.put(this.table.getRowRecord().get(i).getTid(), temp);
						}
				}
				for (int j = 0; j < this.table.getRowRecord().get(i).getNumofPoints()-1; j++) {
					tempTime=tempTime+this.table.getRowRecord().get(i).getData().getDc().get(j).getDt()*1000;
					tempLongi=tempLongi+(double)this.table.getRowRecord().get(i).getData().getDc().get(j).getDx()/1000000;
					tempLati=tempLati+(double)this.table.getRowRecord().get(i).getData().getDc().get(j).getDy()/1000000;
					
					if(tempTime <= (midTime+tRadius) && tempTime >= (midTime-tRadius) && commonMethods.Distance.getDistance(longitude, latitude, tempLongi,tempLati) <= sRadius){
						 if (result.containsKey(this.table.getRowRecord().get(i).getTid())){
							result.get(this.table.getRowRecord().get(i).getTid()).add
                                                                                                                            (new SamplePoint(tempLongi, 
                                                                                                                           tempLati, 
                                                                                                                            tempTime));
						}else{
							ArrayList<SamplePoint> temp = new ArrayList<>();
							temp.add(new SamplePoint(tempLongi, 
                                                                                                                           tempLati, tempTime));
                                                             
						result.put(this.table.getRowRecord().get(i).getTid(), temp);
						}
					}				
				}
			}
		}
		return result;
	}
}
