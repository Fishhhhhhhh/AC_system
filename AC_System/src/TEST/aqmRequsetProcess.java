package TEST;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import TEST.AC.ACsize;
import TEST.AC.ACtype;
import TEST.RpcParser;

/**
 * Servlet implementation class aqmRequsetProcess
 */
@WebServlet("/ServiceAC")
public class aqmRequsetProcess extends HttpServlet {
	protected enum Request {Start, Enqueue, Dequeue}
	private static boolean startFlag = false;
	private static long timeStamp = 0;
    private HashMap<String, Long> timeMap = new HashMap<>();  
    private PriorityQueue<AC> ACqueue = new PriorityQueue<>(new Comparator<AC>(){
    		@Override
    		public int compare(AC ac1, AC ac2) {
    			if (ac1.type.equals(ACtype.passenger) && 
    				ac2.type.equals(ACtype.cargo)) {
    				return -1;
    			} else if (ac1.type.equals(ACtype.cargo) && 
        			ac2.type.equals(ACtype.passenger)) {
    				return 1;
    			} else {
    				if (ac1.size.equals(ACsize.large) && 
    					ac2.size.equals(ACsize.small)) {
    	    				return -1;
    	    			} else if (ac1.size.equals(ACsize.small) && 
    					ac2.size.equals(ACsize.large)) {
    	    				return 1;
    	    			} else {
    	    				if (timeMap.get(ac1.id) < timeMap.get(ac2.id)) {
    	    					return -1;
    	    				} else if (timeMap.get(ac1.id) > timeMap.get(ac2.id)) {
    	    					return 1;
    	    				} else {
    	    					return 0;
    	    				}
    	    			}
    			}
    		}
    });
    /**
     * @see HttpServlet#HttpServlet()
     */
    public aqmRequsetProcess() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    		try {
    			response.setContentType("application/json");
        	    //responce in json
        	    response.addHeader("Access-Control-Allow-Origin", "*");   
        	    //allow all viewer to view
        	    String ACid = "";
        	    String ACrequest = "";    
        	    //create a printwriter from sponce that we can add data to respnce
        	    if (request.getParameter("request") != null) {
        	    	  	ACrequest = request.getParameter("request");
        	    	  	if (ACrequest.equals(Request.Start.toString())) {
        	    	  		startFlag = true;
        	    	  		RpcParser.writeOutput(response, new JSONObject().put("Start", "success"));
        	    	  	} else if (ACrequest.equals(Request.Dequeue.toString()) && startFlag) {
        	    	  		if (!ACqueue.isEmpty()) {
        	    	  			String id = ACqueue.poll().id;
            	    	  		timeMap.remove(id);
            	    	  		RpcParser.writeOutput(response, new JSONObject().put("Dequeue", id));
        	    	  		} else {
        	    	  			RpcParser.writeOutput(response, new JSONObject().put("Dequeue", "queue is empty"));
        	    	  		}     	    	  		
        	    	  	} else if (ACrequest.equals("GetQueue") && startFlag && !ACqueue.isEmpty()) {
        	    	  		ArrayList<String> list = new ArrayList<>();
        	    	  		Iterator<AC> it = ACqueue.iterator();
        	    	  		while (it.hasNext()) {
        	    	  			list.add(it.next().id);
        	    	  		}
        	    	  		RpcParser.writeOutput(response, new JSONObject().put("GetQueue", list.toString()));
        	    	  	}
        	    }  
    		} catch (JSONException e) {
  			  e.printStackTrace();
  		}     
    	}



    protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    		  try {
    			  JSONObject input = RpcParser.parseInput(request);
    		      if (input.has("request") && input.has("AC")) {
    		    	  	JSONArray array = (JSONArray) input.get("AC");
    		    	  	String ACrequest = (String) input.get("request");
    		    	  	String id = (String) array.get(0);
    		    	  	if (startFlag && ACrequest.equals(Request.Enqueue.toString()) && !timeMap.containsKey(id)) {
    		    	  		AC ACToEnqueue = new AC(id, ACtype.valueOf((String) array.get(1)), ACsize.valueOf((String) array.get(2)));
    		    	  		timeMap.put(id, timeStamp++);
    		    	  		ACqueue.add(ACToEnqueue);
    		    	  		RpcParser.writeOutput(response, new JSONObject().put("Enqueue", "success"));
    		    	  	} else if (startFlag && ACrequest.equals(Request.Enqueue.toString()) && timeMap.containsKey(id)) {
    		    	  		RpcParser.writeOutput(response, new JSONObject().put("Enqueue", "Already in the queue"));
    		    	  	}    		    	  	
    		      } else {
    		    	  		RpcParser.writeOutput(response, new JSONObject().put("Enqueue", "fail"));
    		      }
    		  } catch (JSONException e) {
    			  e.printStackTrace();
    		  }
    }
}
