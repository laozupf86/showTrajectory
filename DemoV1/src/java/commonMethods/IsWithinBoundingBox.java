package commonMethods;

/**
 * test if the given points are within a radius of bounding box
 * @author Xuefei Li
 *
 */
public class IsWithinBoundingBox {
	
	/**
	 * 
	 * @param longi
	 * @param lati
	 * @param Qlongi
	 * @param Qlati
	 * @param radius
	 * @return
	 */
	public static boolean isWithinBoundingBox(double longi, double lati, double Qlongi, double Qlati, double radius){
		double rLongi=0, rLati=0;
		rLati=radius/2/111000;
		rLongi=Math.abs(radius/2/(111000*Math.cos(Qlati)));
		if(longi >= (Qlongi-rLongi) && longi <= (Qlongi+rLongi) && lati >= (Qlati-rLati) && lati <= (Qlati+rLati)){
			return true;
		}
		else{
			return false;
		}		
	}

}
