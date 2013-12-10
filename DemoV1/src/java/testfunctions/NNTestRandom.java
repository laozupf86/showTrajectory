package testfunctions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dataS.SamplePoint;

public class NNTestRandom {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
					
					long starttime =t.getTime() + 3600*1000;
					long endtime = 0; 

					/*for (int kk = 1; kk <= 4; kk = kk + 1){
						
						System.out.println("Importing data ft-30, k is " + 500*kk);
						//pipt = new parallelFrameTable.IPFrameTable(t.getTime(), 120000, 200, 4*kk);
						//ipt = new frameTable.IPFrameTable(t.getTime(),30000, 200, 4, 116.0, 39.0, 0.0025*kk, 0.0025*kk);
						ft = new frameTable.FrameTable(t.getTime(), 30000, 2, 116.0, 39.0, 0.005*kk, 0.005*kk);
						//ipt.importBulkofData(sp);
						ft.importBulkofDataWithIndex(sp);

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
							}	
							eTime = System.currentTimeMillis();
							System.out.println("NN search:  " + (eTime - sTime));
							
						
						}
						
						ft = null;
						System.gc();					
					}
					
					
					for (int kk = 1; kk <= 4; kk = kk + 1){
						
						System.out.println("Importing data ft-60, k is " + 500*kk);
						//pipt = new parallelFrameTable.IPFrameTable(t.getTime(), 120000, 200, 4*kk);
						//ipt = new frameTable.IPFrameTable(t.getTime(),30000, 200, 4, 116.0, 39.0, 0.0025*kk, 0.0025*kk);
						ft = new frameTable.FrameTable(t.getTime(), 60000, 2, 116.0, 39.0, 0.005*kk, 0.005*kk);
						//ipt.importBulkofData(sp);
						ft.importBulkofDataWithIndex(sp);

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
							}	
							eTime = System.currentTimeMillis();
							System.out.println("NN search:  " + (eTime - sTime));
							
						
						}
						
						ft = null;
						System.gc();
					
												
						
					}
			
					for (int kk = 1; kk <= 4; kk = kk + 1){
						
						System.out.println("Importing data ft-90, k is " + 500*kk);
						//pipt = new parallelFrameTable.IPFrameTable(t.getTime(), 120000, 200, 4*kk);
						//ipt = new frameTable.IPFrameTable(t.getTime(),30000, 200, 4, 116.0, 39.0, 0.0025*kk, 0.0025*kk);
						ft = new frameTable.FrameTable(t.getTime(), 90000, 2, 116.0, 39.0, 0.005*kk, 0.005*kk);
						//ipt.importBulkofData(sp);
						ft.importBulkofDataWithIndex(sp);

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
							}	
							eTime = System.currentTimeMillis();
							System.out.println("NN search:  " + (eTime - sTime));
							
						
						}
						
						ft = null;
						System.gc();
					
					}
										
					*/
			/*		for (int kk = 1; kk <= 4; kk = kk + 1){
						
						System.out.println("Importing data pt-30-4, k is " + 500*kk);
						//pipt = new parallelFrameTable.IPFrameTable(t.getTime(), 120000, 200, 4*kk);
						ipt = new frameTable.IPFrameTable(t.getTime(),30000, 200, 4, 116.0, 39.0, 0.005*kk, 0.005*kk);
						//ft = new frameTable.FrameTable(t.getTime(), 90000, 2, 116.0, 39.0, 0.005*kk, 0.005*kk);
						//ipt.importBulkofData(sp);
						ipt.importBulkofDataWithIndex(sp);
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
								
								ipt.findNNByGridIndex(xset.get(i), yset.get(i), starttime, endtime, 40);
							}	
							eTime = System.currentTimeMillis();
							System.out.println("NN search:  " + (eTime - sTime));
							
						
						}
						
						ipt = null;
						System.gc();
					
					
					}
					
					for (int kk = 1; kk <= 4; kk = kk + 1){
						
						System.out.println("Importing data pt-60-4, k is " + 500*kk);
						//pipt = new parallelFrameTable.IPFrameTable(t.getTime(), 120000, 200, 4*kk);
						ipt = new frameTable.IPFrameTable(t.getTime(),60000, 200, 4, 116.0, 39.0, 0.005*kk, 0.005*kk);
						ipt.importBulkofDataWithIndex(sp);
						
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
								ipt.findNNByGridIndex(xset.get(i), yset.get(i), starttime, endtime, 40);

							}	
							eTime = System.currentTimeMillis();
							System.out.println("NN search:  " + (eTime - sTime));				
						}
						
						ipt = null;
						System.gc();
					}
					
					for (int kk = 1; kk <= 4; kk = kk + 1){
						
						System.out.println("Importing data pt-90-4, k is " + 500*kk);
						//pipt = new parallelFrameTable.IPFrameTable(t.getTime(), 120000, 200, 4*kk);
						ipt = new frameTable.IPFrameTable(t.getTime(),90000, 200, 4, 116.0, 39.0, 0.005*kk, 0.005*kk);
						ipt.importBulkofDataWithIndex(sp);
						
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
								ipt.findNNByGridIndex(xset.get(i), yset.get(i), starttime, endtime, 40);

							}	
							eTime = System.currentTimeMillis();
							System.out.println("NN search:  " + (eTime - sTime));				
						}
						
						ipt = null;
						System.gc();
					}
					
					
					for (int kk = 1; kk <= 4; kk = kk + 1){
						
						System.out.println("Importing data pt-30-8, k is " + 500*kk);
						//pipt = new parallelFrameTable.IPFrameTable(t.getTime(), 120000, 200, 4*kk);
						ipt = new frameTable.IPFrameTable(t.getTime(),30000, 200, 8, 116.0, 39.0, 0.005*kk, 0.005*kk);
						ipt.importBulkofDataWithIndex(sp);
						
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
								ipt.findNNByGridIndex(xset.get(i), yset.get(i), starttime, endtime, 40);

							}	
							eTime = System.currentTimeMillis();
							System.out.println("NN search:  " + (eTime - sTime));				
						}
						
						ipt = null;
						System.gc();
					}
					
					for (int kk = 1; kk <= 4; kk = kk + 1){
						
						System.out.println("Importing data pt-30-12, k is " + 500*kk);
						//pipt = new parallelFrameTable.IPFrameTable(t.getTime(), 120000, 200, 4*kk);
						ipt = new frameTable.IPFrameTable(t.getTime(),30000, 200, 12, 116.0, 39.0, 0.005*kk, 0.005*kk);
						ipt.importBulkofDataWithIndex(sp);
						
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
								ipt.findNNByGridIndex(xset.get(i), yset.get(i), starttime, endtime, 40);

							}	
							eTime = System.currentTimeMillis();
							System.out.println("NN search:  " + (eTime - sTime));				
						}
						
						ipt = null;
						System.gc();
					}*/
					
