package APIs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class designated to query the Google Geocode API to get the latitude and longitude of a provided address
 *
 */
public class GoogleGeocode {
	private final String APIKey = "/* INSERT API HERE */";		// API Key for Geocode provided by Google
	private String address;										// street address
	private String city;										// city
	private String state;										// state (abbreviated)
	private double lat;											// latitude
	private double lng;											// longitude
	
	/**
	 * Constructor provided the string of the street address, city, and abbreviated state 
	 * @param String a (street address)
	 * @param String c (city)
	 * @param String s (abbreviated state)
	 */
	public GoogleGeocode(String a, String c, String s){
		address = a.replace(' ','+');	// replaces all spaces in address with '+' for URL
		city = c.replace(' ','+');		// replaces all spaces in city with '+' for URL
		state = s;
		lat = 0.0;
		lng = 0.0;
		
		getLatLong();
	}
	
	/**
	 * Gets the latitude
	 * @return double lat
	 */
	public double getLat(){
		return lat;
	}
	
	/**
	 * Gets he longitude
	 * @return double long
	 */
	public double getLong(){
		return lng;
	}
	
	/**
	 * Queries the Google Geocode API to obtain the latitude and longitude for the provided address
	 * Lat and Long are assigned to the class variables
	 */
	private void getLatLong(){
		String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "," + city + "," + state + "&key=" + APIKey;
		
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
			
			JSONObject root = new JSONObject(response.toString());
			
			if (root.has("results")){
				JSONArray results = new JSONArray (root.get("results").toString());
	
				JSONObject resultsResults = results.getJSONObject(0);
				
				if(resultsResults.has("geometry")){
					JSONObject geometry = resultsResults.getJSONObject("geometry");
					
					if (geometry.has("location")) {
						JSONObject location = geometry.getJSONObject("location");
						
						if(location.has("lat")){
							lat = location.getDouble("lat");
						}
						if(location.has("lng")){
							lng = location.getDouble("lng");
						}
					}
				}
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
