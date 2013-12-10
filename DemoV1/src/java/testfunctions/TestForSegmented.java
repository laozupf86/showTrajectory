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

public class TestForSegmented {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//String path = "E:\\uqhwan16\\data\\cleaned\\1\\";
		String path = "C:\\Users\\uqhwan21.EAIT\\Documents\\data\\cleaned\\1\\";
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
		segmentedTable.SegmentedTable st = new segmentedTable.SegmentedTable();
		//st.importBulkofData(staticFunction.LoadFromFile.getArrayPoints(path, ""));
		deltaSegmentedTable.SegmentedTable dst = new deltaSegmentedTable.SegmentedTable();
		//dst.importBulkofData(staticFunction.LoadFromFile.getArrayPoints(path, ""));
		
		
		 while(true){
		    	String n = "";
		    
		    	try {
		    		System.out.println("Enter to start read ");
					n = in.readLine();
					System.out.println("load file into memory...");
					
					ArrayList<ArrayList<SamplePoint>> sp = new ArrayList<ArrayList<SamplePoint>>();
					sp = commonMethods.LoadFromFile.getArrayPoints(path, "");
					//bt.importBulkofData(staticFunction.LoadFromFile.getArrayPoints(path, ""));
					
					System.out.println("Done! enter 1 to build table without delta, 2 for delta ");
					
					n = in.readLine();
					
					if (n.equals("1")){
						st =new segmentedTable.SegmentedTable();
						
						for (String s : commonMethods.LoadFromFile.getIDs(path, "")){
							st.InsertSingleRecord(i, commonMethods.LoadFromFile.getPoints(path, s, ""));
							
							i++;
							if (i % 10000 == 0){
								System.out.println("read at " + s + " count is " + i);
								//System.gc();
							}
							
						}
						
						//bt.importBulkofData(sp);
					}else{
						System.out.println("compressing...");
						dst = new deltaSegmentedTable.SegmentedTable();
						/*for (String s : staticFunction.LoadFromFile.getIDs(path, "")){
							dst.InsertSingleRecord(i, staticFunction.LoadFromFile.getPoints(path, s, ""));
							
							i++;
							if (i % 10000 == 0){
								System.out.println("read at " + s + " count is " + i);
								System.gc();
							}
							
						}*/
						
						
					
						dst.importBulkofData(sp);
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
			
			
		
		
		
		/*segmentedTable.SegmentedTable st = new segmentedTable.SegmentedTable();
		//st.importBulkofData(staticFunction.LoadFromFile.getArrayPoints(path, ""));
		deltaSegmentedTable.SegmentedTable dst = new deltaSegmentedTable.SegmentedTable();
		//dst.importBulkofData(staticFunction.LoadFromFile.getArrayPoints(path, ""));
		int i = 0;
		for (String s : staticFunction.LoadFromFile.getIDs(path, "")){
			dst.InsertSingleRecord(i, staticFunction.LoadFromFile.getPoints(path, s, ""));
			
			i++;
			if (i % 10000 == 0){
				System.out.println("read at " + s + " count is " + i);
				//System.gc();
			}
			
			
		}
*/
	}


}