					for (int kk = 1; kk <= 4; kk = kk + 1){
						
						System.out.println("Importing data pipt-30-4, k is " + 500*kk);
						pipt = new parallelFrameTable.IPFrameTable(t.getTime(), 30000, 200, 4, 116.0, 39.0, 0.005*kk, 0.005*kk);
						pipt.importBulkofDataWithIndex(sp);
						
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
								pipt.findNNByGridIndex(xset.get(i), yset.get(i), starttime, endtime, 40);

							}	
							eTime = System.currentTimeMillis();
							System.out.println("NN search:  " + (eTime - sTime));				
						}
						
						pipt = null;
						System.gc();
					}
					
					for (int kk = 1; kk <= 4; kk = kk + 1){
						
						System.out.println("Importing data pipt-60-4, k is " + 500*kk);
						pipt = new parallelFrameTable.IPFrameTable(t.getTime(), 60000, 200, 4, 116.0, 39.0, 0.005*kk, 0.005*kk);
						pipt.importBulkofDataWithIndex(sp);
						
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
								pipt.findNNByGridIndex(xset.get(i), yset.get(i), starttime, endtime, 40);

							}	
							eTime = System.currentTimeMillis();
							System.out.println("NN search:  " + (eTime - sTime));				
						}
						
						pipt = null;
						System.gc();
					}
					
					for (int kk = 1; kk <= 4; kk = kk + 1){
						
						System.out.println("Importing data pipt-90-4, k is " + 500*kk);
						pipt = new parallelFrameTable.IPFrameTable(t.getTime(), 90000, 200, 4, 116.0, 39.0, 0.005*kk, 0.005*kk);
						pipt.importBulkofDataWithIndex(sp);
						
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
								pipt.findNNByGridIndex(xset.get(i), yset.get(i), starttime, endtime, 40);

							}	
							eTime = System.currentTimeMillis();
							System.out.println("NN search:  " + (eTime - sTime));				
						}
						
						pipt = null;
						System.gc();
					}
					
					
					for (int kk = 1; kk <= 4; kk = kk + 1){
						
						System.out.println("Importing data pipt-30-8, k is " + 500*kk);
						pipt = new parallelFrameTable.IPFrameTable(t.getTime(), 30000, 200, 8, 116.0, 39.0, 0.005*kk, 0.005*kk);
						pipt.importBulkofDataWithIndex(sp);
						
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
								pipt.findNNByGridIndex(xset.get(i), yset.get(i), starttime, endtime, 40);

							}	
							eTime = System.currentTimeMillis();
							System.out.println("NN search:  " + (eTime - sTime));				
						}
						
						pipt = null;
						System.gc();
					}
					
					for (int kk = 1; kk <= 4; kk = kk + 1){
						
						System.out.println("Importing data pipt-30-12, k is " + 500*kk);
						pipt = new parallelFrameTable.IPFrameTable(t.getTime(), 30000, 200, 12, 116.0, 39.0, 0.005*kk, 0.005*kk);
						pipt.importBulkofDataWithIndex(sp);
						
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
								pipt.findNNByGridIndex(xset.get(i), yset.get(i), starttime, endtime, 40);

							}	
							eTime = System.currentTimeMillis();
							System.out.println("NN search:  " + (eTime - sTime));				
						}
						
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
