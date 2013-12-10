package commonMethods;

/**
 * Convert the time to its time slot id or convert back
 * @author Haozhou
 *
 */
public class TimeOperation {
	
	
	public static int getSlot(long startTime, int interval, long time){
		long r = (time - startTime)/interval;
		return (int) r ;
	}
	
	public static long getTimeBySlot(long startTime, int interval, int slot){
		return (slot  * interval) + startTime;
	}

}
