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
public class OutputClass {
    private String resultStatus ;
    private String queryType;
    private Object data;
    private Object runningTime;
    private String errorMessage;
    private Object datalength;
    
    public OutputClass(String resultStatus, String queryType, Object data, Object runningTime, Object datalength){
        this.resultStatus = resultStatus;
        this.queryType = queryType;
        this.data = data;
        this.runningTime = runningTime;
        this.errorMessage = "";
        this.datalength = datalength;
    }
    
    public OutputClass(String resultStatus, String queryType, String errorMessage){
        this.resultStatus = resultStatus;
        this.queryType = queryType;
        this.data = null;
        this.runningTime = null;
        this.errorMessage = errorMessage;
    }
    
    public Object getData(){
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public Object getRunningTime() {
        return runningTime;
    }
    
   
    
}
