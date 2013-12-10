package parallelFrameTable;

/**
 * A row of auxiliary table without tid. <tid, startframe, endframe, jumperframes, ref> 
 * @author uqhwan21
 *
 */

public class AuxTableRow{
	
	private int tid;
	private int startFrame;
	private int endFrame;
	private int jumpedFrames;
	private int orginalRef;
	
	/**
	 * 
	 * The tid already store in the AuxTable itself, no needs to store here.
	 * @param startFrame the start number of time slot of this trajectory
	 * @param endFrame the end number of time slot of this trajectory
	 * @param jumpedFrames the jumped encodes of this trajectory
	 * @param orginalRef the reference point to its original file
	 */
	
	public AuxTableRow(int tid, int startFrame, int endFrame, int jumpedFrames, int orginalRef){
		
		this.tid = tid;
		this.startFrame = startFrame;
		this.endFrame = endFrame;
		this.jumpedFrames = jumpedFrames;
		this.orginalRef = orginalRef;
	}


	


	public int getTid() {
		return tid;
	}


	public int getJumpedFrames() {
		return jumpedFrames;
	}


	public int getStartFrame() {
		return startFrame;
	}


	public void setStartFrame(int startFrame) {
		this.startFrame = startFrame;
	}


	public int getEndFrame() {
		return endFrame;
	}


	public void setEndFrame(int endFrame) {
		this.endFrame = endFrame;
	}


	public int getOrginalRef() {
		return orginalRef;
	}


	public void setOrginalRef(int orginalRef) {
		this.orginalRef = orginalRef;
	}

}
