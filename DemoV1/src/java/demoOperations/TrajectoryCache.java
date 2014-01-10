/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package demoOperations;

import dataS.SamplePoint;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author haozhouwang
 */
public class TrajectoryCache {
    
    private HashMap<Integer, CacheObject> cache;
    private int currentKeyFrameId;
    private int currentCacheIndex;
    
    /*
     * @param keyId the id of key frame
    */
    public TrajectoryCache(int keyId){
        this.cache = new HashMap<>();
        this.currentKeyFrameId = keyId;
        this.currentCacheIndex = 0;
    }
    
    public TrajectoryCache(int keyId, int movedCacheIndex){
        this.cache = new HashMap<>();
        this.currentKeyFrameId = keyId;
        this.currentCacheIndex = movedCacheIndex;
    }
    
    public int getKeyId(){
        return this.currentKeyFrameId;
    }
    
    public double getSpeed(int id){
        return cache.get(id).speed;
    }
    
    public int getFrameIndex(int id){
        return cache.get(id).frameIndex;
    }
    
    public SamplePoint getSamplePoint(int id){
        return cache.get(id).p.get(currentCacheIndex);
    }
    
    public CacheObject getCacheObject(int id){
        return cache.get(id);
    }
    
    public void updateCacheFrameIndex(){
        this.currentCacheIndex++;
    }
    
    public void addNewCacheObject(int id, CacheObject cacheObject){
        this.cache.put(id, cacheObject);
    }
    
    
}

