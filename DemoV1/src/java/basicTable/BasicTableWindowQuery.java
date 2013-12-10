package basicTable;

import java.util.ArrayList;
import commonMethods.IsColliding;
import dataS.QueryResultDataStruc;
import dataS.SamplePoint;
import advantageOperators.WindowQueryInterface;
import java.util.HashMap;

/**
 * implementation of window query for basic data structure
 * @author Xuefei Li
 *
 */
public class BasicTableWindowQuery extends BasicTable implements WindowQueryInterface {
	
	
	public BasicTableWindowQuery(){
		super();
	}
	
	@Override
	public ArrayList<QueryResultDataStruc> TimeWindowQuery(long midTime, long radius) {
		// TODO Auto-generated method stub
		ArrayList<QueryResultDataStruc> trajPoints=new ArrayList<QueryResultDataStruc>();
		for (int i=0;i<this.table.getRowRecord().size();i++){
			if (this.table.getRowRecord().get(i).getMbr().getTe() < (midTime-radius) || this.table.getRowRecord().get(i).getMbr().getTs() > (midTime+radius)){
				continue;				
			}
			else{
				for (int j=0;j<this.table.getRowRecord().get(i).getNumofPoints();j++){
					if(this.table.getRowRecord().get(i).getData().get(j).getT()<=(midTime+radius) && this.table.getRowRecord().get(i).getData().get(j).getT()>=(midTime-radius)){
						trajPoints.add(new QueryResultDataStruc(this.table.getRowRecord().get(i).getTid(),
								new SamplePoint(this.table.getRowRecord().get(i).getData().get(j).getX(), this.table.getRowRecord().get(i).getData().get(j).getY(), this.table.getRowRecord().get(i).getData().get(j).getT())));
					}
				}
			}
		}
		return trajPoints;
	}

	@Override
	public ArrayList<QueryResultDataStruc> SpatialWindowQuery(double longitude, double latitude,
			double radius) {
		// TODO Auto-generated method stub
		ArrayList<QueryResultDataStruc> trajPoints=new ArrayList<QueryResultDataStruc>();
		for (int i = 0; i < this.table.getRowRecord().size(); i++) {
			if(!IsColliding.isCollidingCircleRectangle(longitude,latitude, radius,
					(this.table.getRowRecord().get(i).getMbr().getXhigh()+this.table.getRowRecord().get(i).getMbr().getXlow())/2, 
					(this.table.getRowRecord().get(i).getMbr().getYhigh()+this.table.getRowRecord().get(i).getMbr().getYlow())/2, 
					this.table.getRowRecord().get(i).getMbr().getXhigh()-this.table.getRowRecord().get(i).getMbr().getXlow(),
					this.table.getRowRecord().get(i).getMbr().getYhigh()-this.table.getRowRecord().get(i).getMbr().getYlow())){
				continue;
			}
			else{
				for (int j = 0; j < this.table.getRowRecord().get(i).getNumofPoints(); j++) {
					if (commonMethods.Distance.getDistance(longitude,latitude,this.table.getRowRecord().get(i).getData().get(j).getX(),this.table.getRowRecord().get(i).getData().get(j).getY())<=radius){
						trajPoints.add(new QueryResultDataStruc(this.table.getRowRecord().get(i).getTid(),
								new SamplePoint(this.table.getRowRecord().get(i).getData().get(j).getX(), this.table.getRowRecord().get(i).getData().get(j).getY(), this.table.getRowRecord().get(i).getData().get(j).getT())));
					}
				}
			}
		}
		return trajPoints;
	}

	@Override
	public HashMap<Integer, ArrayList<SamplePoint>> SpatialTemporalWindowQuery(long midTime, long tRadius, double longitude, double latitude, double sRadius) {
		// TODO Auto-generated method stub
		//ArrayList<QueryResultDataStruc> trajPoints=new ArrayList<QueryResultDataStruc>();
                                    HashMap<Integer, ArrayList<SamplePoint>> result = new HashMap<>();
                
                                    System.out.println("here!!");
		for (int i=0;i<this.table.getRowRecord().size();i++){
			if (this.table.getRowRecord().get(i).getMbr().getTe() < (midTime-sRadius) || this.table.getRowRecord().get(i).getMbr().getTs() > (midTime+sRadius)){
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
							&& commonMethods.Distance.getDistance(longitude,latitude,this.table.getRowRecord().get(i).getData().get(j).getY(),this.table.getRowRecord().get(i).getData().get(j).getX())<=sRadius) {
						
                                                                                                                        
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
