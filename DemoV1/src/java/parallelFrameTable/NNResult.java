package parallelFrameTable;

/**
 * A structure to store the result of NN search
 * @author Haozhou
 *
 */
public class NNResult {
	
	private double bestDistanceSoFar;
	private int bestSoFarTid;
	
	/**
	 * 
	 * @param bestDistanceSoFar current best value
	 * @param bestSoFarTid current best trajectory's id
	 */
	public NNResult(double bestDistanceSoFar, int bestSoFarTid){
		this.bestDistanceSoFar = bestDistanceSoFar;
		this.bestSoFarTid = bestSoFarTid;
	}

	public double getBestDistanceSoFar() {
		return this.bestDistanceSoFar;
	}

	public int getBestSoFarTid() {
		return this.bestSoFarTid;
	}
	
	
	

}
