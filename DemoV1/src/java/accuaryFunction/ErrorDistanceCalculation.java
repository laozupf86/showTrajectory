package accuaryFunction;

import java.util.ArrayList;

import dataS.SamplePoint;
import frameTable.FrameConvert;

public class ErrorDistanceCalculation {
	
	private long startTime = 0;
	private int interval = 0;
	
	
	public ErrorDistanceCalculation(long startTime, int interval){
		this.startTime = startTime;
		this.interval = interval;
	}
	
	
	public double getSEDerrorDistance(ArrayList<ArrayList<SamplePoint>> input){
		
		
		FrameConvert fc = new FrameConvert(this.startTime, this.interval);
		ArrayList<ArrayList<SamplePoint>> data = this.checkTrajectory(input);
		double errorRate = 0;
		SEDCalculation sc = new SEDCalculation(this.startTime, this.interval);
		int tid = 0;
		int i = 0;
		double max = 0;
		double distance = 0;
		
		for (ArrayList<SamplePoint> tr : data){
			double error = 0;
			
			if (this.getDistance(tr) != 0){
				error = sc.getSEDDisance(tr, fc.accConvert(tid, tr, 0));
				errorRate = errorRate + error/this.getDistance(tr);
				//staticFunction.LoadFromFile.writeToFile("C:\\myUQ\\expData\\TC\\temp\\", tid + "", fc.accConvert(tid, tr, 0), 0);
			}
			
			if (error < 0.5){
				
			}
			
			//System.out.println(" get error is " + error + " sed is " + sc.getSEDDisance(tr, fc.accConvert(tid, tr, 0)));
			
			if (error/this.getDistance(tr) > 0.5){
				i++;
			}
			if (error > max){
				max = error;
				distance = this.getDistance(tr);
			}
			
			tid++;
		}
		
		if (data.size() == 0){
			return 0;
		}
		//System.out.println("the max error is " + max + " count is " + i + " this distance is " + distance);
		return errorRate/data.size();
		
	}
	
	
	private double getDistance(ArrayList<SamplePoint> input){
		
		double distance = 0;
		
		for (int i = 1; i < input.size(); i++){
			distance = distance + commonMethods.Distance.getDistance(input.get(i - 1).getX(), input.get(i - 1).getY(), 
					input.get(i).getX(), input.get(i).getY());
		}
		
		return distance;
		
	}
	
	
	private ArrayList<ArrayList<SamplePoint>> checkTrajectory(ArrayList<ArrayList<SamplePoint>> input){
		ArrayList<ArrayList<SamplePoint>> fin = new ArrayList<ArrayList<SamplePoint>>();
		
		for (ArrayList<SamplePoint> t : input){
			fin.add(this.checkSingleTrajetcory(t));
		}
		
		return fin;
		
	}
	
	private ArrayList<SamplePoint> checkSingleTrajetcory(ArrayList<SamplePoint> orgi){
		
		ArrayList<SamplePoint> fin = new ArrayList<SamplePoint>();
		
		fin.add(orgi.get(0));
		
		long tempTime = orgi.get(0).getT();
		
		for (int i = 1; i < orgi.size(); i++){
			if (orgi.get(i).getT() > tempTime){
				tempTime = orgi.get(i).getT();
				//System.out.println("time of i is " + orgi.get(i).getT() + " time of i - 1 is " + orgi.get(i - 1).getT());
				fin.add(orgi.get(i));
			}
		}
		
		return fin;
	}
	
	
	

}
