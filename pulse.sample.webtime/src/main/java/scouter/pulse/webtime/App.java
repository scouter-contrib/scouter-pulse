package scouter.pulse.webtime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Hello world!
 *
 */
public class App 
{
	
	public final static String[] SAMPLE_SITES = {
			"http://www.google.co.kr",
			"http://www.google.com",
			"http://www.google.co.uk",
			"http://www.google.co.jp",
			"http://www.google.co.be", // Belgium Google
			"http://www.google.com.br", // Brazil Google
			"http://www.google.de",
			"http://www.google.co.za", // SouthAfrica Google
	};
	
	static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	
	static String ip = "127.0.0.1";
	static String port = "6180";
	
	static List<String> siteList = null; 
	
    public static void main( String[] args ) throws Exception
    {
    	
    	if (args != null && args.length > 0) {
    		ip = args[0];
    		if (args.length > 1) {
    			port = args[1];
    			if (args.length > 2) {
    				String filename = args[2];
    				File f = new File(filename);
    				if (f.canRead()) {
    					BufferedReader br = new BufferedReader(new FileReader(f));
    					String line = null;
    					siteList = new ArrayList<String>();
    					while ((line = br.readLine()) != null) {
    						siteList.add(line);
    					}
    					br.close();
    				}
    			}
    		}
    	}
    	
    	if (siteList == null) {
    		siteList = new ArrayList<String>();
    		for (String site : SAMPLE_SITES) {
    			siteList.add(site);
    		}
    	}
    	
    	int status = registerMetadata();
    	
    	if (status == HttpStatus.SC_CREATED) {
    		
    		Runnable r = new Runnable() {
    			HashMap<String, WebObject> statusMap = new HashMap<String, WebObject>(); 
				
				public void run() {
					sendCounterData();
					for (int i = 0; i < siteList.size(); i++) {
		    			final String site = siteList.get(i);
		    			WebObject obj = statusMap.get(site);
		    			if (obj == null) {
		    				obj = new WebObject();
		    				obj.site = site;
		    				statusMap.put(site, obj);
		    			}
		    			if (obj.status == WebStatusEnum.READY) {
			    			obj.startTime = System.currentTimeMillis();
			    			obj.status = WebStatusEnum.ING;
			    			Unirest.get(site).asStringAsync(new Callback<String>() {
								
								public void failed(UnirestException e) {
									WebObject obj = statusMap.get(site);
									obj.endTime = System.currentTimeMillis();
									obj.status = WebStatusEnum.FAILED;
								}
								
								public void completed(HttpResponse<String> response) {
									WebObject obj = statusMap.get(site);
									obj.endTime = System.currentTimeMillis();
									obj.status = WebStatusEnum.DONE;
								}
								
								public void cancelled() {
									
								}
			    			});
		    			}
		    		}
				}
				
				private void sendCounterData() {
					try {
						JSONArray jsonArray = new JSONArray();
						for (String key : statusMap.keySet()) {
							WebObject obj = statusMap.get(key);
							JSONObject element = null;
							JSONObject objectJson = null;
							JSONArray countersArray = null;
							JSONObject counterJson = null;
							switch (obj.status) {
								case DONE:
									element = new JSONObject();
									objectJson = new JSONObject();
									element.put("object", objectJson);
									objectJson.put("host", InetAddress.getLocalHost().getHostName());
									objectJson.put("name", key.substring(key.lastIndexOf("/") + 1));
									objectJson.put("type", "website");
									objectJson.put("address", InetAddress.getLocalHost().getHostAddress());
									countersArray = new JSONArray();
									element.put("counters", countersArray);
									counterJson = new JSONObject();
									counterJson.put("name",  "Time");
									counterJson.put("value", obj.endTime - obj.startTime);
									countersArray.put(counterJson);
									obj.status = WebStatusEnum.READY;
									break;
								case ING:
									element = new JSONObject();
									objectJson = new JSONObject();
									element.put("object", objectJson);
									objectJson.put("host", InetAddress.getLocalHost().getHostName());
									objectJson.put("name", key.substring(key.lastIndexOf("/") + 1));
									objectJson.put("type", "website");
									objectJson.put("address", InetAddress.getLocalHost().getHostAddress());
									countersArray = new JSONArray();
									element.put("counters", countersArray);
									counterJson = new JSONObject();
									counterJson.put("name",  "WebTime");
									counterJson.put("value", System.currentTimeMillis() - obj.startTime);
									countersArray.put(counterJson);
									break;
								case FAILED:
									obj.status = WebStatusEnum.READY;
									break;
								case READY:
									break;
							}
							if (element != null) {
								jsonArray.put(element);
							}
						}
						
						HttpResponse<JsonNode> response = Unirest.post("http://" + ip + ":" + port + "/counter")
						    	.header("accept", "application/json")
						    	.header("content-type", "application/json")
						    	.body(jsonArray).asJson();
						
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			};
    		executor.scheduleAtFixedRate(r, 0, 2, TimeUnit.SECONDS);
    	}
    }
    
    private static int registerMetadata() throws UnirestException {
    	JSONObject rootJson = new JSONObject();
    	JSONObject objectJson = new JSONObject();
    	objectJson.put("type", "website");
    	objectJson.put("display", "Web");
    	rootJson.put("object", objectJson);
    	JSONArray counterArray = new JSONArray();
    	rootJson.put("counters", counterArray);
		JSONObject counterMap = new JSONObject();
		counterArray.put(counterMap);
		counterMap.put("name", "Time");
		counterMap.put("unit", "ms");
    	
    	HttpResponse<JsonNode> response = Unirest.post("http://" + ip + ":" + port + "/register")
    	.header("accept", "application/json")
    	.header("content-type", "application/json")
    	.body(rootJson).asJson();
    	
    	return response.getStatus();
    }
}
