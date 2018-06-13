package APIs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.json.Json;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class to query and handle the WalkScore API for the walking score and transit score for a provided address
 *
 */
public class WalkScore {
	private final String APIKey = "/* INSERT API HERE */";		// API Key provided by WalkScore
	private String address;										// street address
	private String city; 										// city
	private String state;										// state (abbreviated)
	private String zip;											// 5 digit zip code
	private double lat;											// latitude
	private double lon;											// longitude
	
	/**
	 * Constructor provided the String street address, city, state, zip code, latitude, and longitude
	 * @param String a street address 
	 * @param String c city
	 * @param String s abbreviated state 
	 * @param String z zip code 
	 * @param double la latitude 
	 * @param double lo longitude 
	 */
	public WalkScore(String a, String c, String s, String z, double la, double lo){
		address = a.replaceAll(" ", "%20");
		city = c.replaceAll(" ", "%20");
		state = s;
		zip = z;
		lat = la;
		lon = lo;
	}
	
	/**
	 * Queries the Walk Score API to get the rating and description for the walk and transit score for the provided property
	 * @return String JSON object of WalkScore 
	 */
	public String getTransitScores() {
		String url = "http://api.walkscore.com/score?format=json&address=" + address + "%20" + city + "%20" + state + "%20" + zip + "&lat=" + lat + "&lon=" + lon + "&transit=1&bike=1&wsapikey=" + APIKey;
		
		try {
			
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();	
			
			int ws_rating = 0;
			String ws_desc = "";
			int ts_rating = 0;
			String ts_desc = "";
			
			JSONObject root = new JSONObject(response.toString());
			if (root.has("walkscore") && !root.isNull("walkscore") && !root.get("walkscore").equals(""))
				ws_rating = root.getInt("walkscore");
			if (root.has("description") && !root.isNull("description") && !root.get("description").equals(""))
				ws_desc = root.getString("description");
			if(root.has("transit") && !root.isNull("transit") && !root.get("transit").equals("")){
				JSONObject transit = root.getJSONObject("transit");
				if (transit.has("score") && !transit.isNull("score") && !transit.get("score").equals(""))
					ts_rating = root.getInt("bedrooms");
				if (transit.has("description") && !transit.isNull("description") && !transit.get("description").equals(""))
					ts_desc = transit.getString("description");
			}
			
			String jsonWalkString = Json.createObjectBuilder()
					.add("walkscore", ws_rating)
					.add("walkscore_desc", ws_desc)
					.add("transitscore", ts_rating)
		            .add("transitscore_desc", ts_desc)
		            .build()
		            .toString();
	
			return jsonWalkString;	
			
		} catch (Exception e) {
			System.out.println(e);
			return "";
		}
	}
	
	/**
	 * Given the provided JSON String and score type, abstracts the appropriate rating
	 * @param JSON String walkString
	 * @param String type
	 * @return integer of transit rating 
	 */
	public static int getWalkRating(String walkString, String type){
		int rating = 0;
		
		JSONObject walkRoot;
		try {
			walkRoot = new JSONObject(walkString);
			if(walkRoot.has(type) && !walkRoot.isNull(type) && !walkRoot.get(type).equals(""))
				rating = walkRoot.getInt(type);				
	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return rating;
	}
	
	/**
	 * Given the provided JSON String and score type, abstracts the appropriate description
	 * @param JSON String walkString
	 * @param String type
	 * @return String of transit description 
	 */
	public static String getWalkDesc(String walkString, String type){
		String desc = "";
		
		JSONObject walkRoot;
		try {
			walkRoot = new JSONObject(walkString);
			if(walkRoot.has(type) && !walkRoot.isNull(type) && !walkRoot.get(type).equals(""))
				desc = walkRoot.getString(type);				
	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return desc;
	}
}
