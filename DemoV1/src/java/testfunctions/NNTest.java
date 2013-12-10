package testfunctions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import dataS.SamplePoint;

public class NNTest {

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
							
							basicTable.BasicTable bt = new basicTable.BasicTable();
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
							
							
							//ipt = new frameTable.IPFrameTable(ts, 30000, 2, 4);
							//dst = new deltaSegmentedTable.SegmentedTable();;
							//pipt = new parallelFrameTable.IPFrameTable(ts, 30000, 200, 4*k);
							
							
							//dbt.importBulkofData(sp);
							
							/*double x1 = 116.418842;
							double y1 = 39.915463;
							
							double x2 = 116.518842;
							double y2 = 39.815463;
							
							double x3 = 116.618842;
							double y3 = 39.715463;
							
							double x4 = 116.318842;
							double y4 = 40.0195463;
							
							double x5 = 116.218842;
							double y5 = 40.1095643;*/
							
							
							ArrayList<Double> xset = new ArrayList<Double>();
							ArrayList<Double> yset = new ArrayList<Double>();
							
							
							for (int j = 0; j < 40; j++){
								double x1 = Math.random()*2 + 116;
								double y1 = Math.random()*2 + 39;
								xset.add(x1);
								yset.add(y1);
								
								
							}
							
							
							//long starttime = qs.getTime();
							//long endtime = qe.getTime();
							
							long starttime =t.getTime() + 3600*1000;
							long endtime = 0; 
							
							//ipt = new frameTable.IPFrameTable(t.getTime(),30000, 200, 4, 116.0, 39.0, 0.005, 0.005);
							//ft = new frameTable.FrameTable(ts, 30000, 2);
							

							for (int kk = 1; kk <= 4; kk = kk + 1){
								
								System.out.println("Importing data, k is " + 500*kk);
								//pipt = new parallelFrameTable.IPFrameTable(t.getTime(), 120000, 200, 4*kk);
								//ipt = new frameTable.IPFrameTable(t.getTime(),30000, 200, 4, 116.0, 39.0, 0.0025*kk, 0.0025*kk);
								ft = new frameTable.FrameTable(t.getTime(), 90000, 2, 116.0, 39.0, 0.005*kk, 0.005*kk);
								//ipt.importBulkofData(sp);
								ft.importBulkofDataWithIndex(sp);
									
								//System.out.println("check Memory!");
								//System.gc();
								//System.in.read();
								
								
								for (int k = 2; k <= 8; k = k+2){
									if (k == 6){
										endtime = starttime + 3600*1000*1;
									}else{
										endtime = starttime + 3600*1000*k;
									}
									
									
									System.out.println("current hour is " + k + " start time is " + starttime + "  end time is " + endtime);
									System.gc();
									sTime = System.currentTimeMillis();
									for(int i = 0; i <xset.size(); i++){
										
										ft.findNNByGridIndex(xset.get(i), yset.get(i), starttime, endtime, 40);
										
										//ipt.selectOneRecordByTID(i*50);
										/*//ipt.deleteOneRecordByTID(i);
										
										//ipt.findNN(x1, y1, starttime, endtime, 40);
										ft.findNNByGridIndex(x2, y2, starttime, endtime, 40);
										ft.findNNByGridIndex(x3, y3, starttime, endtime, 40);
										ft.findNNByGridIndex(x4, y4, starttime, endtime, 40);
										ft.findNNByGridIndex(x5, y5, starttime, endtime, 40);
*/
										
										
										//pipt.selectOneRecordByTID(i*50);
										//dbt.deleteOneRecordByTID(i*70);

									}	
									eTime = System.currentTimeMillis();
									System.out.println("NN search:  " + (eTime - sTime));
									
								
								}
								
								ft = null;
								System.gc();
							
														
								
							}
							
							//ipt = new frameTable.IPFrameTable(ts, 30000, 200, 4, 116.0, 39.0, 0.005, 0.005);
							//ft = new frameTable.FrameTable(ts, 30000, 2, 116.0, 39.0, 0.005, 0.005);
							
							//ipt.importBulkofData(sp);
							//ipt.importBulkofDataWithIndex(sp);
							
							/*pipt = new parallelFrameTable.IPFrameTable(ts, 30000, 200, 4, 116.0, 39.0, 0.005, 0.005);
							
							pipt.importBulkofDataWithIndex(sp);
							
							int x = 1;
							
							parallelFrameTable.Trajectory result = pipt.findNNByGridIndex(116.461401, 39.922417, 1235729631000L, 1237538674000L, 40);
							System.out.println("tid is " + result.getTid());
							for (int ii = 0; ii < result.getTrajectotySize(); ii++){
								System.out.println(x +  " Sample is " + result.getTrajectory()[ii].getX() + "," + result.getTrajectory()[ii].getY() + "," + result.getTrajectory()[ii].getT());
								x++;
							}*/
							
							
							/*Trajectory result = ipt.findNNByGridIndex(116.461411, 39.922417, 1235729631000L, 1237538674000L, 40);
							
							System.out.println("tid is " + result.getTid());
							
							ArrayList<SamplePoint> r = result.getTrajectory();
							
							int x = 1;
							
							for (SamplePoint s : r){
								
								System.out.println(x +  " Sample is " + s.getX() + "," + s.getY() + "," + s.getT());
								x++;
							}*/
												
							
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
