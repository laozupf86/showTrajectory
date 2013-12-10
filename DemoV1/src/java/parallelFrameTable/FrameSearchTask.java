package parallelFrameTable;

import java.util.concurrent.RecursiveTask;

import dataS.IFramePoint;
import dataS.SamplePoint;


public class FrameSearchTask extends RecursiveTask<Integer>{

	
	
	private long standardTime;
	private int interval;
	private DualFrameList dfl;
	private AuxTable at;
	private int n;
	private int sf;
	private int ef;
	private Trajectory tr;
	private int tid;
	private int index;
	
	public FrameSearchTask(DualFrameList dfl, int interval, long standardTime, int n, int startFrame, int endFrame,
			Trajectory tr, int tid, int index){
		super();
		this.dfl = dfl;
		this.interval = interval;
		this.n = n;
		this.standardTime = standardTime;
		this.sf = startFrame;
		this.ef = endFrame;
		this.tr = tr;
		this.tid = tid;
		this.index = index;
	
		//System.out.println("start is " + sf + " end is " + ef);
		
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7503994832244409793L;

	@Override
	protected Integer compute() {
		// TODO Auto-generated method stub
		
		//Trajectory tr = new Trajectory(tid);
		
		//int currentFrame = startFrame;
		
		
		
		for (int i = this.sf; i < this.ef; i++){
			int c = 0;
			
			for (IFramePoint fp : dfl.getIFrameByFID(i).getPoints()){
				
				if(fp.getTid() == tid){
					
					
					
					int currentTimeSlot = (i - 1)*this.n;
					int pSlot = (i - 1)*(this.n - 1);
					
					tr.setPoint(index, new SamplePoint(fp.getX(), fp.getY(), 
							commonMethods.TimeOperation.getTimeBySlot(this.standardTime, this.interval ,currentTimeSlot)));
					
					index++;
					
					//System.out.println("index is " + index);
					
					for (int j = 1; j < this.n; j++){
						//long code = dfl.getPFrameByPID(pSlot + j).getPointByPID(c);
						int shiftingCode = dfl.getPFrameByPID(pSlot + j).getPointByPID(c);
						short dx = (short) (shiftingCode >> 16);
						short dy = (short) (shiftingCode & 0xFFFF);
						
						
						
						if (dx == Short.MAX_VALUE && dy == Short.MIN_VALUE){
							continue;
						}
						
						//System.out.println("dx is " + dx + " dy is " + dy + " slot is " + (pSlot + j));
						//System.out.println("index is " + index);
						double x = fp.getX() + ((double)(dx)/1000000);
						double y = fp.getY() + ((double)(dy)/1000000);
						
						tr.setPoint(index, new SamplePoint(x, y, 
								commonMethods.TimeOperation.getTimeBySlot(this.standardTime, this.interval ,currentTimeSlot + j)));
						index++;
						
					}
					
					break;
					
				}
				c++;
				//index = index + this.n;
				
				
			}
			
				//tr.addSamplePoint(new SamplePoint(dfl.getIFrameByFID(i).g;
			
		}
		return 0;
		
	}

}
