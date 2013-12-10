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

public class BasicOpeationTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
				String path = "C:\\Users\\uqhwan21.EAIT\\Documents\\data\\cleaned\\1\\";
				//String path = "C:\\myUQ\\expData\\TC\\anotherData\\";
				//String path = "C:\\myUQ\\expData\\TC\\cleaneddata\\";
				//String path = "C:\\Users\\WANG Haozhou\\Documents\\myUQ\\expData\\cleaneddata\\";
				//String path = "C:\\Users\\Haozhou\\Documents\\MyUQ\\dataset\\anotherData\\";
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
							
							
							frameTable.FrameTable ft = new frameTable.FrameTable(ts, 30000, 2);
							frameTable.IPFrameTable ipt = new frameTable.IPFrameTable(ts, 30000, 200, 4);
							
							
							
							basicTable.BasicTable bt = new BasicTable();
							deltaBasicTable.BasicTable dbt = new deltaBasicTable.BasicTable();
						
							segmentedTable.SegmentedTable st = new segmentedTable.SegmentedTable();
							deltaSegmentedTable.SegmentedTable dst = new deltaSegmentedTable.SegmentedTable();
							
							System.out.println("Done! Total size is " + sp.size() + " importing to table.... ");
							
							parallelFrameTable.IPFrameTable pipt = new parallelFrameTable.IPFrameTable(ts, 30000, 200, 8);
							pipt.importBulkofData(sp);
							//ipt.importBulkofData(sp);
							
							ArrayList<SamplePoint> r = new ArrayList<SamplePoint>();
							
							//SamplePoint[] sps = pipt.selectOneRecordByTID(0).getTrajectory();
							long sTime, eTime;
							
							sTime = System.currentTimeMillis();
							for(int i = 0; i < 10000; i++){
								pipt.selectOneRecordByTID(i*4);
							}
							
							eTime = System.currentTimeMillis();
							
							System.out.println("frame table with parl: select: " + (eTime - sTime));
							
							
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
