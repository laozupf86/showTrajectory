/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import com.google.gson.Gson;
import dataS.SamplePoint;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author uqhwan21
 */
public class SharkDBHolder extends HttpServlet {

    private QueryProcessing queryProcdssing;
    private int returnFlag = 0;    
    
     @Override
    public void init() throws ServletException{
         
       this.queryProcdssing = new QueryProcessing();
       
    }
    
    
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        OutputClass returnObject = null;
        InfoClass ic = null;
        
        if(request.getParameter("querytype").equals("NN")){
            
            String[] tableName = request.getParameter("table").split(",");
            String x = request.getParameter("x");
            String y = request.getParameter("y");
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");
            
            ArrayList<ResultObject> data = this.queryProcdssing.getResultNN(tableName, Double.parseDouble(x), Double.parseDouble(y), 
                    Long.parseLong(startTime), Long.parseLong(endTime));
            
            ArrayList<RunningTime> rt = new ArrayList<>();
            ArrayList<Double> datalength = new ArrayList<>();
            
            for(ResultObject o : data){
                rt.add(new RunningTime(o.tableName, o.runningTime));
            }
            datalength.add(CalculateTrajectoryDistance.getDistance(data.get(0).data));
            returnObject = new OutputClass("Yes", "NN", data.get(0).data, rt, datalength);
            
        }else if (request.getParameter("querytype").equals("windowquery")){
            HttpSession user = request.getSession(true);
            String changeFlag = request.getParameter("changepage");
            
            
              
            String[] tableName = request.getParameter("table").split(",");
            String x = request.getParameter("x");
            String y = request.getParameter("y");
            String r = request.getParameter("r");
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");
            
            ArrayList<TrajectoriesObject> data;
          
            
            ArrayList<RunningTime> rt = new ArrayList<>();
            
            //System.out.println(startTime)
            
            if (changeFlag == null){
                data = this.queryProcdssing.getResultWinfow(tableName, Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(r), 
                    Long.parseLong(startTime), Long.parseLong(endTime));
                
                
                for(TrajectoriesObject o : data){
                    rt.add(new RunningTime(o.tableName, o.runningTime));
                }
                
                OutputClass oc = new OutputClass("Yes", "Window", data.get(0).data, rt, null);
                
                user.setAttribute("data", oc);
                //user.setAttribute("changePage", "yes");
                
                
                
                //int p = Integer.parseInt(request.getParameter("page"));
                int n = Integer.parseInt(request.getParameter("number"));
                
               ArrayList< ArrayList<SamplePoint>> selectData = new ArrayList<>();
               ArrayList<Double> datalength = new ArrayList<>();
               
               for(int i = 0; i < n; i++){
                    if (i >= data.get(0).data.size()){
                        break;
                    }else{
                        selectData.add(data.get(0).data.get(i));
                        datalength.add(CalculateTrajectoryDistance.getDistance(data.get(0).data.get(i)));
                    }
                }
                
               
               
               
                returnObject = new OutputClass("Yes", "Window", selectData, rt, datalength);
                   
            }else{
                OutputClass oc = (OutputClass) user.getAttribute("data");
                 
                ArrayList< ArrayList<SamplePoint>> tempData = (ArrayList< ArrayList<SamplePoint>>) oc.getData();
                ArrayList< ArrayList<SamplePoint>> selectData = new ArrayList<>();
                ArrayList<Double> datalength = new ArrayList<>();
                  
                int p = Integer.parseInt(request.getParameter("page"));
                int n = Integer.parseInt(request.getParameter("number"));
                
                for(int i = ((p-1)*n ); i < p*n; i++){
                    if (i >= tempData.size()){
                        break;
                    }else{
                        selectData.add(tempData.get(i));
                        datalength.add(CalculateTrajectoryDistance.getDistance(tempData.get(i)));
                    }
                }
                
               returnObject = new OutputClass("Yes", "Window", selectData, oc.getRunningTime(), datalength); 
                
            }
        }else if(request.getParameter("querytype").equals("getinfo")){
            
            ArrayList<Double> x = new ArrayList<>();
            x.add(116.0);
            x.add(39.0);
            ArrayList<Long> y = new ArrayList<>();
            y.add(1235829631000L);
            y.add(1239141696000L);
            
            ic = new InfoClass();
            ic.centroid = x;
            ic.timePeriod = y;
            
        }

       // opc = new OutputClass(tableName, "NN", t.getTrajectory(), ((double)(et-st))/1000000);
        
        Gson g = new Gson();
        
        response.setContentType("text/html;charset=UTF-8");
        response.addHeader("Access-Control-Allow-Origin","*"); 
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
           if(ic != null){
               out.println(g.toJson(ic));
           } else{
               ic = null;
               out.println(g.toJson(returnObject));
           }
          
           
        } finally {            
            out.close();
        }
    }
    


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}

class RunningTime{
    private String tableName;
    private double runningTime;
    
    public RunningTime(String tableName, double runningTime){
        this.tableName = tableName;
        this.runningTime = runningTime;
    }
   
}