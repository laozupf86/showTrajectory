package testfunctions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import basicTable.BasicTable;

import dataS.SamplePoint;

public class TestSelectionForSegment {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//String path = "C:\\Users\\uqhwan21.EAIT\\Documents\\data\\cleaned\\1\\";
		//String path = "C:\\myUQ\\expData\\TC\\anotherData\\";
		//String path = "C:\\myUQ\\expData\\TC\\cleaneddata\\";
		//String path = "C:\\Users\\WANG Haozhou\\Documents\\myUQ\\expData\\cleaneddata\\";
		String path = "C:\\Users\\Haozhou\\Documents\\MyUQ\\dataset\\anotherData\\";
		//String path = "C:\\myUQ\\expData\\TC\\temp\\";
		//String path = "C:\\Users\\uqhwan21.EAIT\\Documents\\data\\anotherData\\";
		
		DateFormat time = new SimpleDateFormat("yyyyMMddhhmmss");
		Date t = null;
		try {
			t = time.parse("20120930000000");
			
		} catch (ParseException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	   // int i = 0;
	    long ts = 1235729631000L;
	    //frameTable.FrameTable ft = new frameTable.FrameTable(t.getTime(), 90000, 2);
	    //frameTable.IPFrameTable ipt = new frameTable.IPFrameTable(t.getTime(), 120000, 200, 8);
	    //ErrorDistanceCalculation edc = new ErrorDistanceCalculation(ts, 30000);
		
	    
		 while(true){
		    	//String n = "";
		    
		    	try {
		    		//System.out.println("Enter to start read ");
					//n = in.readLine();
					System.out.println("load file into memory..." + "120000");
					
					ArrayList<ArrayList<SamplePoint>> sp = new ArrayList<ArrayList<SamplePoint>>();
					sp = commonMethods.LoadFromFile.getArrayPoints(path, "");
					//bt.importBulkofData(staticFunction.LoadFromFile.getArrayPoints(path, ""));
					
					
					frameTable.FrameTable ft; 
					frameTable.IPFrameTable ipt; 
					
					basicTable.BasicTable bt = new BasicTable();
					deltaBasicTable.BasicTable dbt = new deltaBasicTable.BasicTable();
				
					segmentedTable.SegmentedTable st = new segmentedTable.SegmentedTable();
					deltaSegmentedTable.SegmentedTable dst = new deltaSegmentedTable.SegmentedTable();
					
					
					parallelFrameTable.IPFrameTable pipt;
					
					System.out.println("Done! Total size is " + sp.size() + " importing to table.... ");
					
					long sTime, eTime;
					
					
					
					//ipt = new frameTable.IPFrameTable(ts, 90000, 200, 4);
					//ft = new frameTable.FrameTable(ts, 90000, 2);
					
					//ft.importBulkofData(sp);
					// ipt.importBulkofData(sp);
					//System.out.println("frame table import compelet, start test");
					
					for (int k = 1; k <=4; k++){

						//ipt = new frameTable.IPFrameTable(ts, 30000, 2, 4*k);
						//dst = new deltaSegmentedTable.SegmentedTable();;
						pipt = new parallelFrameTable.IPFrameTable(ts, 30000, 200, 4*k);
						
						
						pipt.importBulkofData(sp);
						System.out.println("frame table import compelet, start test k = " + 4*k);
						System.gc();
						sTime = System.currentTimeMillis();
						for(int i = 0; i < 1000; i++){

							//ipt.selectOneRecordByTID(i*50);
							//ipt.deleteOneRecordByTID(i);

							pipt.selectOneRecordByTID(i*50);
							//dbt.deleteOneRecordByTID(i*70);

						}
						
						eTime = System.currentTimeMillis();
						System.out.println("frame table:  " + (eTime - sTime));
						pipt = null;
						System.gc();
					}
					
					
					
					
					break;
					
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    	
				break;
				//System.out.println("add finish");
		    	
		    }
		
		
		
				

	}

}
