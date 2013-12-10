package testfunctions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import segmentedTable.SegmentTableWindowQuery;
import basicTable.BasicTableWindowQuery;

import dataS.SamplePoint;
import deltaBasicTable.BasicDeltaWindowQuery;
import deltaSegmentedTable.SegmentDeltaWindowQuery;

public class WindowTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String path = "C:\\Users\\uqhwan21.EAIT\\Documents\\data\\cleaned\\1\\";
		//String path = "C:\\myUQ\\expData\\TC\\anotherData\\";
		//String path = "C:\\myUQ\\expData\\TC\\cleaneddata\\";
		//String path = "C:\\Users\\WANG Haozhou\\Documents\\myUQ\\expData\\cleaneddata\\";
		//String path = "C:\\Users\\Haozhou\\Documents\\MyUQ\\dataset\\anotherData\\";
		//String path = "C:\\myUQ\\expData\\TC\\temp1\\";
		//String path = "C:\\Users\\uqhwan21.EAIT\\Documents\\data\\anotherData\\";
		String path = "E:\\Data\\hwang\\cleaned\\1\\";
		DateFormat time = new SimpleDateFormat("yyyyMMddhhmmss");
		Date t = null;
		Date qs = null;
		Date qe = null;
		try {
			t = time.parse("20120930000000");
			qs = time.parse("2012093010000");
			qe = time.parse("2012093020000");
			
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
					System.out.println("load file into memory...");
					
					ArrayList<ArrayList<SamplePoint>> sp = new ArrayList<ArrayList<SamplePoint>>();
					sp = commonMethods.LoadFromFile.getArrayPoints(path, "");
					//bt.importBulkofData(staticFunction.LoadFromFile.getArrayPoints(path, ""));
					
					
					frameTable.FrameTable ft; 
					frameTable.IPFrameTable ipt; 
					
					BasicTableWindowQuery bt = new BasicTableWindowQuery();
					BasicDeltaWindowQuery dbt = new BasicDeltaWindowQuery();
					
					SegmentTableWindowQuery st = new SegmentTableWindowQuery();	
					SegmentDeltaWindowQuery dst = new SegmentDeltaWindowQuery();
					
					
					
					parallelFrameTable.IPFrameTable pipt;
					
					System.out.println("Done! Total size is " + sp.size() + " importing to table.... ");
					
					long sTime, eTime;
					
					
					ArrayList<Double> xset = new ArrayList<Double>();
					ArrayList<Double> yset = new ArrayList<Double>();
					
					
					for (int j = 0; j < 50; j++){
						double x1 = Math.random()*2 + 116;
						double y1 = Math.random()*2 + 39;
						xset.add(x1);
						yset.add(y1);
						
						
					}
					
					
					//long starttime = qs.getTime();
					//long endtime = qe.getTime();
					
					long starttime =t.getTime() + 3600*1000*12;
					long endtime = 0; 

					
					System.out.println("Importing data bt");
					bt.importBulkofData(sp);
					for (int k = 0; k < 6; k++){
						double distance = 1000 * (Math.pow(2, k));			
						System.out.println("distance is "  + distance/1000);
						System.gc();
						sTime = System.currentTimeMillis();
						for(int i = 0; i <xset.size(); i++){
							bt.SpatialWindowQuery(xset.get(i), yset.get(i), distance);
						}	
						eTime = System.currentTimeMillis();
						System.out.println("Spatial Only search:  " + (eTime - sTime));
					}
					bt = null;
					System.gc();
					
					
					System.out.println("Importing data dbt");
					dbt.importBulkofData(sp);
					for (int k = 0; k < 6; k++){
						double distance = 1000 * (Math.pow(2, k));
						System.out.println("distance is "  + distance/1000);
						System.gc();
						sTime = System.currentTimeMillis();
						for(int i = 0; i <xset.size(); i++){
							dbt.SpatialWindowQuery(xset.get(i), yset.get(i), distance);
						}	
						eTime = System.currentTimeMillis();
						System.out.println("Spatial Only search:  " + (eTime - sTime));
					}
					dbt = null;
					System.gc();
					
					
					System.out.println("Importing data st");
					st.importBulkofData(sp);
					for (int k = 0; k < 6; k++){
						double distance = 1000 * (Math.pow(2, k));
						System.out.println("distance is "  + distance/1000);
						System.gc();
						sTime = System.currentTimeMillis();
						for(int i = 0; i <xset.size(); i++){
							st.SpatialWindowQuery(xset.get(i), yset.get(i), distance);
						}	
						eTime = System.currentTimeMillis();
						System.out.println("Spatial Only search:  " + (eTime - sTime));
					}
					st = null;
					System.gc();
					
					
					System.out.println("Importing data bt");
					dst.importBulkofData(sp);
					for (int k = 0; k < 6; k++){
						double distance = 1000 * (Math.pow(2, k));
						System.out.println("distance is "  + distance/1000);
						System.gc();
						sTime = System.currentTimeMillis();
						for(int i = 0; i <xset.size(); i++){
							dst.SpatialWindowQuery(xset.get(i), yset.get(i), distance);
						}	
						eTime = System.currentTimeMillis();
						System.out.println("Spatial Only search:  " + (eTime - sTime));
					}
					dst = null;
					System.gc();
					
					
					System.out.println("Importing data ft");
					ft = new frameTable.FrameTable(t.getTime(), 60000, 2);
					ft.importBulkofData(sp);
					for (int k = 0; k < 6; k++){
						double distance = 1000 * (Math.pow(2, k));
						System.out.println("distance is "  + distance/1000);
						System.gc();
						sTime = System.currentTimeMillis();
						for(int i = 0; i <xset.size(); i++){
							ft.SOnlyWindowQuery(xset.get(i), yset.get(i), distance);
						}	
						eTime = System.currentTimeMillis();
						System.out.println("Spatial Only search:  " + (eTime - sTime));
					}
					ft = null;
					System.gc();
					
					System.out.println("Importing data ipt");
					ipt = new frameTable.IPFrameTable(t.getTime(),30000, 200, 5);
					ipt.importBulkofData(sp);
					for (int k = 0; k < 6; k++){
						double distance = 1000 * (Math.pow(2, k));
						System.out.println("distance is "  + distance/1000);
						System.gc();
						sTime = System.currentTimeMillis();
						for(int i = 0; i <xset.size(); i++){
							ipt.SOnlyWindowQuery(xset.get(i), yset.get(i), distance, 14.0);
						}	
						eTime = System.currentTimeMillis();
						System.out.println("Spatial Only search:  " + (eTime - sTime));
					}
					ipt = null;
					System.gc();
					
					
					System.out.println("Start ST Query-------------------------------------------");
					
					bt = new BasicTableWindowQuery();
					dbt = new BasicDeltaWindowQuery();
					
					st = new SegmentTableWindowQuery();	
					dst = new SegmentDeltaWindowQuery();
					
					
					System.out.println("Importing data bt");
					bt.importBulkofData(sp);
					
					for (int kk = 1; kk <= 4; kk = kk + 1) {
						System.out.println("Time interval is " + kk);
						
						for (int k = 0; k < 6; k++){
							double distance = 1000 * (Math.pow(2, k));			
							System.out.println("distance is "  + distance/1000);
							System.gc();
							sTime = System.currentTimeMillis();
							for(int i = 0; i <xset.size(); i++){
								bt.SpatialTemporalWindowQuery(starttime, 3600*1000*kk, xset.get(i), yset.get(i), distance);
							}	
							eTime = System.currentTimeMillis();
							System.out.println("SpatialTemporal Only search:  " + (eTime - sTime));
						}
					}
									
					bt = null;
					System.gc();
					
					
					System.out.println("Importing data dbt");
					dbt.importBulkofData(sp);
					for (int kk = 1; kk <= 4; kk = kk + 1) {
						System.out.println("Time interval is " + kk);
						
						for (int k = 0; k < 6; k++){
							double distance = 1000 * (Math.pow(2, k));			
							System.out.println("distance is "  + distance/1000);
							System.gc();
							sTime = System.currentTimeMillis();
							for(int i = 0; i <xset.size(); i++){
								dbt.SpatialTemporalWindowQuery(starttime, 3600*1000*kk, xset.get(i), yset.get(i), distance);
							}	
							eTime = System.currentTimeMillis();
							System.out.println("SpatialTemporal Only search:  " + (eTime - sTime));
						}
					}
									
					dbt = null;
					System.gc();
					
					
					System.out.println("Importing data st");
					st.importBulkofData(sp);
					for (int kk = 1; kk <= 4; kk = kk + 1) {
						System.out.println("Time interval is " + kk);
						
						for (int k = 0; k < 6; k++){
							double distance = 1000 * (Math.pow(2, k));			
							System.out.println("distance is "  + distance/1000);
							System.gc();
							sTime = System.currentTimeMillis();
							for(int i = 0; i <xset.size(); i++){
								st.SpatialTemporalWindowQuery(starttime, 3600*1000*kk, xset.get(i), yset.get(i), distance);
							}	
							eTime = System.currentTimeMillis();
							System.out.println("SpatialTemporal  search:  " + (eTime - sTime));
						}
					}
									
					st = null;
					System.gc();
					
					System.out.println("Importing data dst");
					dst.importBulkofData(sp);
					for (int kk = 1; kk <= 4; kk = kk + 1) {
						System.out.println("Time interval is " + kk);		
						for (int k = 0; k < 6; k++){
							double distance = 1000 * (Math.pow(2, k));			
							System.out.println("distance is "  + distance/1000);
							System.gc();
							sTime = System.currentTimeMillis();
							for(int i = 0; i <xset.size(); i++){
								dst.SpatialTemporalWindowQuery(starttime, 3600*1000*kk, xset.get(i), yset.get(i), distance);
							}	
							eTime = System.currentTimeMillis();
							System.out.println("SpatialTemporal Only search:  " + (eTime - sTime));
						}
					}
									
					dst = null;
					System.gc();
					
					
					System.out.println("Importing data ft");
					ft = new frameTable.FrameTable(t.getTime(), 60000, 2);
					ft.importBulkofData(sp);
					
					for (int kk = 2; kk <= 8; kk = kk + 2) {
						endtime = starttime + 3600*1000*kk;
						System.out.println("Time interval is " + kk);		
						for (int k = 0; k < 6; k++){
							double distance = 1000 * (Math.pow(2, k));			
							System.out.println("distance is "  + distance/1000);
							System.gc();
							sTime = System.currentTimeMillis();
							for(int i = 0; i <xset.size(); i++){
								ft.STWindowQuery(xset.get(i), yset.get(i), starttime, endtime, distance);
							}	
							eTime = System.currentTimeMillis();
							System.out.println("SpatialTemporal Only search:  " + (eTime - sTime));
						}
					}
									
					ft = null;
					System.gc();
					
					System.out.println("Importing data ipt");
					ipt = new frameTable.IPFrameTable(t.getTime(),30000, 200, 5);
					ipt.importBulkofData(sp);
					for (int kk = 2; kk <= 8; kk = kk + 2) {
						endtime = starttime + 3600*1000*kk;
						System.out.println("Time interval is " + kk);		
						for (int k = 0; k < 6; k++){
							double distance = 1000 * (Math.pow(2, k));			
							System.out.println("distance is "  + distance/1000);
							System.gc();
							sTime = System.currentTimeMillis();
							for(int i = 0; i <xset.size(); i++){
								ipt.STWindowQuery(xset.get(i), yset.get(i), starttime, endtime, distance, 14);
							}	
							eTime = System.currentTimeMillis();
							System.out.println("SpatialTemporal Only search:  " + (eTime - sTime));
						}
					}
					ipt = null;
					System.gc();
					
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
