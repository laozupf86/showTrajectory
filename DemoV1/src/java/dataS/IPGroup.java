package dataS;

import java.util.ArrayList;

/**
 * A I/P frame points group, includes a Iframe point and n-1 Pframe points, it uses only for 
 * assign the points to I/P frame structure. 
 * @author uqhwan21
 *
 */


public class IPGroup {
	
	//Iframe point
	private TempFramePoint f;
	//A Pframe points list
	private ArrayList<PFramePoint> p;
	
	public IPGroup(){
		p = new ArrayList<PFramePoint>();
	}

	public TempFramePoint getF() {
		return f;
	}

	public void setF(TempFramePoint f) {
		this.f = f;
	}

	public ArrayList<PFramePoint> getP() {
		return p;
	}
	/**
	 * add a new pframe point into P frame list
	 * @param pf a Pframe point
	 */
	public void addNewPframePoint(PFramePoint pf){
		this.p.add(pf);
	}


}
