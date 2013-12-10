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
public class CalculateTrajectoryDistance {
    
    public static double getDistance(ArrayList<SamplePoint> data){
        double distance = 0.0;
        
        for(int i = 1;  i < data.size(); i++){
            distance = distance + commonMethods.Distance.getDistance(data.get(i -1).getX(), data.get(i -1).getY(), 
                    data.get(i).getX(), data.get(i).getY());
        }
        
        
        return distance;
    }
    
}
