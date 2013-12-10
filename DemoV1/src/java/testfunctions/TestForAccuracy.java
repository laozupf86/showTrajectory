package testfunctions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import accuaryFunction.ErrorDistanceCalculation;


import dataS.SamplePoint;
import frameTable.FrameConvert;

public class TestForAccuracy {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String path = "C:\\Users\\uqhwan21.EAIT\\Documents\\data\\cleaned\\1\\";
		//String path = "C:\\myUQ\\expData\\TC\\anotherData\\";
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
	    long ts = 1235950299000L;
	    //frameTable.FrameTable ft = new frameTable.FrameTable(t.getTime(), 90000, 2);
	    //frameTable.IPFrameTable ipt = new frameTable.IPFrameTable(t.getTime(), 120000, 200, 8);
	    ErrorDistanceCalculation edc = new ErrorDistanceCalculation(ts, 30000);
		
	    
		 while(true){
		    	//String n = "";
		    
		    	try {
		    		//System.out.println("Enter to start read ");
					//n = in.readLine();
					System.out.println("load file into memory..." + "120000");
					
					ArrayList<ArrayList<SamplePoint>> sp = new ArrayList<ArrayList<SamplePoint>>();
					sp = commonMethods.LoadFromFile.getArrayPoints(path, "");
					//bt.importBulkofData(staticFunction.LoadFromFile.getArrayPoints(path, ""));
					
					
					
					System.out.println("Done! Total size is " + sp.size() + " calculating distance.... ");
					
					for (int j = 1; j < 6; j++){
						edc = new ErrorDistanceCalculation(t.getTime(), 30000*j);
						double rate = edc.getSEDerrorDistance(sp);
						System.out.println("error rate is " + rate + " at " + 30000*j);
					}
					
					//n = in.readLine();
					
					
					//double rate = edc.getSEDerrorDistance(sp);
					//System.out.println("error rate is " + rate);
					
					//System.gc();
					System.out.println("done, please check memeory");
					//in.readLine();
					break;
					
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    	
				
				//System.out.println("add finish");
		    	
		    }
		

	}

}
