/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package demoOperations;

import dataS.IFramePoint;
import dataS.SamplePoint;
import frameTable.IPFrameTable;

/**
 *
 * @author haozhouwang
 */
public class QuickSelection extends IPFrameTable{

    
    
    
    public QuickSelection(long st, int intervalTime, int max, int n) {
        super(st, intervalTime, max, n);
    }
    
    public TrajectoryCache getKeyFramebyTime(int keyFrameId){
        
        int c = 0;
        int pSlot = (keyFrameId - 1)*this.n;
        
        TrajectoryCache tCache = new TrajectoryCache(keyFrameId);
        
        for(IFramePoint fp : dfl.getIFrameByFID(keyFrameId).getPoints()){
            CacheObject cacheObject = new CacheObject(this.n);
            cacheObject.frameIndex = c;
            SamplePoint keyPoint = new SamplePoint(fp.getX(), fp.getY());
            cacheObject.addPoint(keyPoint);
            //decoding
            for(int i = 1; i < this.n; i++){
                int shiftingCode = dfl.getPFrameByPID(pSlot + i).getPointByPID(c);
                short dx = (short) (shiftingCode >> 16);
                short dy = (short) (shiftingCode & 0xFFFF);
                
                if(dx == Short.MAX_VALUE){
                    continue;
                }
                
                double x = fp.getX() + (double)(dx/1000000);
                double y = fp.getY() + (double)(dy/1000000);
                
                cacheObject.addPoint(new SamplePoint(x, y));
            }
            
            tCache.addNewCacheObject(fp.getTid(), cacheObject);
            c++;
        }
        
        return tCache;
        
        
    }
    
}
