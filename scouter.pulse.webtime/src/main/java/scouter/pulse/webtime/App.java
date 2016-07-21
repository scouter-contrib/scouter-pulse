package scouter.pulse.webtime;

import java.net.InetAddress;
import java.util.HashMap;
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
	
	public final static String[] TARGET_SITES = {
			"http://www.google.co.kr",
			"http://www.google.com",
			"http://www.google.co.uk",
			"http://www.google.co.jp",
			"http://www.google.co.be", // Belgium
			"http://www.google.com.br", // Brazil
			"http://www.google.de",
			"http://www.google.co.za", // South Africa
	};
	
	static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	
    public static void main( String[] args ) throws Exception
    {
    	
    	int status = registerMetadata();
    	
    	if (status == HttpStatus.SC_CREATED) {
    		
    		Runnable r = new Runnable() {
    			HashMap<String, InternetObject> statusMap = new HashMap<String, InternetObject>(); 
				
				public void run() {
					sendCounterData();
					
					for (int i = 0; i < TARGET_SITES.length; i++) {
		    			final String site = TARGET_SITES[i];
		    			InternetObject obj = statusMap.get(site);
		    			if (obj == null) {
		    				obj = new InternetObject();
		    				obj.site = site;
		    				statusMap.put(site, obj);
		    			}
		    			if (obj.status == WebStatusEnum.READY) {
			    			obj.startTime = System.currentTimeMillis();
			    			obj.status = WebStatusEnum.ING;
			    			Unirest.get(site).asStringAsync(new Callback<String>() {
								
								public void failed(UnirestException e) {
									InternetObject obj = statusMap.get(site);
									obj.endTime = System.currentTimeMillis();
									obj.status = WebStatusEnum.FAILED;
								}
								
								public void completed(HttpResponse<String> response) {
									InternetObject obj = statusMap.get(site);
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
						JSONObject rootJson = new JSONObject();
						JSONObject objectJson = new JSONObject();
						rootJson.put("object", objectJson);
						objectJson.put("host", InetAddress.getLocalHost().getHostName());
						objectJson.put("name", "Google_Timer");
						objectJson.put("type", "google");
						objectJson.put("address", InetAddress.getLocalHost().getHostAddress());
						
						JSONArray countersArray = new JSONArray();
						rootJson.put("counters", countersArray);
						for (String key : statusMap.keySet()) {
							InternetObject obj = statusMap.get(key);
							JSONObject counterJson = null;
							switch (obj.status) {
								case DONE:
									counterJson = new JSONObject();
									counterJson.put("name",  key.substring(key.lastIndexOf("/") + 1));
									counterJson.put("value", obj.endTime - obj.startTime);
									obj.status = WebStatusEnum.READY;
									break;
								case ING:
									counterJson = new JSONObject();
									counterJson.put("name",  key.substring(key.lastIndexOf("/") + 1));
									counterJson.put("value", System.currentTimeMillis() - obj.startTime);
									break;
								case FAILED:
									obj.status = WebStatusEnum.READY;
									break;
								case READY:
									break;
							}
							if (counterJson != null) {
								countersArray.put(counterJson);
							}
						}
						
						HttpResponse<JsonNode> response = Unirest.post("http://127.0.0.1:6180/counter")
						    	.header("accept", "application/json")
						    	.header("content-type", "application/json")
						    	.body(rootJson).asJson();
						
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
    	objectJson.put("type", "google");
    	objectJson.put("display", "Google Webtime");
    	rootJson.put("object", objectJson);
    	JSONArray counterArray = new JSONArray();
    	rootJson.put("counters", counterArray);
    	for (int i = 0; i < TARGET_SITES.length; i++) {
    		JSONObject counterMap = new JSONObject();
    		counterArray.put(counterMap);
    		String site = TARGET_SITES[i];
    		counterMap.put("name", site.substring(site.lastIndexOf("/") + 1));
    		counterMap.put("unit", "ms");
    	}
    	
    	
    	HttpResponse<JsonNode> response = Unirest.post("http://127.0.0.1:6180/register")
    	.header("accept", "application/json")
    	.header("content-type", "application/json")
    	.body(rootJson).asJson();
    	
    	return response.getStatus();
    }
}
