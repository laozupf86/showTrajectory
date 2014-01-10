/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package demoOperations;

import dataS.SamplePoint;
import java.util.ArrayList;

/**
 *
 * @author haozhouwang
 */
public class CacheObject {
    public double speed;
    public ArrayList<SamplePoint> p;
    //public int CurrentkeyFrameId;
    public int frameIndex; 
    
    
    public CacheObject(int n){
        this.p = new ArrayList<>(n+2);
    }
    
    public void addPoint(SamplePoint point){
        this.p.add(point);
    }
}
