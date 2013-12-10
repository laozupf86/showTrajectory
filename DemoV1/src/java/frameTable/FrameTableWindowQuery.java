package frameTable;

import java.util.ArrayList;
import commonMethods.IsWithinBoundingBox;
import commonMethods.TimeOperation;
import dataS.QueryResultDataStruc;
import dataS.SamplePoint;
import advantageOperators.WindowQueryInterface;
import java.util.HashMap;

/**
 * implementation of window query for I frame only data structure
 * @author Xuefei Li
 *
 */
public class FrameTableWindowQuery extends FrameTable {

	public FrameTableWindowQuery(long st, int intervalTime, int max) {
		super(st, intervalTime, max);
		// TODO Auto-generated constructor stub
	}

	
	public ArrayList<QueryResultDataStruc> TimeWindowQuery(long midTime, long radius) {
		// TODO Auto-generated method stub
		ArrayList<QueryResultDataStruc> trajPoints=new ArrayList<QueryResultDataStruc>();
		int start=TimeOperation.getSlot(this.getStandardTime(),this.getInterval(), midTime-radius);
		int end=TimeOperation.getSlot(this.getStandardTime(),this.getInterval(), midTime+radius);
		
		for (int i = Math.max(start,0); i <Math.min(end, this.fl.frames.keySet().size()); i++) {
			if (this.fl.getFrameByTimeSlot(i) == null){
				continue;
			}
			for (int j = 0; j < this.fl.getFrameByTimeSlot(i).getPoints().size(); j++){
				trajPoints.add(new QueryResultDataStruc(this.fl.getFrameByTimeSlot(i).getPoints().get(j).getTid(),
						new SamplePoint(this.fl.getFrameByTimeSlot(i).getPoints().get(j).getX(), this.fl.getFrameByTimeSlot(i).getPoints().get(j).getY(),TimeOperation.getTimeBySlot(this.getStandardTime(),this.getInterval(),i))));
			}
		}
		return trajPoints;
	}

	
	public ArrayList<QueryResultDataStruc> SpatialWindowQuery(double longitude, double latitude, double radius) {
		// TODO Auto-generated method stub
		ArrayList<QueryResultDataStruc> trajPoints=new ArrayList<QueryResultDataStruc>();
		for (int i = 0; i < this.fl.frames.keySet().size(); i++) {
			if (this.fl.getFrameByTimeSlot(i) == null){
				continue;
			}
			for (int j = 0; j < this.fl.getFrameByTimeSlot(i).getPoints().size(); j++) {
				if(IsWithinBoundingBox.isWithinBoundingBox(this.fl.getFrameByTimeSlot(i).getPoints().get(j).getX(), this.fl.getFrameByTimeSlot(i).getPoints().get(j).getY(), longitude, latitude, radius)){
					if (commonMethods.Distance.getDistance(longitude,latitude,this.fl.getFrameByTimeSlot(i).getPoints().get(j).getX(), this.fl.getFrameByTimeSlot(i).getPoints().get(j).getY())<=radius){
						trajPoints.add(new QueryResultDataStruc(this.fl.getFrameByTimeSlot(i).getPoints().get(j).getTid(),
								new SamplePoint(this.fl.getFrameByTimeSlot(i).getPoints().get(j).getX(), this.fl.getFrameByTimeSlot(i).getPoints().get(j).getY(),TimeOperation.getTimeBySlot(this.getStandardTime(),this.getInterval(),i))));
					}
					}
				}
			}
		return trajPoints;
	}

	
	public ArrayList<QueryResultDataStruc> SpatialTemporalWindowQuery(long midTime, long tRadius, double longitude, double latitude, double sRadius) {
		// TODO Auto-generated method stub
		ArrayList<QueryResultDataStruc> trajPoints=new ArrayList<QueryResultDataStruc>();
		int start=TimeOperation.getSlot(this.getStandardTime(),this.getInterval(), midTime-tRadius);
		int end=TimeOperation.getSlot(this.getStandardTime(),this.getInterval(), midTime+tRadius);
		
		for (int i = Math.max(start,0); i <Math.min(end, this.fl.frames.keySet().size()); i++) {
			if (this.fl.getFrameByTimeSlot(i) == null){
				continue;
			}
			for (int j = 0; j < this.fl.getFrameByTimeSlot(i).getPoints().size(); j++){
				if(IsWithinBoundingBox.isWithinBoundingBox(this.fl.getFrameByTimeSlot(i).getPoints().get(j).getX(), this.fl.getFrameByTimeSlot(i).getPoints().get(j).getY(), longitude, latitude, sRadius)){
					if (commonMethods.Distance.getDistance(longitude,latitude,this.fl.getFrameByTimeSlot(i).getPoints().get(j).getX(), this.fl.getFrameByTimeSlot(i).getPoints().get(j).getY()) <= sRadius){
						trajPoints.add(new QueryResultDataStruc(this.fl.getFrameByTimeSlot(i).getPoints().get(j).getTid(),
								new SamplePoint(this.fl.getFrameByTimeSlot(i).getPoints().get(j).getX(), this.fl.getFrameByTimeSlot(i).getPoints().get(j).getY(),TimeOperation.getTimeBySlot(this.getStandardTime(),this.getInterval(),i))));
					}
				}
			}
		}
		return trajPoints;
	}

    

}
