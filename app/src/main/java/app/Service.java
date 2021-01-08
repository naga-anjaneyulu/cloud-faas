package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

public class Service implements HttpFunction {

	public void service(HttpRequest request, HttpResponse response) throws Exception {
		 response.appendHeader("Access-Control-Allow-Origin", "*");
		 if ("OPTIONS".equals(request.getMethod())) {
			 System.out.print("hey");
		      response.appendHeader("Access-Control-Allow-Methods", "GET");
		      response.appendHeader("Access-Control-Allow-Headers", "Content-Type");
		      response.appendHeader("Access-Control-Max-Age", "3600");
		      response.setStatusCode(HttpURLConnection.HTTP_NO_CONTENT);
		     
		    }
       System.out.println("Started Processing request");
		String url = processRequest(request).get("url");
        if(url != null) {
            url = url.trim();
            try {
             
                String responseObj = calculateDistribution(url);
                
                BufferedWriter out = response.getWriter();
                out.write(responseObj);
                System.out.println("Finished Processing request");
            } catch (IOException e) {
                e.printStackTrace();
            }
	}
	}   
      public Map<String, String> processRequest(HttpRequest request) {
    	  System.out.println("Started Building requestObj");
   		 Map<String, String> mappedObj = new HashMap<String, String>();
   	        try {
   	            mapObject(mappedObj, request.getReader().lines().collect(Collectors.joining()));
   	        } catch (IOException e) {
   	            e.printStackTrace();
   	        }
   	     System.out.println(mappedObj.toString());
   	     System.out.println("Finished Building requestObj");
   	        return mappedObj;
   		
   	}
      
      private void mapObject(Map<String, String> mappedObj, String body) {
    	  System.out.println(body);
  		try {
              JSONObject jsonData = (JSONObject) new JSONParser().parse(body);
              System.out.println(jsonData.toString());
              for(Object key: jsonData.keySet()) {
                  if(key.toString().equals("data")) {
                	  JSONObject urlData = (JSONObject) new JSONParser().parse(jsonData.get(key).toString());
                	  System.out.println(urlData.toJSONString());
                	  
                	  for(Object key1: urlData.keySet()) {
                		  mappedObj.put(key1.toString(), urlData.get(key1).toString());
                	  }
                	  
                	 
                  }
              }
          } catch (ParseException e) {
              e.printStackTrace();
          }
  		
  	}

      
      public String calculateDistribution(String url) throws IOException {
  		System.out.println("Started Calculating Distribution");
  		HashMap<Integer,Integer> hMap = new HashMap<Integer, Integer>();
  		String content = getContent(url);
  		String[] sentences = content.split("\\.");
  		int count  = 1;
  		for(String sent : sentences) {
  			String[] arr = sent.split("\\s");
  			hMap.put(count++,arr.length);
  		}
  		
  		StringBuilder json = new StringBuilder();
  		json.append("{");
  		for(Map.Entry<Integer,Integer> m : hMap.entrySet()) {
  			json.append("\""+m.getKey()+"\"").append(":").append(m.getValue()).append(",");
  		}
  		String js = json.toString().substring(0,json.toString().length()-1);
  		js += "}";
  		
  		System.out.println("Finished Calculating Distribution");
  		
  		return js;
  	}
      
      public  String getContent(String url) throws IOException {
  		System.out.println("Started Getting content");
          HttpURLConnection con = null;
          BufferedReader in = null;
          StringBuilder lines = new StringBuilder();
          URL urlObj = new URL(url);
          con = (HttpURLConnection) urlObj.openConnection();
          con.setRequestMethod("GET");

              in = new BufferedReader(
                      new InputStreamReader(con.getInputStream()));
              String line;
              while ((line = in.readLine()) != null) {
              	line = line.replace("\n","").replace("\r","");
              	lines.append(line);
              }
              	
              System.out.println("Finished Getting content");    
          

          return lines.toString();
      }  
      
}
