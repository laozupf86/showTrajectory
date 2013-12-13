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
public class Frame {
    
    public int tid;
    public double sLat;
    public double sLng;
    public double eLat;
    public double eLng;
    public double speed;
    
    public Frame(int tid, double sLat, double sLng, double eLat, double eLng, double speed){
        
        this.tid = tid;
        this.sLat = sLat;
        this.eLat = eLat;
        this.sLng = sLng;
        this.eLng = eLng;
        this.speed = speed;
                
               
        
    }
    
    
}
