package frameTable;

import java.util.Arrays;

import dataS.PFramePoint;

/**
 * An array used to store the Pframe code for each Pframe 
 * @author Haozhou
 *
 */
public class PFrame {
	

	private int[] data;
	private int i = 0;
	private int c = 23000;
	
	/**
	 * initial with default size of array 
	 */
	public PFrame(){
		this.data = new int[this.c];
	}
	
	/**
	 * initial with user defined size of array
	 * @param initalSize the initial size of array
	 */
	public PFrame(int initalSize){
		this.data = new int[initalSize];
		this.c = initalSize;
	}
	
	/**
	 * Add new Pframe point into Pframe and encode it
	 * @param p a Pframe point
	 */
	public void addNewPoint(PFramePoint p){
		
		int coordcode = ((int)p.getX() << 16) | ((int)p.getY() & 0xFFFF);
		
		if (this.i >= data.length - 1){
			this.data = Arrays.copyOf(this.data, this.c + 200);
			this.c = this.c + 200;
				
		}
		
		
		data[this.i] = coordcode;
		this.i++;
		
	}
	
	/**
	 * Remove a Pframe code by its index
	 * @param pid the index of Pframe coder
	 * @return the removed code
	 */
	public int removePointByPID(int pid){
		int code = this.data[pid];
		int numMoved = this.i - pid;
		if (numMoved > 0){
			 System.arraycopy(this.data, pid + 1, this.data, pid, numMoved);
		}
		this.i = this.i - 1;
		
		return code;
			            
		
	}
	
	/**
	 * Get a Pframe code with its index
	 * @param pid the index of Pframe coder
	 * @return the code
	 */
	public int getPointByPID(int pid){
		
		if (pid < this.data.length){
			return this.data[pid];
		}else{
			return 0;
		}	
	}
	

}
