package frameTable;

import java.util.ArrayList;
import java.util.HashMap;

import commonMethods.IsWithinBoundingBox;
import commonMethods.TimeOperation;
import dataS.IFramePoint;
import dataS.QueryResultDataStruc;
import dataS.SamplePoint;

import advantageOperators.WindowQueryInterface;

public class IPFrameTableWindowQuery extends IPFrameTable{

	public IPFrameTableWindowQuery(long st, int intervalTime, int max, int n) {
		super(st, intervalTime, max, n);
		// TODO Auto-generated constructor stub
	}

	
	public ArrayList<QueryResultDataStruc> TimeWindowQuery(long midTime, long radius) {
		// TODO Auto-generated method stub
		ArrayList<QueryResultDataStruc> trajPoints=new ArrayList<QueryResultDataStruc>();
		HashMap<Integer, IFramePoint> iframehash = new HashMap <Integer, IFramePoint>();
		double preLong=0, preLati=0;
		int start=TimeOperation.getSlot(this.getStandardTime(),this.getInterval(), midTime-radius);
		int end=TimeOperation.getSlot(this.getStandardTime(),this.getInterval(), midTime+radius);
		
		for (int i = Math.max(start/this.n,0); i <Math.min(end/this.n, this.dfl.getIframeTimeSlotSet().size()); i++) {
			if (this.dfl.getIFrameByFID(i) == null){
				continue;
			}
			if(i==Math.max(start/this.n,0) && i*this.n!=Math.max(start/this.n,0)){		
				for (int j = 0; j < this.dfl.getIFrameByFID(i).getPoints().size(); j++){
					iframehash.put(this.dfl.getIFrameByFID(i).getPoints().get(j).getTid(), this.dfl.getIFrameByFID(i).getPoints().get(j));
				}
			}
			else{
				for (int j = 0; j < this.dfl.getIFrameByFID(i).getPoints().size(); j++){
					iframehash.put(this.dfl.getIFrameByFID(i).getPoints().get(j).getTid(), this.dfl.getIFrameByFID(i).getPoints().get(j));
					trajPoints.add(new QueryResultDataStruc(this.dfl.getIFrameByFID(i).getPoints().get(j).getTid(),
							new SamplePoint(this.dfl.getIFrameByFID(i).getPoints().get(j).getX(), this.dfl.getIFrameByFID(i).getPoints().get(j).getY(),TimeOperation.getTimeBySlot(this.getStandardTime(),this.getInterval(),i))));
				}
			}
			for (int k = i * (this.n-1); k < Math.min((i + 1) * (this.n-1),this.dfl.getNumOfPfames()); k++) {
				for (int m = 0; m < this.dfl.getIFrameByFID(i).getPoints().size(); m++) {
					int pid = this.dfl.getIFrameByFID(i).getPoints().get(m).getTid();
					int shiftingCode=this.dfl.getPFrameByPID(k).getPointByPID(m);
					short dx = (short) (shiftingCode >> 16);
					short dy = (short) (shiftingCode & 0xFFFF);	
					preLong = iframehash.get(pid).getX()+ ((double)dx / 1000000);
					preLati = iframehash.get(pid).getY()+ ((double)dy / 1000000);						
					IFramePoint temp = new IFramePoint(iframehash.get(pid).getX(), iframehash.get(pid).getY(),iframehash.get(pid).getTid());
					temp.setX(preLong);
					temp.setY(preLati);
					iframehash.put(pid, temp);
					trajPoints.add(new QueryResultDataStruc(pid,new SamplePoint(preLong,preLati,TimeOperation.getTimeBySlot(this.getStandardTime(),this.getInterval(),i+k)))); 
				}
			}
		}
		return trajPoints;
	}

	
	public ArrayList<QueryResultDataStruc> SpatialWindowQuery(double longitude, double latitude, double radius) {
		// TODO Auto-generated method stub
		ArrayList<QueryResultDataStruc> trajPoints=new ArrayList<QueryResultDataStruc>();
		HashMap<Integer, IFramePoint> iframehash = new HashMap <Integer, IFramePoint>();
		double preLong=0, preLati=0;
		for (Integer i : this.dfl.getIframeTimeSlotSet()) {
			if (this.dfl.getIFrameByFID(i) == null){
				continue;
			}
			for (int j = 0; j < this.dfl.getIFrameByFID(i).getPoints().size(); j++) {
				preLong=this.dfl.getIFrameByFID(i).getPoints().get(j).getX();
				preLati=this.dfl.getIFrameByFID(i).getPoints().get(j).getY();
				if(IsWithinBoundingBox.isWithinBoundingBox(preLong, preLati, longitude, latitude, radius)){
					if (commonMethods.Distance.getDistance(longitude,latitude,preLong,preLati)<=radius){					
						trajPoints.add(new QueryResultDataStruc(this.dfl.getIFrameByFID(i).getPoints().get(j).getTid(),new SamplePoint(preLong,preLati,TimeOperation.getTimeBySlot(this.getStandardTime(),this.getInterval(),i*this.n))));						
					}
				}
				if(IsWithinBoundingBox.isWithinBoundingBox(preLong, preLati, longitude, latitude, radius+800*4)){ // suppose a car moves 800m/min 
					iframehash.put(this.dfl.getIFrameByFID(i).getPoints().get(j).getTid(), this.dfl.getIFrameByFID(i).getPoints().get(j));
				}
				
			}
			for (int k = i * (this.n-1); k < Math.min((i + 1) * (this.n-1),this.dfl.getNumOfPfames()); k++) {
				for (int m = 0; m < this.dfl.getIFrameByFID(i).getPoints().size(); m++) {
					int pid = this.dfl.getIFrameByFID(i).getPoints().get(m).getTid();
					if(iframehash.containsKey(pid)){
						int shiftingCode=this.dfl.getPFrameByPID(k).getPointByPID(m);
						short dx = (short) (shiftingCode >> 16);
						short dy = (short) (shiftingCode & 0xFFFF);	
					
						preLong = iframehash.get(pid).getX()+ ((double)dx / 1000000);
						preLati = iframehash.get(pid).getY()+ ((double)dy / 1000000);						
						IFramePoint temp = new IFramePoint(iframehash.get(pid).getX(), iframehash.get(pid).getY(),iframehash.get(pid).getTid());
						temp.setX(preLong);
						temp.setY(preLati);
						iframehash.put(pid, temp);
						if(IsWithinBoundingBox.isWithinBoundingBox(preLong, preLati, longitude, latitude, radius)){	
							if (commonMethods.Distance.getDistance(longitude, latitude, preLong,preLati) <= radius){
								trajPoints.add(new QueryResultDataStruc(pid,new SamplePoint(preLong,preLati,TimeOperation.getTimeBySlot(this.getStandardTime(),this.getInterval(),i+k)))); 
							}
						}
					}
				}
			}
		}
		return trajPoints;
	}

	
	public ArrayList<QueryResultDataStruc> SpatialTemporalWindowQuery(long midTime, long tRadius, double longitude, double latitude, double sRadius) {
		// TODO Auto-generated method stub
		ArrayList<QueryResultDataStruc> trajPoints=new ArrayList<QueryResultDataStruc>();
		HashMap<Integer, IFramePoint> iframehash = new HashMap <Integer, IFramePoint>();
		int start=TimeOperation.getSlot(this.getStandardTime(),this.getInterval(), midTime-tRadius)/this.n;
		int end=TimeOperation.getSlot(this.getStandardTime(),this.getInterval(), midTime+tRadius)/this.n;
		double preLong = 0, preLati = 0;

		for (int i = Math.max(start,0); i <Math.min(end, this.dfl.getIframeTimeSlotSet().size()); i++) {
			if (this.dfl.getIFrameByFID(i) == null){
				continue;
			}
			for (int j = 0; j < this.dfl.getIFrameByFID(i).getPoints().size(); j++){
				preLong=this.dfl.getIFrameByFID(i).getPoints().get(j).getX();
				preLati=this.dfl.getIFrameByFID(i).getPoints().get(j).getY();
				if(IsWithinBoundingBox.isWithinBoundingBox(preLong, preLati, longitude, latitude, sRadius)){
					if (commonMethods.Distance.getDistance(longitude,latitude,preLong,preLati)<=sRadius){					
						trajPoints.add(new QueryResultDataStruc(this.dfl.getIFrameByFID(i).getPoints().get(j).getTid(),new SamplePoint(preLong,preLati,TimeOperation.getTimeBySlot(this.getStandardTime(),this.getInterval(),i*this.n))));						
					}
				}
				if(IsWithinBoundingBox.isWithinBoundingBox(preLong, preLati, longitude, latitude, sRadius+800*4)){ // suppose a car moves 800m/min 
					iframehash.put(this.dfl.getIFrameByFID(i).getPoints().get(j).getTid(), this.dfl.getIFrameByFID(i).getPoints().get(j));
				}
				
			}
			for (int k = i * (this.n-1); k < Math.min((i + 1) * (this.n-1),this.dfl.getNumOfPfames()); k++) {
				for (int m = 0; m < this.dfl.getIFrameByFID(i).getPoints().size(); m++) {
					int pid = this.dfl.getIFrameByFID(i).getPoints().get(m).getTid();
					if(iframehash.containsKey(pid)){
						int shiftingCode=this.dfl.getPFrameByPID(k).getPointByPID(m);
						short dx = (short) (shiftingCode >> 16);
						short dy = (short) (shiftingCode & 0xFFFF);						
						preLong = iframehash.get(pid).getX()+ ((double)dx / 1000000);
						preLati = iframehash.get(pid).getY()+ ((double)dy / 1000000);						
						IFramePoint temp = new IFramePoint(iframehash.get(pid).getX(), iframehash.get(pid).getY(),iframehash.get(pid).getTid());
						temp.setX(preLong);
						temp.setY(preLati);
						iframehash.put(pid, temp);
						if(IsWithinBoundingBox.isWithinBoundingBox(preLong, preLati, longitude, latitude, sRadius)){	
							if (commonMethods.Distance.getDistance(longitude, latitude, preLong,preLati) <= sRadius){
								trajPoints.add(new QueryResultDataStruc(pid,new SamplePoint(preLong,preLati,TimeOperation.getTimeBySlot(this.getStandardTime(),this.getInterval(),i+k)))); 
							}
						}
					}
				}
			}
		}
		return trajPoints;
	}

}
