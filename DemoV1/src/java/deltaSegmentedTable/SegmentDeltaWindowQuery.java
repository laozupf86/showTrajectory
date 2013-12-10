package deltaSegmentedTable;

import java.util.ArrayList;
import commonMethods.IsColliding;
import dataS.QueryResultDataStruc;
import dataS.SamplePoint;
import advantageOperators.WindowQueryInterface;
import java.util.HashMap;

/**
 * implementation of window query for segment+delta data structure
 * @author Xuefei Li
 *
 */
public class SegmentDeltaWindowQuery extends SegmentedTable implements WindowQueryInterface{
	
	public SegmentDeltaWindowQuery(){
		super();
	}

	@Override
	public ArrayList<QueryResultDataStruc> TimeWindowQuery(long midTime, long radius) {
		// TODO Auto-generated method stub
		ArrayList<QueryResultDataStruc> trajPoints=new ArrayList<QueryResultDataStruc>();
		long tempTime=0;
		double tempLongi=0;
		double tempLati=0;
		int segmentID;
		
		for (int i=0;i<this.tt.getData().size();i++){
			segmentID=this.tt.getData().get(i).getSid();
			if (this.st.getData().get(segmentID).getMbr().getTe() < (midTime-radius) 
					|| this.st.getData().get(segmentID).getMbr().getTs() > (midTime+radius)){
				continue;				
			}
			tempTime=this.st.getData().get(segmentID).getData().getStartPoint().getT();
			tempLongi=this.st.getData().get(segmentID).getData().getStartPoint().getX();
			tempLati=this.st.getData().get(segmentID).getData().getStartPoint().getY();
			if( tempTime <= (midTime+radius) && tempTime >= (midTime-radius)){
				trajPoints.add(new QueryResultDataStruc(this.tt.getData().get(i).getTid(), this.st.getData().get(segmentID).getData().getStartPoint()));
			}
			
			for (int j=0;j<this.st.getData().get(segmentID).getData().getDc().size();j++){
				tempTime=tempTime+this.st.getData().get(segmentID).getData().getDc().get(j).getDt()*1000;
				tempLongi=tempLongi+(double)this.st.getData().get(segmentID).getData().getDc().get(j).getDx()/1000000;
				tempLati=tempLati+(double)this.st.getData().get(segmentID).getData().getDc().get(j).getDy()/1000000;
				if(tempTime<=(midTime+radius)&& tempTime>=(midTime-radius)){	
					trajPoints.add(new QueryResultDataStruc(this.tt.getData().get(i).getTid(),new SamplePoint(tempLongi,tempLati,tempTime)));
				}
			}
		}
		return trajPoints;
	}

	@Override
	public ArrayList<QueryResultDataStruc> SpatialWindowQuery(double longitude, double latitude, double radius) {
		// TODO Auto-generated method stub
		ArrayList<QueryResultDataStruc> trajPoints=new ArrayList<QueryResultDataStruc>();
		long tempTime=0;
		double tempLongi=0;
		double tempLati=0;
		int segmentID;
		
		for (int i=0;i<this.tt.getData().size();i++){
			segmentID=this.tt.getData().get(i).getSid();
			if(!IsColliding.isCollidingCircleRectangle(longitude,latitude, radius,
					(this.st.getData().get(segmentID).getMbr().getXhigh()+this.st.getData().get(segmentID).getMbr().getXlow())/2, 
					(this.st.getData().get(segmentID).getMbr().getYhigh()+this.st.getData().get(segmentID).getMbr().getYlow())/2, 
					this.st.getData().get(segmentID).getMbr().getXhigh()-this.st.getData().get(segmentID).getMbr().getXlow(),
					this.st.getData().get(segmentID).getMbr().getYhigh()-this.st.getData().get(segmentID).getMbr().getYlow())){
				continue;
			}
			tempTime=this.st.getData().get(segmentID).getData().getStartPoint().getT();
			tempLongi=this.st.getData().get(segmentID).getData().getStartPoint().getX();
			tempLati=this.st.getData().get(segmentID).getData().getStartPoint().getY();
			if (commonMethods.Distance.getDistance(longitude, latitude, tempLongi, tempLati) <= radius) {
				trajPoints.add(new QueryResultDataStruc(this.tt.getData().get(i).getTid(), this.st.getData().get(segmentID).getData().getStartPoint()));
			}
			
			for (int j = 0; j < this.st.getData().get(segmentID).getData().getDc().size(); j++) {
				tempTime=tempTime+this.st.getData().get(segmentID).getData().getDc().get(j).getDt()*1000;
				tempLongi=tempLongi+(double)this.st.getData().get(segmentID).getData().getDc().get(j).getDx()/1000000;
				tempLati=tempLati+(double)this.st.getData().get(segmentID).getData().getDc().get(j).getDy()/1000000;
				if (commonMethods.Distance.getDistance(longitude, latitude, tempLongi, tempLati)<= radius){
					trajPoints.add(new QueryResultDataStruc(this.tt.getData().get(i).getTid(),new SamplePoint(tempLongi,tempLati,tempTime)));
				}
			}
		}
		return trajPoints;
	}

	@Override
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
