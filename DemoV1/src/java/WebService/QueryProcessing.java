/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;


import dataS.SamplePoint;
import dataS.Trajectory;
import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * @author uqhwan21
 */
public class QueryProcessing {
    
    private parallelFrameTable.IPFrameTable pipt;
    private frameTable.FrameTable ft; 
    private frameTable.IPFrameTable ipt; 
    private basicTable.BasicTable bt;
    private deltaBasicTable.BasicTable dbt;
    private segmentedTable.SegmentedTable st;
    private deltaSegmentedTable.SegmentedTable dst;
    
    
    //parameters
    private int n;
    private int timeInterval;
    
    public QueryProcessing(int n, int timeInterval){
        
        
        ReadFileString rfs = new ReadFileString();
        
        this.n = n;
        this.timeInterval = timeInterval;
         
        this.bt= new basicTable.BasicTable();
        this.dbt= new deltaBasicTable.BasicTable();
        this.st = new segmentedTable.SegmentedTable();
        this.dst = new deltaSegmentedTable.SegmentedTable();
        this.ft = new frameTable.FrameTable(rfs.timeString, 60000, 200);
        this.ipt = new frameTable.IPFrameTable(rfs.timeString, 60000, 200, this.n);
        this.pipt = new parallelFrameTable.IPFrameTable(rfs.timeString, 60000, 200, 8);
        
        
        ArrayList<ArrayList<SamplePoint>> sp;

        sp = commonMethods.LoadFromFile.getArrayPoints(rfs.getFilePath(5), "");

        
        this.bt.importBulkofData(sp);
        this.dbt.importBulkofData(sp);
        this.st.importBulkofData(sp);
        this.dst.importBulkofData(sp);
        this.ft.importBulkofData(sp);
        this.ipt.importBulkofData(sp);
        this.pipt.importBulkofData(sp);
        
        
        
    }
    
    
    //calculate the id of decoded frame id and get the results
    public ArrayList<Frame> getCurrentFrame(int frameId, ArrayList<Integer> tIDs,
            int frameInterval){
        
        int iFrameId = 0;
        int startFrameId = 0;
        int endFrameId = 0;
        
        //current request frame is 
        if(checkIframe(frameId)){
            
        }
         
        return null;
    }
    
    private boolean checkIframe(int frameID){
        
        if(frameID % this.n == 0){
            return true;
        }else{
            return false;
        }
    }
    
    
    private ResultObject getResultNNSingle(String table, double x, double y, long stime, long etime){
        
        Trajectory data = null;
        long st = 0;
        long et = 0;
       
         
        
        double time = et -st;
        time = time/1000000;
        
        if(data == null){
            return null;
        }
        
        return new ResultObject(data.getTrajectory(), time, table);
    }
    
    
    
    
    
    
}
