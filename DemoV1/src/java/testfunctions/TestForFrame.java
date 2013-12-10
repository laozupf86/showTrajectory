package testfunctions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dataS.SamplePoint;

public class TestForFrame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		String path = "C:\\Users\\uqhwan21.EAIT\\Documents\\data\\cleaned\\1\\";
		//String path = "C:\\myUQ\\expData\\TC\\filter\\";
		//String path = "C:\\myUQ\\expData\\TC\\cleaneddata\\";
		//String path = "C:\\Users\\WANG Haozhou\\Documents\\myUQ\\expData\\cleaneddata\\";
		DateFormat time = new SimpleDateFormat("yyyyMMddhhmmss");
		Date t = null;
		try {
			t = time.parse("20120930000000");
			
		} catch (ParseException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    int i = 0;
	    frameTable.FrameTable ft = new frameTable.FrameTable(t.getTime(), 60000, 2);
	    frameTable.IPFrameTable ipt = new frameTable.IPFrameTable(t.getTime(), 60000, 200, 8);
		
		 while(true){
		    	String n = "";
		    
		    	try {
		    		System.out.println("Enter to start read ");
					n = in.readLine();
					System.out.println("load file into memory..." + "60000");
					
					ArrayList<ArrayList<SamplePoint>> sp = new ArrayList<ArrayList<SamplePoint>>();
					sp = commonMethods.LoadFromFile.getArrayPoints(path, "");
					//bt.importBulkofData(staticFunction.LoadFromFile.getArrayPoints(path, ""));
					
					System.out.println("Done! Total size is " + sp.size() + " enter 1 to build table without delta, 2 for delta ");
					
					n = in.readLine();
					
					if (n.equals("1")){
						long st = System.currentTimeMillis();
						ft.importBulkofData(sp);
						long et = System.currentTimeMillis();
						System.out.println("running time is " + (et - st));
						//System.out.println("added distance " + ft.getAddDistance()/sp.size());
						//System.out.println("deled distance " + ft.getDelDistance()/sp.size());
						//System.out.println("mixed distance " + ft.getMixDistance()/sp.size());
						//System.out.println("sed distance " + ft.getSed()/sp.size());
						
						
						//bt.importBulkofData(sp);
					}else{
						System.out.println("compressing...");
						long st = System.currentTimeMillis();
						ipt.importBulkofData(sp);
						//sp = null;
						System.gc();
						long et = System.currentTimeMillis();
						/*System.out.println("added point " + (double)ipt.getNumOfAddPoints()/(double)ipt.getTotalPoint());
						System.out.println("deleted point " + (double)ipt.getNumOfDelPoints()/(double)ipt.getTotalPoint());
						System.out.println("total point " + ipt.getTotalPoint());*/
						System.out.println("running time is " + (et - st));
					
						//dbt.importBulkofData(sp);
					}
					//System.gc();
					System.out.println("done, please check memeory");
					in.readLine();
					
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    	
				
				//System.out.println("add finish");
		    	
		    }
		
		
		
		/*frameTable.FrameTable ft = new frameTable.FrameTable(t.getTime(), 60000, 2);
		ft.importBulkofData(staticFunction.LoadFromFile.getArrayPoints(path, ""));	*/
		

	}

}
