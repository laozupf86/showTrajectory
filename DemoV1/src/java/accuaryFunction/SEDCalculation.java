package accuaryFunction;

import java.util.ArrayList;

import commonMethods.TimeOperation;


import dataS.SamplePoint;
import dataS.TempFramePoint;

public class SEDCalculation {
	
	
	private long startTime;
	private int interval;
	
	public SEDCalculation(long startTime, int interval){
		this.startTime = startTime;
		this.interval = interval;
	}
	
	
	public double getSEDDisance(ArrayList<SamplePoint> orgi, ArrayList<TempFramePoint> frames){
		
		int j = 0;
		
		double totalSEDdistance = 0;
		
		
		for (int i = 0; i < orgi.size();){
			
			int timeSlot = TimeOperation.getSlot(this.startTime, this.interval, orgi.get(i).getT());
			if (frames.get(j).getTimeslot() == timeSlot){
				if (j + 1 < frames.size()){
					totalSEDdistance = totalSEDdistance + this.getSEDDirect(frames.get(j).getX(), frames.get(j).getY(), 
							frames.get(j + 1).getX(), frames.get(j + 1).getY(), orgi.get(i).getX(), orgi.get(i).getY(),
							frames.get(j).getT(), 
							frames.get(j + 1).getT(), orgi.get(i).getT());					
					
				}else{
					totalSEDdistance = totalSEDdistance + this.getSEDDirect(frames.get(j).getX(), frames.get(j).getY(), 
							orgi.get(orgi.size() - 1).getX(), orgi.get(orgi.size() - 1).getY(), orgi.get(i).getX(), orgi.get(i).getY(),
							frames.get(j).getT(), orgi.get(orgi.size() - 1).getT(), orgi.get(i).getT());
				}
				i++;
			}else if(frames.get(j).getTimeslot() < timeSlot){
				j++;
				if (j + 1 == frames.size()){
					//System.out.println("unexcpect size of frames");
				}
			}else{
				System.out.println("unexcpect time slot, current frame is " + 
						frames.get(j - 1).getT() + " original is " + orgi.get(i - 1).getT());
				i++;
			}
			
		}
		
		
		return totalSEDdistance;
	}
	
	
	private double getSEDDirect(double xs, double ys, double xe, double ye, double xi, double yi, long xt, long yt, long it){
		double dti = (it - xt)/1000;
		double dt = (yt - xt)/1000;
		if (dt <= 0){
			
			return 0;
		}
		
		double d = dti/dt;
		//System.out.println("time can not be less than 0, yt is " + yt + " xt is " + xt + " dti is " + dti);
		
		double dx = xs + d*(xe - xs);
		double dy = ys + d*(ye - ys);
		
		//System.out.println(".........  " + xi + "," +  yi +  "," +  dx  + "," +  dy + " distance is " + staticFunction.Distance.getDistance(xi, yi, dx, dy) + " dt is " + dt);
		//System.out.println(dx  + "," +  dy);
		//System.out.println("distance is " + staticFunction.Distance.getEuclideanDistance(xi, yi, dx, dy));
		//System.out.println("......... ******  " + staticFunction.Distance.getDistance(xi, yi, dx, dy));
		//System.out.println(".........   " + staticFunction.Distance.getDistance(xs, ys, xi, yi));
		return commonMethods.Distance.getDistance(xi, yi, dx, dy);
	}
	
	
	
	/*private double getSED(TempFramePoint standard, ArrayList<TempFramePoint> delPoints, TempFramePoint nextPoint){
		
		long dt = nextPoint.getT() - standard.getT();
		double sumDistance = 0;;
		
		
		if (dt == 0){
			System.out.println("current time is " + standard.getT() + " next time is " + nextPoint.getT());
			return 0;
		}
		
		if (nextPoint.getTimeslot() - standard.getTimeslot() != 1){
			System.out.println("current slot is " + standard.getTimeslot() + " next slot is " + nextPoint.getTimeslot());
			return 0;
		}
		
		for (TempFramePoint p : delPoints){
			long dti = p.getT() - standard.getT();
			double dx = standard.getX() + ((double)dti/(double)dt)*(nextPoint.getX() - standard.getX());
			double dy = standard.getY() + ((double)dti/(double)dt)*(nextPoint.getY() - standard.getY());
			if (p.getTimeslot() != standard.getTimeslot()){
				System.out.println("not match! current slot is " + standard.getTimeslot() + " next slot is " + p.getTimeslot());
			}else{
				sumDistance = sumDistance + staticFunction.Distance.getDistance(standard.getX(), standard.getY(), dx, dy);
			}
			
			
			//System.out.println("standard x is " + standard.getX() + " standard y is " + standard.getY() + 
			//		" dx  is " + (nextPoint.getX() - standard.getX()) + " dy is " + (nextPoint.getY() - standard.getY())+ " dt " + dti/dt);
		}
		
		//System.out.println("sum is " + sumDistance);
		
		return sumDistance;
	}
	*/

}
