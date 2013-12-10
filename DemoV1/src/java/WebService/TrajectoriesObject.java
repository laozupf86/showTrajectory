/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import dataS.SamplePoint;
import java.util.ArrayList;

/**
 *
 * @author WANG Haozhou
 */
public class TrajectoriesObject {
    
    public ArrayList<ArrayList<SamplePoint>> data;
    public double runningTime;
    public String tableName;
    
    public TrajectoriesObject(ArrayList<ArrayList<SamplePoint>> data, double runningTime, String tableName){
        this.data = data;
        this.runningTime = runningTime;
        this.tableName = tableName;
    }
    
    
}
