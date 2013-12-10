/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import dataS.SamplePoint;
import java.util.ArrayList;

/**
 *
 * @author uqhwan21
 */
public class ResultObject {
    
     public ArrayList<SamplePoint> data;
     public double runningTime;
     public String tableName;
     
     
     public ResultObject(ArrayList<SamplePoint> data, double runningTime, String tableName){
         this.data = data;
         this.runningTime = runningTime;
         this.tableName = tableName;
     }
    
}
