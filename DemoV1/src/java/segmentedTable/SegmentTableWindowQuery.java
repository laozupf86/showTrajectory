package segmentedTable;

import java.util.ArrayList;
import commonMethods.IsColliding;
import dataS.QueryResultDataStruc;
import dataS.SamplePoint;
import advantageOperators.WindowQueryInterface;
import java.util.HashMap;


/**
 * implementation of window query for segment data structure
 * @author Xuefei Li
 *
 */
public class SegmentTableWindowQuery extends SegmentedTable implements WindowQueryInterface{
	
	public SegmentTableWindowQuery(){
		super();
	}

	@Override
	public ArrayList<QueryResultDataStruc> TimeWindowQuery(long midTime, long radius) {
		// TODO Auto-generated method stub
		ArrayList<QueryResultDataStruc> trajPoints=new ArrayList<QueryResultDataStruc>();
		int segmentID;
		
		for (int i=0;i<this.tt.getData().size();i++){
			segmentID=this.tt.getData().get(i).getSid();
			if (this.st.getData().get(segmentID).getMbr().getTe() < (midTime-radius) 
					|| this.st.getData().get(segmentID).getMbr().getTs() > (midTime+radius)){
				continue;				
			}
			for (int j=0;j<this.st.getData().get(segmentID).getData().size();j++){
				if(this.st.getData().get(segmentID).getData().get(j).getT()<=(midTime+radius)
						&& this.st.getData().get(segmentID).getData().get(j).getT()>=(midTime-radius)){
					trajPoints.add(new QueryResultDataStruc(this.tt.getData().get(i).getTid(), 
							new SamplePoint(this.st.getData().get(segmentID).getData().get(j).getX(), 
									this.st.getData().get(segmentID).getData().get(j).getY(), 
									this.st.getData().get(segmentID).getData().get(j).getT())
							));
				}
			}
		}
		return trajPoints;
	}

	@Override
	public ArrayList<QueryResultDataStruc> SpatialWindowQuery(double longitude, double latitude, double radius) {
		// TODO Auto-generated method stub
		ArrayList<QueryResultDataStruc> trajPoints=new ArrayList<QueryResultDataStruc>();
		int segmentID;
		
		for (int i = 0; i<this.tt.getData().size(); i++) {
			segmentID=this.tt.getData().get(i).getSid();
			if(!IsColliding.isCollidingCircleRectangle(longitude,latitude, radius,
					(this.st.getData().get(segmentID).getMbr().getXhigh()+this.st.getData().get(segmentID).getMbr().getXlow())/2, 
					(this.st.getData().get(segmentID).getMbr().getYhigh()+this.st.getData().get(segmentID).getMbr().getYlow())/2, 
					this.st.getData().get(segmentID).getMbr().getXhigh()-this.st.getData().get(segmentID).getMbr().getXlow(),
					this.st.getData().get(segmentID).getMbr().getYhigh()-this.st.getData().get(segmentID).getMbr().getYlow())){
				continue;
			}			
			for (int j = 0; j < this.st.getData().get(segmentID).getData().size(); j++) {
				if (commonMethods.Distance.getDistance(longitude,latitude,this.st.getData().get(segmentID).getData().get(j).getX(),this.st.getData().get(segmentID).getData().get(j).getY())<=radius){
					trajPoints.add(new QueryResultDataStruc(this.tt.getData().get(i).getTid(),
							new SamplePoint(this.st.getData().get(segmentID).getData().get(j).getX(), 
									this.st.getData().get(segmentID).getData().get(j).getY(), 
									this.st.getData().get(segmentID).getData().get(j).getT())));
				}
			}
		}
		return trajPoints;
	}

	@Override
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
