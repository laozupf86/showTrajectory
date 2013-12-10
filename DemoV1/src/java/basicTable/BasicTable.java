package basicTable;

import java.util.ArrayList;

import dataS.MBR;
import dataS.SamplePoint;
import dataS.Trajectory;
import advantageOperators.NNSearch;
import basicOperators.DeleteData;
import basicOperators.InsertData;
import basicOperators.OtherOperators;
import basicOperators.SelectData;
import commonMethods.IsColliding;
import java.util.HashMap;

/**
 * The main part of basic in-memory table, includes table itself and operators.
 * @author Haozhou
 *
 */

public class BasicTable implements InsertData, OtherOperators, SelectData, DeleteData, NNSearch {
	
	//the basic table data structure
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
		
		//build MBR
		for (SamplePoint p : sp){
			if (p.getX() < lowx){
				lowx = p.getX();
			}else if (p.getX() > highx){
				highx = p.getX();
			}
			
			if (p.getY() < lowy){
				lowy = p.getY();
			}else if (p.getY() > highy){
				highy = p.getY();
			}		
			
		}
		
		mbr = new MBR(lowx, lowy, highx, highy, sp.get(0).getT(), sp.get(sp.size() - 1).getT());
		rr = new TableRow(sp.size(), mbr, sp, tid);
		
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
		
		//each trajectory is a single record
		for (ArrayList<SamplePoint> sp : data){
			this.InsertSingleRecord(tid, sp);
			tid++;
			
		}
		return true;
	}

	@Override
	public ArrayList<Integer> numOfObjects() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Trajectory selectOneRecordByTID(int tid) {
		// TODO Auto-generated method stub
		Trajectory tr = new Trajectory(tid);
		
		ArrayList<SamplePoint> spl = this.table.getPointsByTID(tid);
		
		
		
		for (SamplePoint p : spl){
			
			tr.addSamplePoint(new SamplePoint(p.getX(), p.getY(), p.getT()));
		}
		
		
		return tr;
	}

	@Override
	public Trajectory deleteOneRecordByTID(int tid) {
		// TODO Auto-generated method stub
		Trajectory tr = new Trajectory(tid);
		
		ArrayList<SamplePoint> spl = this.table.removePointsByTID(tid);
			
		for (SamplePoint p : spl){
			
			tr.addSamplePoint(new SamplePoint(p.getX(), p.getY(), p.getT()));
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
			
			if (rr.getMbr().getTs() > endTime){
				continue;
			}else if (rr.getMbr().getTe() < startTime){
				continue;
			}else{
				if (commonMethods.Distance.checkArea(x, y, rr.getMbr())){
					if (commonMethods.Distance.getMinMaxDistance(x, y, rr.getMbr()) < distanceBestSoFar){
						ArrayList<SamplePoint> spl = rr.getData();
						for (SamplePoint sp : spl){
							double distance = commonMethods.Distance.getDistance(x, y, sp.getX(), sp.getY());
							if (distance < distanceBestSoFar){							
								if (sp.getT() >= startTime){
									if (sp.getT() <= endTime){						
										distanceBestSoFar = distance;
										bestSoFarTid = rr.getTid();
									}
								}
								
							}
						}
					}
				}else{
					if (commonMethods.Distance.getMinMaxDistance(x, y, rr.getMbr()) < distanceBestSoFar){
						ArrayList<SamplePoint> spl = rr.getData();
						
						for (SamplePoint sp : spl){
							double distance = commonMethods.Distance.getDistance(x, y, sp.getX(), sp.getY());
							if (distance < distanceBestSoFar){							
								if (sp.getT() >= startTime){
									if (sp.getT() <= endTime){						
										distanceBestSoFar = distance;
										bestSoFarTid = rr.getTid();
									}
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
		//ArrayList<QueryResultDataStruc> trajPoints=new ArrayList<QueryResultDataStruc>();
                                    HashMap<Integer, ArrayList<SamplePoint>> result = new HashMap<>();
                 
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
				for (int j = 0; j < this.table.getRowRecord().get(i).getNumofPoints(); j++) {
                                   
					if (this.table.getRowRecord().get(i).getData().get(j).getT()<=(midTime+tRadius) 
							&& this.table.getRowRecord().get(i).getData().get(j).getT()>=(midTime-tRadius)
							&& commonMethods.Distance.getDistance(longitude,latitude,this.table.getRowRecord().get(i).getData().get(j).getX(),this.table.getRowRecord().get(i).getData().get(j).getY())<=sRadius) {
						
                                                                                                                        
                                                                                            if (result.containsKey(this.table.getRowRecord().get(i).getTid())){
							result.get(this.table.getRowRecord().get(i).getTid()).add
                                                                                                                            (new SamplePoint(this.table.getRowRecord().get(i).getData().get(j).getX(), 
                                                                                                                            this.table.getRowRecord().get(i).getData().get(j).getY(), 
                                                                                                                            this.table.getRowRecord().get(i).getData().get(j).getT()));
						}else{
							ArrayList<SamplePoint> temp = new ArrayList<SamplePoint>();
							temp.add(new SamplePoint(this.table.getRowRecord().get(i).getData().get(j).getX(), 
                                                                                                                            this.table.getRowRecord().get(i).getData().get(j).getY(), 
                                                                                                                            this.table.getRowRecord().get(i).getData().get(j).getT()));
                                                             
						result.put(this.table.getRowRecord().get(i).getTid(), temp);
						}
                                            
                                            
                                            //trajPoints.add(new QueryResultDataStruc(this.table.getRowRecord().get(i).getTid(), 
								//new SamplePoint(this.table.getRowRecord().get(i).getData().get(j).getX(), this.table.getRowRecord().get(i).getData().get(j).getY(), this.table.getRowRecord().get(i).getData().get(j).getT())));
					}
				}
			}
		}
		return result;
	}


}
