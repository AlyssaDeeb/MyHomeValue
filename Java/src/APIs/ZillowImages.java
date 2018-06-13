package APIs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Class designated to query the Zillow property API to abstract any provided image URLs for a provided address
 *
 */
public class ZillowImages {
	private final String APIKey = "/* INSERT API HERE */";		// API Key obtained by Zillow
	private String address;										// Street address
	private String city;										// city 
	private String state;										// state (abbreviated)
	
	/**
	 * Constructor provided the String address, city, and state
	 * @param String a (street address)
	 * @param String c (city)
	 * @param String s (state)
	 */
	public ZillowImages(String a, String c, String s){
		address = a.replace(' ','+');		// replaces all spaces with '+' for URL requests
		city = c.replace(' ','+');			// replaces all spaces with '+' for URL requests
		state = s;
	}
	
	/**
	 * Queries the Zillow Property API with the provided address to obtain the Zillow specific zpid key
	 * @return String zpid
	 */
	public String getZillowID() {
		String url = "http://www.zillow.com/webservice/GetSearchResults.htm?zws-id=" + APIKey + "&address=" + address + "&citystatezip=" + city + "%2C+" + state;
		String id = null;
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
			
			id = response.toString().substring((response.toString().indexOf("<zpid>") + 6), response.toString().indexOf("</zpid>"));
			
		} catch (Exception e) {
			System.out.println(e);
				
		}
		return id;
	}
	
	/**
	 * Queries the Zillow property API to obtain URL of property image if one is available, if there is no image provided for the property, assign the URL to the placeholder image
	 * @param String id zpid
	 * @return String image URL
	 */
	public ArrayList<String> getImageURLs(String id) {
		String url = "http://www.zillow.com/webservice/GetUpdatedPropertyDetails.htm?zws-id=" + APIKey + "&zpid=" + id;
		ArrayList<String> urls = new ArrayList<String>();
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
			
			int currentIndex = 0;
		
			while (currentIndex != (response.toString().length()) && currentIndex != -1 && response.toString().indexOf("<url>", currentIndex) != -1){
				urls.add(response.toString().substring(((response.toString().indexOf(("<url>"), currentIndex)) + 5), (response.toString().indexOf("</url>", currentIndex))));
		
				currentIndex = response.toString().indexOf("</url>", currentIndex) + 1;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
		if(urls.isEmpty()){
			urls.add("https://ecowellness.com/wp-content/uploads/2017/04/property.jpg");				// placeholder image from Google
		}
		return urls;
	}
}