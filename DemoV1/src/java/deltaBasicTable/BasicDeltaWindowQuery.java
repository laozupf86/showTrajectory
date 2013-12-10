package deltaBasicTable;

import java.util.ArrayList;
import commonMethods.IsColliding;
import dataS.QueryResultDataStruc;
import dataS.SamplePoint;
import advantageOperators.WindowQueryInterface;
import deltaBasicTable.BasicTable;
import java.util.HashMap;

/**
 * implementation of window query for basic+delta data structure
 * @author Xuefei Li
 *
 */
public class BasicDeltaWindowQuery extends BasicTable implements WindowQueryInterface{

	public BasicDeltaWindowQuery(){
		super();
	}
	
	@Override
	public ArrayList<QueryResultDataStruc> TimeWindowQuery(long midTime, long radius) {
		// TODO Auto-generated method stub
		ArrayList<QueryResultDataStruc> trajPoints=new ArrayList<QueryResultDataStruc>();
		long tempTime=0;
		double tempLongi=0;
		double tempLati=0;
		for (int i=0;i<this.table.getRowRecord().size();i++){
			if (this.table.getRowRecord().get(i).getMbr().getTe() < (midTime-radius) || this.table.getRowRecord().get(i).getMbr().getTs() > (midTime+radius)){
				continue;				
			}
			else{
				tempTime=this.table.getRowRecord().get(i).getData().getStartPoint().getT();
				tempLongi=this.table.getRowRecord().get(i).getData().getStartPoint().getX();
				tempLati=this.table.getRowRecord().get(i).getData().getStartPoint().getY();
				if( tempTime <= (midTime+radius) && tempTime >= (midTime-radius)){
					trajPoints.add(new QueryResultDataStruc(this.table.getRowRecord().get(i).getTid(),this.table.getRowRecord().get(i).getData().getStartPoint()));
				}
				for (int j=0;j<this.table.getRowRecord().get(i).getNumofPoints()-1;j++){
					tempTime=tempTime+this.table.getRowRecord().get(i).getData().getDc().get(j).getDt()*1000;
					tempLongi=tempLongi+(double)this.table.getRowRecord().get(i).getData().getDc().get(j).getDx()/1000000;
					tempLati=tempLati+(double)this.table.getRowRecord().get(i).getData().getDc().get(j).getDy()/1000000;
					if(tempTime <= (midTime+radius) && tempTime >= (midTime-radius)){
						trajPoints.add(new QueryResultDataStruc(this.table.getRowRecord().get(i).getTid(),new SamplePoint(tempLongi,tempLati,tempTime)));
					}				
				}
			}
		}
		return trajPoints;
	}

	@Override
	public ArrayList<QueryResultDataStruc> SpatialWindowQuery(double longitude, double latitude, double radius) {
		// TODO Auto-generated method stub
		ArrayList<QueryResultDataStruc> trajPoints=new ArrayList<QueryResultDataStruc>();
		double tempLongi=0;
		double tempLati=0;	
		long tempTime=0;
		
		for (int i = 0; i < this.table.getRowRecord().size(); i++) {
			tempTime=this.table.getRowRecord().get(i).getData().getStartPoint().getT();
			tempLongi=this.table.getRowRecord().get(i).getData().getStartPoint().getX();
			tempLati=this.table.getRowRecord().get(i).getData().getStartPoint().getY();
			
			if(!IsColliding.isCollidingCircleRectangle(longitude,latitude, radius,
					(this.table.getRowRecord().get(i).getMbr().getXhigh()+this.table.getRowRecord().get(i).getMbr().getXlow())/2, 
					(this.table.getRowRecord().get(i).getMbr().getYhigh()+this.table.getRowRecord().get(i).getMbr().getYlow())/2, 
					this.table.getRowRecord().get(i).getMbr().getXhigh()-this.table.getRowRecord().get(i).getMbr().getXlow(),
					this.table.getRowRecord().get(i).getMbr().getYhigh()-this.table.getRowRecord().get(i).getMbr().getYlow())){
				continue;
			}
			if (commonMethods.Distance.getDistance(longitude, latitude, tempLongi, tempLati) <= radius) {
				trajPoints.add(new QueryResultDataStruc(this.table.getRowRecord().get(i).getTid(),this.table.getRowRecord().get(i).getData().getStartPoint()));
			}

			for (int j = 0; j < this.table.getRowRecord().get(i).getNumofPoints()-1; j++) {
				tempTime=tempTime+this.table.getRowRecord().get(i).getData().getDc().get(j).getDt()*1000;
				tempLongi=tempLongi+(double)this.table.getRowRecord().get(i).getData().getDc().get(j).getDx()/1000000;
				tempLati=tempLati+(double)this.table.getRowRecord().get(i).getData().getDc().get(j).getDy()/1000000;
				
				if (commonMethods.Distance.getDistance(longitude, latitude, tempLongi,tempLati) <= radius) {
					trajPoints.add(new QueryResultDataStruc(this.table.getRowRecord().get(i).getTid(),new SamplePoint(tempLongi,tempLati,tempTime)));
				}				
			}
		}
		return trajPoints;
	}

	@Override
	public HashMap<Integer, ArrayList<SamplePoint>> SpatialTemporalWindowQuery(long midTime, long tRadius, double longitude, double latitude, double sRadius) {
		// TODO Auto-generated method stub
		HashMap<Integer, ArrayList<SamplePoint>> result = new HashMap<>();
		double tempLongi=0;
		double tempLati=0;	
		long tempTime=0;
		
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
