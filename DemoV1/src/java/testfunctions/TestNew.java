package testfunctions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import segmentedTable.SegmentTableWindowQuery;
import basicTable.BasicTableWindowQuery;
import dataS.SamplePoint;
import deltaBasicTable.BasicDeltaWindowQuery;
import deltaSegmentedTable.SegmentDeltaWindowQuery;
import frameTable.FrameTable;
import frameTable.FrameTableWindowQuery;
import frameTable.IPFrameTable;
import frameTable.IPFrameTableWindowQuery;

public class TestNew {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		long minTime = 1348926904000L;
		long maxTime = 1349013602000L;
		int iter = 100;

		long startTime = 0;
		long endTime = 0;
		long totalTime = 0;
		
		long[] sumTime_1 = new long[6];
		long[] sumTime_2 = new long[6];
		long[] sumTime_3 = new long[6];
		long[] sumTime_4 = new long[6];
		long[] sumTime_5 = new long[6];
		long[] sumTime_6 = new long[6];
		
		long[] sumSpatial_1 = new long[6];
		long[] sumSpatial_2 = new long[6];
		long[] sumSpatial_3 = new long[6];
		long[] sumSpatial_4 = new long[6];
		long[] sumSpatial_5 = new long[6];
		long[] sumSpatial_6 = new long[6];
		
		long[][] sumST_1 = new long[6][6];		
		long[][] sumST_2 = new long[6][6];		
		long[][] sumST_3 = new long[6][6];		
		long[][] sumST_4 = new long[6][6];		
		long[][] sumST_5 =new long[6][6];		
		long[][] sumST_6 = new long[6][6];

		long randTime = 0;
		long timeRange = 0;
		double randLongi = 0;
		double randLati = 0;
		double distance = 0;
		//String Filename = "C:\\Users\\uqxli17\\Documents\\cleaned\\cleaned\\1\\";
		String Filename = "C:\\myUQ\\expData\\TC\\temp1\\";
		//String Filename = "E:\\uqhwan16\\data\\cleaned\\1\\";
		
		System.out.println("reading file...");
		ArrayList<ArrayList<SamplePoint>> sp = new ArrayList<ArrayList<SamplePoint>>();
		sp = commonMethods.LoadFromFile.getArrayPoints(Filename, "");
		
		FileWriter fstream = new FileWriter("out.txt", true);
		BufferedWriter out = new BufferedWriter(fstream);
		
