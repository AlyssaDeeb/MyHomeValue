package APIs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.json.Json;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class designated to handle Great Schools API. Gets the name and rating of a single elementary school, 
 * middle school, and high school for a provided address. 
 *
 */
public class GreatSchools {
	private final String APIKey = "/* INSERT API HERE */"; 		//API Key Provided by Great Schools 
	private String address;										// street address
	private String city;										// city
	private String state;										// State (abbreviation)
	private String zip;											// 5 digit zip code
	
	/**
	 * Constructor providing full address
	 * @param a String of street address
	 * @param c String of city
	 * @param s String of abbreviated state
	 * @param z String of 5 digit zip code
	 */
	public GreatSchools(String a, String c, String s, String z){
		address = a.replace(' ','+');		// Replaces all spaces in street address with '+' for URL
		city = c.replace(' ','+');			// Replaces all spaces in city name with '+' for URL
		state = s;
		zip = z;
	}
	
	
	/**
	 * Collect the school name and rating for each of the three school types
	 * @return String of JSON Object
	 */
	public String getSchools() {
		String jsonSchoolString = Json.createObjectBuilder()
				.add("Elementary", getSchoolByType("elementary-schools"))		// Gathers Elementary school information
	    	    .add("Middle", getSchoolByType("middle-schools"))				// Gathers Middle school information
	    	    .add("High", getSchoolByType("high-schools"))					// Gathers High school information
	            .build()
	            .toString();
	
		return jsonSchoolString;	
		
	}
	
	/**
	 * Given the provided school type, calls the Great Schools API and collects the name of the school along with the great schools rating
	 * @param String type of school
	 * @return String of JSON object
	 */
	private String getSchoolByType(String type){
		String url = "https://api.greatschools.org/schools/nearby?key="+ APIKey +"&address=" + address + "&city=" + city + "&state=" + state + "&zip=" + zip + "&schoolType=public&levelCode="+ type + "&limit=1";
		
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
			String name = "";
			String rating = "";
			
			if (response.toString().indexOf("<name>") != -1){
				name = response.toString().substring((response.toString().indexOf("<name>") + 6), (response.toString().indexOf("</name>")));
			}
			if(response.toString().indexOf("<gsRating>") != -1) {
				rating = response.toString().substring((response.toString().indexOf("<gsRating>") + 10), (response.toString().indexOf("</gsRating>")));
			}
			
			String jsonSchoolString = Json.createObjectBuilder()
					.add("Name", name)
		            .add("Rating", rating)
		            .build()
		            .toString();
	
			return jsonSchoolString;	
			
		} catch (Exception e) {
			System.out.println(e);
			return "";
		}
	}
	
	/**
	 * Given the String of the JSON school object, school, and type, takes the string and abstracts the 
	 * appropriate rating for the provided school and type
	 * @param String schoolString
	 * @param String school
	 * @param String type
	 * @return integer rating
	 */
	public static int getSchoolRating(String schoolString, String school, String type){
		int rating = 0;

		try {
			JSONObject schools = new JSONObject(schoolString);
			if(schools.has(school)){
				JSONObject schoolType = new JSONObject(schools.get(school).toString());
				if(schoolType.has(type))
					rating = schoolType.getInt(type);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rating;
	}
	
	/**
	 * Given the String of the JSON school object, school, and type, takes the string and abstracts the 
	 * appropriate description for the provided school and type
	 * @param String schoolString
	 * @param String school
	 * @param String type
	 * @return String description
	 */
	public static String getSchoolDesc(String schoolString, String school, String type){
		
		String desc = "";
		try {
			JSONObject schools = new JSONObject(schoolString);
			if(schools.has(school)){
				JSONObject schoolType = new JSONObject(schools.get(school).toString());
				if(schoolType.has(type))
					desc= schoolType.getString(type);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return desc;
	}

}
