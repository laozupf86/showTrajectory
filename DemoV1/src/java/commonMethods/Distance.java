package commonMethods;

import dataS.MBR;

/**
 * The distance calculation methods
 * @author Haozhou
 *
 */
public class Distance {

	/**
	 * @param args
	 */
	
	
	public static final double EARTH_RADIUS = 6378137;
	
	public Distance(){
		
	}
	
	
	public static void main(String[] args) {
		
		
		// TODO Auto-generated method stub
		
		System.out.println("distance is " + getDistance(0,0, 0.005,0));

	}
	
	
	private static double rad(double d){
		
            return d * Math.PI / 180.0;
       
    }
	
	/**
	 * The actual distance
	 * @param lng1
	 * @param lat1
	 * @param lng2
	 * @param lat2
	 * @return distance
	 */
	public static double getDistance(double lng1, double lat1, double lng2, double lat2){
            
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
                s = s * EARTH_RADIUS;
        //s = Math.round(s * 10000) / 10000;
                return s;
	}
	
	/**
	 * The Euclidean distance
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return distance
	 */
	public static double getEuclideanDistance(double x1, double y1, double x2, double y2){
		
		double d = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1-y2), 2));
		
		return d;
		
		
	}
	
	/**
	 * Minimum distance from a point to a line segment
	 * @param x point
	 * @param y point
	 * @param x1 line segment
	 * @param y1 line segment
	 * @param x2 line segment
	 * @param y2 line segment
	 * @return distance
	 */
	public static double calculateLineDistance(double x, double y, double x1, double y1, double x2, double y2){
		
		double b = commonMethods.Distance.getDistance(x1, y1, x, y);
		double c = commonMethods.Distance.getDistance(x2, y2, x, y);
		double a = commonMethods.Distance.getDistance(x1, y1, x2, y2);
		
		if (c + b == a){
			return 0.0;
		}else if (c * c >= a * a + b * b){
			return b;
		}else if (b * b >= a * a + c * c){
			return c;
		}else{
			double p = (a + b + c) / 2;
			double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));
			return 2 * s / c; 
		}
	}
	
	/**
	 * Check whether a point is insider a given MBR or not
	 * @param x point
	 * @param y point
	 * @param mbr MBR
	 * @return ture for not in
	 */
	public static boolean checkArea(double x, double y, MBR mbr){
		
		if (mbr.getXlow() > x){			
			return false;
		}else if (mbr.getXhigh() < x){
			return false;
		}else if (mbr.getYlow() > y){
			return false;
		}else if (mbr.getYhigh() < y){
			return false;
		}else{
			return true;
		}
		
		
	}
	
	/**
	 * Get the MinMaxDistance between a point and a MBR
	 * @param x point
	 * @param y point
	 * @param mbr MBR
	 * @return distance
	 */
	public static double getMinMaxDistance(double x, double y, MBR mbr){
		
		double result = Double.POSITIVE_INFINITY;
		double a = Double.POSITIVE_INFINITY;
		double b = Double.POSITIVE_INFINITY;
		double c = Double.POSITIVE_INFINITY;
		double d = Double.POSITIVE_INFINITY;
		
		a = calculateLineDistance(x, y, mbr.getXlow(), mbr.getYlow(), mbr.getXlow(), mbr.getYhigh());
		b = calculateLineDistance(x, y, mbr.getXlow(), mbr.getYhigh(), mbr.getXhigh(), mbr.getYhigh());
		c = calculateLineDistance(x, y, mbr.getXhigh(), mbr.getYhigh(), mbr.getXhigh(), mbr.getYlow());
		d = calculateLineDistance(x, y, mbr.getXlow(), mbr.getYlow(), mbr.getXhigh(), mbr.getYlow());
		
		
		if (a < result){
			result = a;
		}
		
		if (b < result){
			result = b;
		}
		
		if (c < result){
			result = c;
		}
		
		if (d < result){
			result = d;
		}
		
		return result;	
	}

}
