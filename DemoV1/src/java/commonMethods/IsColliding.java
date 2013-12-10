package commonMethods;

/**
 * circle-rectangle-collision-detection
 * @author Xuefei Li
 *
 */
public class IsColliding {
	
	/**
	 * see explanation
	 * http://stackoverflow.com/questions/401847/circle-rectangle-collision-detection-intersection
	 * 
	 * @param circleX
	 * @param circleY
	 * @param radius
	 * @param rectangleX
	 * @param rectangleY
	 * @param rectangleWidth
	 * @param rectangleHeight
	 * @return
	 */
	public static boolean isCollidingCircleRectangle(double circleX, double circleY,
			double radius, double rectangleX, double rectangleY,
			double rectangleWidth, double rectangleHeight) {
		
		double xRadius = Math.abs(commonMethods.Distance.getDistance(
				rectangleX, 0, rectangleX + rectangleWidth / 2, 0));
		double yRadius = Math.abs(commonMethods.Distance.getDistance(
				rectangleY, 0, rectangleY + rectangleHeight / 2, 0));
		double circleDistanceX = Math.abs(commonMethods.Distance.getDistance(
				circleX, 0, rectangleX, 0) - xRadius);
		double circleDistanceY = Math.abs(commonMethods.Distance.getDistance(
				circleY, 0, rectangleY, 0) - yRadius);

		if (circleDistanceX > (xRadius + radius)) {
			return false;
		}
		if (circleDistanceY > (yRadius + radius)) {
			return false;
		}

		if (circleDistanceX <= xRadius) {
			return true;
		}
		if (circleDistanceY <= yRadius) {
			return true;
		}

		double cornerDistance_sq = Math.pow(circleDistanceX - xRadius, 2)
				+ Math.pow(circleDistanceY - yRadius, 2);
		return (cornerDistance_sq <= Math.pow(radius, 2));
	}

}