		System.out.println("reading file done!, load into memory...");
		try {
			
			
			BasicTableWindowQuery basic = new BasicTableWindowQuery();
			basic.importBulkofData(sp);
			
			System.out.println("BT done!");
			
			BasicDeltaWindowQuery basicDelta = new BasicDeltaWindowQuery();
			basicDelta.importBulkofData(sp);
			
			System.out.println("DBT done!");
			
			SegmentTableWindowQuery segment = new SegmentTableWindowQuery();
			segment.importBulkofData(sp);
			
			System.out.println("ST done!");
			
			SegmentDeltaWindowQuery segmentDelta = new SegmentDeltaWindowQuery();
			segmentDelta.importBulkofData(sp);
			
			System.out.println("DST done!");
			
			FrameTable iFrame = new FrameTable(minTime, 60000, 1);
			iFrame.importBulkofData(sp);
			
			System.out.println("FT done!");
			
			IPFrameTable IP = new IPFrameTable(minTime, 60000, 1, 5);
			IP.importBulkofData(sp);

			System.out.println("IPT done!");
			
			for (int k = 0; k < iter; k++) {
				randTime = (long) (minTime + Math.random()* (maxTime - minTime));
				randLongi = 115 + Math.random() * 2;
				randLati = 39 + Math.random() * 2;
				System.out.println("round: "+k);
				
				////////////temporal window query/////////////
				/*System.out.println(" temporal processing ");
				for (int i = 1; i < 7; i++) {
					timeRange = i * 600000;					
				
					System.gc();
					startTime = System.currentTimeMillis();
					basic.TimeWindowQuery(randTime, timeRange);
					endTime = System.currentTimeMillis();
					totalTime = endTime - startTime;
					sumTime_1[i-1] = sumTime_1[i-1] + totalTime;
					
					System.gc();
					startTime = System.currentTimeMillis();
					basicDelta.TimeWindowQuery(randTime, timeRange);
					endTime = System.currentTimeMillis();
					totalTime = endTime - startTime;
					sumTime_2[i-1] = sumTime_2[i-1] + totalTime;

					System.gc();
					startTime = System.currentTimeMillis();
					segment.TimeWindowQuery(randTime, timeRange);
					endTime = System.currentTimeMillis();
					totalTime = endTime - startTime;
					sumTime_3[i-1] = sumTime_3[i-1] + totalTime;

					System.gc();
					startTime = System.currentTimeMillis();
					segmentDelta.TimeWindowQuery(randTime, timeRange);
					endTime = System.currentTimeMillis();
					totalTime = endTime - startTime;
					sumTime_4[i-1] = sumTime_4[i-1] + totalTime;
				
					System.gc();
					startTime = System.currentTimeMillis();
					iFrame.TimeWindowQuery(randTime, timeRange);
					endTime = System.currentTimeMillis();
					totalTime = endTime - startTime;
					sumTime_5[i-1] = sumTime_5[i-1] + totalTime;
					
					System.gc();
					startTime = System.currentTimeMillis();
					IP.TimeWindowQuery(randTime, timeRange);
					endTime = System.currentTimeMillis();
					totalTime = endTime - startTime;
					sumTime_6[i-1] = sumTime_6[i-1] + totalTime;
				}
				*/
	
				////////////spatial window query///////////
				System.out.println(" spatial processing ");
				for (int j = 0; j < 6; j++) {
					distance = 1000 * (Math.pow(2, j));
					
					System.gc();
					startTime = System.currentTimeMillis();
					basic.SpatialWindowQuery(randLongi, randLati, distance);
					endTime = System.currentTimeMillis();
					totalTime = endTime - startTime;
					sumSpatial_1[j] = sumSpatial_1[j] + totalTime;
					
					System.gc();
					startTime = System.currentTimeMillis();
					basicDelta.SpatialWindowQuery(randLongi, randLati,distance);
					endTime = System.currentTimeMillis();
					totalTime = endTime - startTime;
					sumSpatial_2[j] = sumSpatial_2[j] + totalTime;

					System.gc();
					startTime = System.currentTimeMillis();
					segment.SpatialWindowQuery(randLongi, randLati,distance);
					endTime = System.currentTimeMillis();
					totalTime = endTime - startTime;
					sumSpatial_3[j] = sumSpatial_3[j] + totalTime;

					System.gc();
					startTime = System.currentTimeMillis();
					segmentDelta.SpatialWindowQuery(randLongi, randLati,distance);
					endTime = System.currentTimeMillis();
					totalTime = endTime - startTime;
					sumSpatial_4[j] = sumSpatial_4[j] + totalTime;

					System.gc();
					startTime = System.currentTimeMillis();
					iFrame.SOnlyWindowQuery(randLongi, randLati, distance);
					endTime = System.currentTimeMillis();
					totalTime = endTime - startTime;
					sumSpatial_5[j] = sumSpatial_5[j] + totalTime;

					System.gc();
					startTime = System.currentTimeMillis();
					IP.SOnlyWindowQuery(randLongi, randLati,distance, 14.0);
					endTime = System.currentTimeMillis();
					totalTime = endTime - startTime;
					sumSpatial_6[j] = sumSpatial_6[j] + totalTime;
				}
				
				///////spatial temporal query //////
				System.out.println(" spatial-temporal processing ");
				
				for (int i = 1; i < 7; i++) {
					timeRange = i * 600000 + randTime;

					for (int j = 0; j < 6; j++) {
						distance = 1000 * (Math.pow(2, j));

						// ////////////// basic data structure ////////////////
						System.gc();
						startTime = System.currentTimeMillis();
						basic.SpatialTemporalWindowQuery(randTime, timeRange,randLongi, randLati, distance);
						endTime = System.currentTimeMillis();
						totalTime = endTime - startTime;
						sumST_1[i-1][j] = sumST_1[i-1][j] + totalTime;

						//////////// basic compressed data structure //////////////
						System.gc();
						startTime = System.currentTimeMillis();
						basicDelta.SpatialTemporalWindowQuery(randTime,timeRange, randLongi, randLati, distance);
						endTime = System.currentTimeMillis();
						totalTime = endTime - startTime;
						sumST_2[i-1][j] = sumST_2[i-1][j] + totalTime;

						/////////// segment data structure ///////////////
						System.gc();
						startTime = System.currentTimeMillis();
						segment.SpatialTemporalWindowQuery(randTime, timeRange,randLongi, randLati, distance);
						endTime = System.currentTimeMillis();
						totalTime = endTime - startTime;
						sumST_3[i-1][j] = sumST_3[i-1][j] + totalTime;

						//////////segment compressed data structure////////////
						System.gc();
						startTime = System.currentTimeMillis();
						segmentDelta.SpatialTemporalWindowQuery(randTime,timeRange, randLongi, randLati, distance);
						endTime = System.currentTimeMillis();
						totalTime = endTime - startTime;
						sumST_4[i-1][j] = sumST_4[i-1][j] + totalTime;

						//////////// iFrame data structure////////////
						System.gc();
						startTime = System.currentTimeMillis();
						iFrame.STWindowQuery(randLongi, randLati, randTime, timeRange, distance);
						endTime = System.currentTimeMillis();
						totalTime = endTime - startTime;
						sumST_5[i-1][j] = sumST_5[i-1][j] + totalTime;

						///////// IP Frame data structure////////////
						System.gc();
						startTime = System.currentTimeMillis();
						IP.STWindowQuery(randLongi, randLati, randTime, timeRange, distance, 14.0);
						endTime = System.currentTimeMillis();
						totalTime = endTime - startTime;
						sumST_6[i-1][j] = sumST_6[i-1][j] + totalTime;
					}
				}
			}
		
			// print temporal query result
			System.out.println(" temporal result ");
			out.write("\n temporal result \n");
			for(int i=0;i<6;i++){
				timeRange = (i+1) * 600000;					
				System.out.println("**********");
				System.out.println("timeWindow: " + timeRange);					
				out.write("******** \n");
				out.write("timeWindow: " + timeRange + "\n");
			
				System.out.println("basicTime: " + (double) sumTime_1[i] / iter);
				System.out.println("basicDeltaTime: " + (double) sumTime_2[i]/ iter);
				System.out.println("segmentTime: " + (double) sumTime_3[i]/ iter);
				System.out.println("segmentDeltaTime: "	+ (double) sumTime_4[i] / iter);
				System.out.println("iFrameTime: " + (double) sumTime_5[i]/ iter);
				System.out.println("IP Time: " + (double) sumTime_6[i] / iter);
			
				out.write("basicTime: " + (double) sumTime_1[i] / iter +"\n");
				out.write("basicDeltaTime: " + (double) sumTime_2[i] / iter +"\n");
				out.write("segmentTime: " + (double) sumTime_3[i] / iter +"\n");
				out.write("segmentDeltaTime: " + (double) sumTime_4[i] / iter +"\n");
				out.write("iFrameTime: " + (double) sumTime_5[i] / iter +"\n");
				out.write("IP Time: " + (double) sumTime_6[i] / iter +"\n");
				out.flush();
			}
		
		System.out.println(" spatial result ");
		out.write("\n spatial result \n");
		for(int i=0;i<6;i++){
			distance = 1000 * (Math.pow(2, i));			
			System.out.println("----------");
			System.out.println("distanceWindow: " + distance);			
			out.write("-----------\n");
			out.write("distanceWindow: " + distance + "\n");
			
			System.out.println("basicSpatial: " + (double) sumSpatial_1[i] / iter);
			System.out.println("basicDeltaSpatial: "+ (double) sumSpatial_2[i] / iter);
			System.out.println("segmentSpatial: "+ (double) sumSpatial_3[i] / iter);
			System.out.println("segmentDeltaSpatial: "+ (double) sumSpatial_4[i] / iter);
			System.out.println("iFrameSpatial: "+ (double) sumSpatial_5[i] / iter);
			System.out.println("IP Spatial: " + (double) sumSpatial_6[i]/ iter);
			
			out.write("basicSpatial: " + (double) sumSpatial_1[i] / iter +"\n");
			out.write("basicDeltaSpatial: " + (double) sumSpatial_2[i]/ iter +"\n");
			out.write("segmentSpatial: " + (double) sumSpatial_3[i] / iter +"\n");
			out.write("segmentDeltaSpatial: " + (double) sumSpatial_4[i]/ iter +"\n");
			out.write("iFrameSpatial: " + (double) sumSpatial_5[i] / iter +"\n");
			out.write("IP Spatial: " + (double) sumSpatial_6[i] / iter +"\n");
			out.flush();
		}
		
		System.out.println(" ST result ");
		out.write("\n ST result \n");
		for(int i=0;i<6;i++){
			timeRange = (i+1) * 600000;					
			System.out.println("**********");
			System.out.println("timeWindow: " + timeRange);					
			out.write("******** \n");
			out.write("timeWindow: " + timeRange + "\n");
			for(int j=0;j<6;j++){
				distance = 1000 * (Math.pow(2, j));
				System.out.println("----------");
				System.out.println("distanceWindow: " + distance);				
				out.write("-----------\n");
				out.write("distanceWindow: " + distance + "\n");
		
				System.out.println("basicST: " + (double) sumST_1[i][j] / iter);
				System.out.println("basicDeltaST: " + (double) sumST_2[i][j]/ iter);
				System.out.println("segmentST: " + (double) sumST_3[i][j]/ iter);
				System.out.println("segmentDeltaST: " + (double) sumST_4[i][j]/ iter);
				System.out.println("iFrameST: " + (double) sumST_5[i][j] / iter);
				System.out.println("IP ST: " + (double) sumST_6[i][j] / iter);

				out.write("basicST: " + (double) sumST_1[i][j] / iter + "\n");
				out.write("basicDeltaST: " + (double) sumST_2[i][j] / iter+ "\n");
				out.write("segmentST: " + (double) sumST_3[i][j] / iter + "\n");
				out.write("segmentDeltaST: " + (double) sumST_4[i][j] / iter+ "\n");
				out.write("iFrameST: " + (double) sumST_5[i][j] / iter + "\n");
				out.write("IP ST: " + (double) sumST_6[i][j] / iter + "\n");
				out.flush();
			}
		}
		out.close();
		} catch (Exception e) {
			e.printStackTrace();			
		}
	}

}
