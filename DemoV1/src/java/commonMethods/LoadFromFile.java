package commonMethods;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import dataS.SamplePoint;
import dataS.TempFramePoint;

/**
 * Load trajectory details from files
 * @author Haozhou
 *
 */

public class LoadFromFile {
	
	
	
	
	public static String[] getIDs(String filePath, String timeslot){
		File pf = new File(filePath + timeslot);
		return pf.list();
		
	}
	
	public static ArrayList<ArrayList<SamplePoint>> getArrayPointsWithLargeFile(String filePath, String timeslot){
		
		ArrayList<ArrayList<SamplePoint>> tr = new ArrayList<ArrayList<SamplePoint>>();
		
		ArrayList<SamplePoint> sp; 
		BufferedReader reader = null;
		String line = null;
		long c = 0;
		
		for(int i = 1; i<=7; i++){
			String newfilePath = filePath + i; 
		
			for (String s : getIDs(newfilePath, timeslot)){
				sp = new ArrayList<SamplePoint>();
				try{
					reader = new BufferedReader(new FileReader(newfilePath + timeslot + "\\" + s));
					while((line = reader.readLine()) != null){
						String[] word = line.split(",");
						long t = Long.parseLong(word[2]);
						Double x = Double.parseDouble(word[0]);
						Double y = Double.parseDouble(word[1]);
						SamplePoint sample = new SamplePoint(x, y, t);
						//sample.tid = tid;
						sp.add(sample);
					}
								
					reader.close();
					
				}catch (Exception e){
					e.printStackTrace();
				}
				//tid++;
				tr.add(sp);
				c++;
				if (c > 4000000000L){
					break;
				}
			}
			
		}
		
		
		//int tid = 0;
		
		return tr;
		
		
		
	}
	
	
	
	public static ArrayList<ArrayList<SamplePoint>> getArrayPoints(String filePath, String timeslot){
		
		ArrayList<ArrayList<SamplePoint>> tr = new ArrayList<ArrayList<SamplePoint>>();
		
		ArrayList<SamplePoint> sp; 
		BufferedReader reader = null;
		String line = null;
		
		
		//int tid = 0;
		int c = 0;
		for (String s : getIDs(filePath, timeslot)){
			sp = new ArrayList<SamplePoint>();
			try{
				reader = new BufferedReader(new FileReader(filePath + timeslot + "\\" + s));
				while((line = reader.readLine()) != null){
					String[] word = line.split(",");
					long t = Long.parseLong(word[2]);
					Double x = Double.parseDouble(word[0]);
					Double y = Double.parseDouble(word[1]);
					SamplePoint sample = new SamplePoint(x, y, t);
					//sample.tid = tid;
					sp.add(sample);
				}
							
				reader.close();
				
			}catch (Exception e){
				e.printStackTrace();
			}
			//tid++;
			tr.add(sp);
			c++;
			if (c > 4000000){
				break;
			}
		}
		
		return tr;
		
	}
	
	public static ArrayList<SamplePoint> getPoints(String filePath, String id, String timeslot){
		
		
		ArrayList<SamplePoint> sp = new ArrayList<SamplePoint>();
		BufferedReader reader = null;
		String line = null;
		
		
		try{
			reader = new BufferedReader(new FileReader(filePath + timeslot + "\\" + id));
			while((line = reader.readLine()) != null){
				String[] word = line.split(",");
				long t = Long.parseLong(word[2]);
				Double x = Double.parseDouble(word[0]);
				Double y = Double.parseDouble(word[1]);
				SamplePoint sample = new SamplePoint(x, y, t);
				sp.add(sample);
			}
						
			reader.close();
			
		}catch (Exception e){
			e.printStackTrace();
		}
		
		//return s;
		
		//System.out.println("read file " + id);
		return sp;
		
	}
	
	
	
	public static void writeToFile(String filePath, String id, 
			ArrayList<TempFramePoint> tr, int j){
		
		FileWriter outFile;
		
		
		try{
			outFile = new FileWriter(filePath  + "\\" +  id + "-" + j);
			BufferedWriter bw = new BufferedWriter(outFile);
			
			int i = 0;
			for (TempFramePoint point : tr){
				//bw.newLine();
				bw.write(point.getX() + "," + point.getY() + "," + point.getT());
				if ( i != tr.size() - 1){
					bw.newLine();
				}
				i++;
			}
			
			bw.flush();
			bw.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	

}
