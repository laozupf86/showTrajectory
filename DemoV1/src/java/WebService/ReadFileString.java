/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Haozhou
 */
public class ReadFileString {
    
    public ArrayList<String> list;
    public long timeString;
    
    public ReadFileString(){
        list = new ArrayList<String>();
        list.add("C:\\Users\\uqhwan21.EAIT\\Documents\\data\\cleaned\\1\\"); //0
        list.add("C:\\myUQ\\expData\\TC\\anotherData\\"); //1
        list.add("C:\\myUQ\\expData\\TC\\cleaneddata\\"); //2
        list.add("C:\\Users\\WANG Haozhou\\Documents\\myUQ\\expData\\cleaneddata\\"); //3
        list.add("C:\\Users\\Haozhou\\Documents\\MyUQ\\dataset\\anotherData\\"); //4
        list.add("C:\\myUQ\\expData\\TC\\temp\\"); //5
        list.add("C:\\Users\\uqhwan21.EAIT\\Documents\\data\\anotherData\\"); //6
        list.add("E:\\Data\\hwang\\cleaned\\1\\"); //7
        
        DateFormat time = new SimpleDateFormat("yyyyMMddhhmmss");
        Date t = null;
        try {
            t = time.parse("20120930000000");				
					
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.timeString = t.getTime();
    }
    
    public String getFilePath(int i){
        return this.list.get(i);
    }
    
    public long getTime(){
        return this.timeString;
    }
    
    
}
