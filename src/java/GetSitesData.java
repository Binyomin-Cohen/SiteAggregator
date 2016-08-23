/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author seanc
 */
@WebServlet(urlPatterns = {"/GetSitesData"})
public class GetSitesData extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
  
       String bh = getAPIData("https://www.bhphotovideo.com/bnh/controller/home?A=GetQuickEndecaSearch&Q=json&O=&N=3929443921+35+38");
       String googleTrends = getAPIData("https://www.google.com/trends/api/stories/latest?hl=en-US&tz=240&cat=all&fi=9&fs=9&geo=US&ri=150&rs=10&sort=0");
       
       
       JSONObject bhJson = null;
       JSONArray bhItems = new JSONArray();
       try{
           bhJson = new JSONObject(bh);
           JSONArray itemsArray = bhJson.getJSONArray("items");
           for(int i = 0; i < itemsArray.length(); ++i){
               bhItems.put(new JSONObject()
                       .put("description", itemsArray.getJSONObject(i).getString("shortDescription"))
                       .put("price", itemsArray.getJSONObject(i).getString("price")) );
           }
       }
       catch(Exception e){
           System.out.println(e.toString());
       }
       
       JSONObject googleJson = null;
       JSONArray titles = new JSONArray();
        try {
            googleJson = new JSONObject(googleTrends.substring(4));
            if(googleJson.has("storySummaries")){
                JSONObject storySummaries = googleJson.getJSONObject("storySummaries");
                JSONArray featuredStories = storySummaries.getJSONArray("featuredStories");
                for(int i =0; i < featuredStories.length(); ++i){
                    titles.put(featuredStories.getJSONObject(i).getString("title"));
                }
            }
        } catch (JSONException ex) {
            
        }
        JSONObject aggregateJson = new JSONObject();
        try{
        aggregateJson.put("googleTrends", titles).put("bh", bhItems);
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        response.setContentType("application/json;charset=UTF-8");
       try (PrintWriter out = response.getWriter()) {
           out.print(aggregateJson);
        }
    }
    
    
    public static String getAPIData(String url){
        StringBuilder sb = new StringBuilder("");
        try{
         URL oracle = new URL(url);
        HttpURLConnection yc = (HttpURLConnection) oracle.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                                    yc.getInputStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null){ 
            sb.append(inputLine);
        }    
        in.close();
            
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        return sb.toString();

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
