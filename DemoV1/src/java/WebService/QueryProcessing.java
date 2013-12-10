/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import advantageOperators.NNSearch;
import dataS.SamplePoint;
import dataS.Trajectory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

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
    
    public QueryProcessing(){
        
        
        ReadFileString rfs = new ReadFileString();
         
        this.bt= new basicTable.BasicTable();
        this.dbt= new deltaBasicTable.BasicTable();
        this.st = new segmentedTable.SegmentedTable();
        this.dst = new deltaSegmentedTable.SegmentedTable();
        this.ft = new frameTable.FrameTable(rfs.timeString, 60000, 200);
        this.ipt = new frameTable.IPFrameTable(rfs.timeString, 60000, 200, 8);
        this.pipt = new parallelFrameTable.IPFrameTable(rfs.timeString, 60000, 200, 8);
        
        
        ArrayList<ArrayList<SamplePoint>> sp = new ArrayList<>();

        sp = commonMethods.LoadFromFile.getArrayPoints(rfs.getFilePath(5), "");

        
        this.bt.importBulkofData(sp);
        this.dbt.importBulkofData(sp);
        this.st.importBulkofData(sp);
        this.dst.importBulkofData(sp);
        this.ft.importBulkofData(sp);
        this.ipt.importBulkofData(sp);
        this.pipt.importBulkofData(sp);
        
        
        
    }
    
    
    
    private ResultObject getResultNNSingle(String table, double x, double y, long stime, long etime){
        
        Trajectory data = null;
        long st = 0;
        long et = 0;
       
          switch (table){
            case "bt" : 
                 st = System.nanoTime();
               data = this.bt.findNN(x,y,stime,etime, 40);
                et  = System.nanoTime();
                break;
            case "dbt":
               st = System.nanoTime();
               data = this.dbt.findNN(x,y,stime,etime, 40);
                 et  = System.nanoTime();
                break;
            case "st":
                st = System.nanoTime();
               data = this.st.findNN(x,y,stime,etime, 40);
                 et  = System.nanoTime();
                break;
            case "dst":
               st = System.nanoTime();
               data = this.dst.findNN(x,y,stime,etime, 40);
                 et  = System.nanoTime();
                break;
            case "ft":
               st = System.nanoTime();
               data = this.ft.findNN(x,y,stime,etime, 40);
                 et  = System.nanoTime();
                break; 
            case "ipt":
               st = System.nanoTime();
               data = this.ipt.findNN(x,y,stime,etime, 40);
                et  = System.nanoTime();
                break;  
        }
        
        double time = et -st;
        time = time/1000000;
        
        if(data == null){
            return null;
        }
        
        return new ResultObject(data.getTrajectory(), time, table);
    }
    
    public ArrayList<ResultObject> getResultNN(String[] tables, double x, double y, long stime, long etime){
        
        ArrayList<ResultObject> r = new ArrayList<>();
        
        for(String t : tables){
           r.add(this.getResultNNSingle(t, x, y, stime, etime));
        }
        
        return r;
    }
    
    
    public ArrayList<TrajectoriesObject> getResultWinfow(String[] tables, double x, double y, double r, long stime, long etime){
        ArrayList<TrajectoriesObject> result = new ArrayList<>();
        
        for(String t : tables){
            result.add(this.getResultWindowSingle(t, x, y, r, stime, etime));
        }
        //System.out.println(result.size());
        return result;
        
    }
    
    private TrajectoriesObject getResultWindowSingle(String table, double x, double y, double r, long stime, long etime){
        long st = 0;
        long et = 0;
        
        HashMap<Integer, ArrayList<SamplePoint>> data  = null;;
        
        long midTime = stime + (etime - stime)/2;
        long tRad =  (etime - stime)/2;
        
        switch (table){
            case "bt" : 
               st = System.nanoTime();
               data = this.bt.SpatialTemporalWindowQuery(midTime, tRad, x, y, r);
               et  = System.nanoTime();
                break;
            case "dbt":
               st = System.nanoTime();
               data = this.dbt.SpatialTemporalWindowQuery(midTime, tRad, x, y, r);
               et  = System.nanoTime();
               break;
            case "st":
               st = System.nanoTime();
                data = this.st.SpatialTemporalWindowQuery(midTime, tRad, x, y, r);
               et  = System.nanoTime();
                break;
            case "dst":
               st = System.nanoTime();
                data = this.dst.SpatialTemporalWindowQuery(midTime, tRad, x, y, r);
               et  = System.nanoTime();
                break;
            case "ft":
               st = System.nanoTime();
               data = this.ft.STWindowQuery(x, y,  stime, etime, r);
               et  = System.nanoTime();
               break; 
            case "ipt":
               st = System.nanoTime();
               data = this.ipt.STWindowQuery(x, y, stime, etime, r, 40);
               et  = System.nanoTime();
               break;  
        }
        
        double time = et - st;
        
        if(data == null){
            return null;
        }
        
        ArrayList<ArrayList<SamplePoint>> extractdata = new ArrayList<>();
        
        //System.out.println("Get value");
        for(Entry<Integer, ArrayList<SamplePoint>> a : data.entrySet()){
            //System.out.println("value is " + a.getValue().get(0).getX());
            extractdata.add(a.getValue());
        }
        
        
        return new TrajectoriesObject(extractdata, time/1000000, table);
    }
    
}
