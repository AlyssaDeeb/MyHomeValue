package objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.json.Json;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import APIs.GreatSchools;
import APIs.WalkScore;
import APIs.ZillowImages;
import general.DataProvider;

/**
 * Class to manage all of the fields of a real estate property 
 *
 */
public class PropertyObject {
	
	private String propertyID;
	private String addressLine;  //Summary API
	private String city;         //Summary API
	private String state;        //Summary API
	private String zip;          //Summary API
	private int value;           //Prospector AVM API
	private String type;         //Site API
	private int yearBuilt;       //Building API
	private int bedrooms;        //Building API
	private double bathrooms;    //Building API
	private int squareFeet;      //Building API
	private double acres;        //Site API
	private int stories;         //Building API
	private int parkingSpaces;	 //Building API
	private double latitude;     //Property API
	private double longitude;    //PropertyAPI
	private int soldYear;
	private int soldValue;       
	private String valueChangePercentage;
	private String schoolRatings;	// GreatSchools API
	private String walkScore;		// WalkScore API
	
	private JSONArray comparables;
	private ArrayList<String> images = new ArrayList<String>();	//ZillowImages API
	
	/**
	 * Constructor that populates the property data by calling all associated APIs
	 * @param String corelogicID
	 */
	public PropertyObject(String corelogicID) {
		this.propertyID = corelogicID;
		this.fillData();
	}
	
	/**
	 * Constructor that populates the property data with the dummy data provide, 
	 * it does not call CoreLogic APIs but still calls other external APIs
	 * @param String corelogicID
	 * @param Boolean dummy flag 
	 */
	public PropertyObject(String corelogicID, boolean dummy) {
		this.propertyID = corelogicID;
		if (dummy) {
			this.generateDummyData();
		}
		else {
			this.fillData();
		}
	}
	
	/**
	 * Constructor that populates the property data with the provided JSON String
	 * @param String corelogicID
	 * @param String jsonData
	 */
	public PropertyObject(String corelogicID, String jsonData, JSONArray comparablesJsonData) {
		this.propertyID = corelogicID;
		
		comparables = comparablesJsonData;
		fillFromJSON(jsonData);
	}
	
	public String getID(){
		return this.propertyID;
	}
	
	/**
	 * Generates and returns full address
	 * Includes street address, city, state, and zip
	 * @return String address
	 */
	public String getAddress(){
		return this.addressLine + ", " + this.city + ", " + this.state + ", " + this.zip; 
	}
	
	/**
	 * Formats value of house to display dollar sign
	 * @return
	 */
	public String getFormattedValue(){
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		return formatter.format(this.value);
	}
	
	/**
	 * Get the number of beds
	 * @return int beds
	 */
	public int getBeds(){
		return this.bedrooms;
	}
	
	/**
	 * Get the number of baths
	 * @return double baths
	 */
	public double getBaths(){
		return this.bathrooms;
	}
	
	/**
	 * Get the house square feet
	 * @return int squareFoot
	 */
	public String getSqft(){
		return Integer.toString(this.squareFeet);
	}
	
	/**
	 * Gets first image for property
	 * @return String image url
	 */
	public String getFirstImage(){
		return images.get(0);
	}
	
	/**
	 * Gets JSONArray of comparable homes
	 * @return JSONArray comparables
	 */
	public JSONArray getComparables(){
		return comparables;
	}
	
	/**
	 * Calculates the percentage that a house has appreciated since last purchase date and amount
	 */
	public void calculateGrowth(){
		double growth = ((double)(this.value - this.soldValue) / this.soldValue) * 100;

		DecimalFormat df = new DecimalFormat("##.##");
	
		this.valueChangePercentage = df.format(growth);
	}
	
	/**
	 * Populates the property object with information from all CoreLogic APIs and external APIs
	 */
	private void fillData() {
		DataProvider dp = new DataProvider(this.propertyID);
		try {
			JSONObject buildingData = new JSONObject(dp.getBuildingData());
			this.yearBuilt = buildingData.getInt("yearBuilt");
			this.bedrooms = buildingData.getInt("bedrooms");
			this.bathrooms = buildingData.getInt("fullBaths") + buildingData.getInt("halfBaths")*.5;
			this.stories = buildingData.getInt("stories");
			this.parkingSpaces = buildingData.getInt("parkingSpaces");
			
			JSONObject summaryData = new JSONObject(dp.getSummaryData());
			this.addressLine = summaryData.getString("address");
			this.city = summaryData.getString("city");
			this.state = summaryData.getString("state");
			this.zip = summaryData.getString("zip5");
			this.squareFeet = summaryData.getInt("buildingSquareFeet");
			
			JSONObject siteData = new JSONObject(dp.getSiteData());
			this.acres = siteData.getDouble("acres");
			this.type = siteData.getString("houseType");
			
			JSONObject propertyData = new JSONObject(dp.getPropertyData());
			this.latitude = propertyData.getDouble("latitude");
			this.longitude = propertyData.getDouble("longitude");
			
			this.value = dp.getValue();
			
			JSONObject saleData = new JSONObject(dp.getSaleData());
			this.soldYear = saleData.getInt("soldyear");
			this.soldValue = saleData.getInt("soldvalue");
			
			this.comparables = dp.getComparables(city, state);
			
			ZillowImages z = new ZillowImages(addressLine, city, state);
			this.images = new ArrayList<String>();
			this.images  = z.getImageURLs(z.getZillowID());
			
			GreatSchools gs = new GreatSchools(addressLine, city, state, zip);
			this.schoolRatings = gs.getSchools();
			
			WalkScore ws = new WalkScore(addressLine, city, state, zip, latitude, longitude);
			this.walkScore = ws.getTransitScores();
			
			this.calculateGrowth();
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Assigns dummy data to property for testing purposes
	 * Does not call CoreLogic APIs
	 */
	public void generateDummyData() {
		this.addressLine = "311 Kings Rd";
		this.city = "Newport Beach";
		this.state = "CA";
		this.zip = "92663";
		this.value = 6335000;
		this.type = "SINGLE FAM RES";
		this.yearBuilt = 2001;
		this.bedrooms = 5;
		this.bathrooms = 5.5;
		this.squareFeet = 6944;
		this.acres = 0.27;
		this.stories = 2;
		this.parkingSpaces = 2;
		this.latitude = 33.617149;
		this.longitude = -117.908468;
		this.soldValue = 6000000;
		this.soldYear = 2018;
		this.calculateGrowth();
		
		try {
			String c = "/* ENTER LIST OF COMPARABLE HOMES */";
			this.comparables = new JSONArray(c);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ZillowImages z = new ZillowImages(addressLine, city, state);
		this.images = new ArrayList<String>();
		this.images  = z.getImageURLs(z.getZillowID());
		
		GreatSchools gs = new GreatSchools(addressLine, city, state, zip);
		this.schoolRatings = gs.getSchools();
		
		WalkScore ws = new WalkScore(addressLine, city, state, zip, latitude, longitude);
		this.walkScore = ws.getTransitScores();
		
	}
	
	/**
	 * Reconstructs property object from a JSON data string
	 * @param String jsonString
	 */
	private void fillFromJSON(String jsonString) {

		try {
			JSONObject root = new JSONObject(jsonString);
			
			this.yearBuilt = root.getInt("yearBuilt");
			this.bedrooms = root.getInt("bedrooms");
			this.bathrooms = root.getDouble("bathrooms");
			this.stories = root.getInt("stories");
			this.parkingSpaces = root.getInt("parkingSpaces");
			this.addressLine = root.getString("addressLine");
			this.city = root.getString("city");
			this.state = root.getString("state");
			this.zip = root.getString("zip");
			this.squareFeet = root.getInt("squareFeet");
			this.acres = root.getDouble("acres");
			this.type = root.getString("type");
			this.latitude = root.getDouble("latitude");
			this.longitude = root.getDouble("longitude");	
			this.value = root.getInt("value");	
			this.soldYear = root.getInt("soldYear");
			this.soldValue = root.getInt("soldValue");
			
			ZillowImages z = new ZillowImages(addressLine, city, state);
			this.images = new ArrayList<String>();
			this.images  = z.getImageURLs(z.getZillowID());
			
			GreatSchools gs = new GreatSchools(addressLine, city, state, zip);
			this.schoolRatings = gs.getSchools();
			
			WalkScore ws = new WalkScore(addressLine, city, state, zip, latitude, longitude);
			this.walkScore = ws.getTransitScores();
			
			this.calculateGrowth();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Generated JSON object string of all provided property information
	 * @return String: JSON property data
	 */
	public String generateJSONString(boolean isSaved) {
		JSONObject result = new JSONObject();

		try {
			result.put("id", this.propertyID); 
			result.put("addressLine", this.addressLine);
			result.put("city", this.city);
			result.put("state", this.state);
			result.put("zip", this.zip);
			result.put("value", this.value);
			result.put("type", this.type);
			result.put("yearBuilt", this.yearBuilt);
			result.put("bedrooms", this.bedrooms);
			result.put("bathrooms", this.bathrooms);
			result.put("squareFeet", this.squareFeet);
			result.put("acres", this.acres);
			result.put("stories", this.stories);
			result.put("parkingSpaces", this.parkingSpaces);
			result.put("latitude", this.latitude);
			result.put("longitude", this.longitude);
			
			result.put("soldValue", this.soldValue);
			result.put("soldYear", this.soldYear);
			result.put("percentageChange", this.valueChangePercentage);
			
			result.put("comparables", this.comparables);
			
			for(String url: images) {
				result.put("url",url);
			}
			
			result.put("schools", this.schoolRatings);
			result.put("walkscore", this.walkScore);
				
			result.put("soldYear", soldYear);
			result.put("soldValue", soldValue);
			result.put("growthRate", valueChangePercentage);
			
			result.put("homeSaved", isSaved);

			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String resultString = result.toString().replace("\\", "").replace("\"{", "{").replace("}\"", "}");
		return resultString;
	}
	
	/**
	 * Generates JSON string of property details from database query result set
	 * @param ResultSet results
	 * @return String: JSON property data
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	public static String generateDatabaseJSONString(ResultSet results, JSONArray comparables, Boolean isSaved) throws NumberFormatException, SQLException {
		if (results.next()) {
			
			JSONObject result = new JSONObject();

			try {
				
				result.put("id", results.getString("home_id"));
				result.put("addressLine", results.getString("addressLine"));
				result.put("city", results.getString("city"));
				result.put("state", results.getString("state"));
				result.put("zip", results.getString("zip"));
				result.put("value", results.getInt("value"));
				result.put("type", results.getString("type"));
				result.put("yearBuilt", results.getInt("yearBuilt"));
				result.put("bedrooms", results.getInt("bedrooms"));
				result.put("bathrooms", results.getDouble("bathrooms"));
				result.put("squareFeet", results.getInt("squareFeet"));
				result.put("acres", results.getDouble("acres"));
				result.put("stories", results.getInt("stories"));
				result.put("parkingSpaces", results.getInt("parkingSpaces"));
				result.put("latitude", results.getDouble("latitude"));
				result.put("longitude", results.getDouble("longitude"));
				result.put("url", results.getString("url"));
				result.put("soldYear", results.getInt("soldYear"));
				result.put("soldValue", results.getInt("soldValue"));
				result.put("growthRate", Double.parseDouble((results.getString("growthRate"))));
				JSONObject schoolResult = new JSONObject();
				try{
					JSONObject eleSchool = new JSONObject();
					try{
						eleSchool.put("Rating", results.getInt("eleSchoolRating"));
						eleSchool.put("Name", results.getString("eleSchoolName"));
					}catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					schoolResult.put("Elementary", eleSchool);
				}catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try{
					JSONObject midSchool = new JSONObject();
					try{
						midSchool.put("Rating", results.getInt("midSchoolRating"));
						midSchool.put("Name", results.getString("midSchoolName"));
					}catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					schoolResult.put("Middle", midSchool);
				}catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try{
					JSONObject highSchool = new JSONObject();
					try{
						highSchool.put("Rating", results.getInt("highSchoolRating"));
						highSchool.put("Name", results.getString("highSchoolName"));
					}catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					schoolResult.put("High", highSchool);
				}catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			result.put("schools", schoolResult);
				JSONObject walkResult = new JSONObject();
					try{
						walkResult.put("walkscore", results.getInt("walkscore"));
						walkResult.put("walkscore_desc", results.getString("walkscore_desc"));
						walkResult.put("transitscore", results.getInt("transitscore"));
						walkResult.put("transitscore_desc", results.getString("transitscore_desc"));
					}catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				result.put("walkscore", walkResult);
				result.put("comparables",comparables);
				result.put("homeSaved", isSaved);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String resultString = result.toString().replace("\\", "").replace("\"{", "{").replace("}\"", "}");
			return resultString;
		}
		return "";
	}
	
	/**
	 * Generates JSON Array for all comparable homes from the MySQL ResultSet
	 * Each comparable has a street address, price, square feet, bedrooms, bathrooms, latitude, and longitude 
	 * @param results
	 * @return
	 * @throws SQLException
	 */
	public static JSONArray generateComparablesJSONArray(ResultSet results) throws SQLException {
		JSONArray comparables = new JSONArray();
		
		while(results.next()) {
			
			String node = Json.createObjectBuilder()
					.add("shortAddress", results.getString("shortAddress"))
					.add("price", results.getInt("price"))
					.add("squareFeet", results.getInt("squareFeet"))
					.add("bedrooms", results.getInt("bedrooms"))
					.add("bathrooms", results.getInt("bathrooms"))
					.add("lat", results.getDouble("lat"))
					.add("long", results.getDouble("long"))
					.build()
					.toString();
			comparables.put(node);
			
		}
		return comparables;
	}
	
	/**
	 * Generates query string to insert home into database cache with all associated property details
	 * @return Query String
	 */
	public String generateQuery() {
		
		String query = "INSERT INTO `homes_cache`(`home_id`,`addressLine`,`city`,`state`,`zip`,`value`,`type`,`yearBuilt`,`bedrooms`,`bathrooms`,"
				+ "`squareFeet`,`acres`,`stories`,`parkingSpaces`, `latitude`,`longitude`,`url`,`soldYear`,`soldValue`,`growthRate`,`eleSchoolRating`,`eleSchoolName`,"
				+ "`midSchoolRating`,`midSchoolName`,`highSchoolRating`,`highSchoolName`,`walkscore`,`walkscore_desc`,`transitscore`,`transitscore_desc`) VALUES(\""
				+ propertyID + "\",\"" + addressLine + "\",\"" + city  + "\",\"" + state  + "\",\"" + zip  + "\"," + value  + ",\"" + type  + "\"," + yearBuilt  + "," + bedrooms  + "," 
				+ bathrooms  + "," + squareFeet  + "," + acres  + "," + stories + "," + parkingSpaces + "," + latitude  + "," + longitude  + ",\"" + images.get(0) +  "\"," + soldYear  + "," + soldValue  + ",\"" 
				+ valueChangePercentage  + "\"," + GreatSchools.getSchoolRating(schoolRatings, "Elementary", "Rating")  + ",\"" + GreatSchools.getSchoolDesc(schoolRatings, "Elementary", "Name")  + "\"," 
				+ GreatSchools.getSchoolRating(schoolRatings, "Middle", "Rating")  + ",\"" + GreatSchools.getSchoolDesc(schoolRatings, "Middle", "Name")   + "\"," + 
				+ GreatSchools.getSchoolRating(schoolRatings, "High", "Rating")  + ",\"" + GreatSchools.getSchoolDesc(schoolRatings, "High", "Name") + "\"," + WalkScore.getWalkRating(walkScore, "walkscore")  
				+ ",\"" + WalkScore.getWalkDesc(walkScore, "walkscore_desc") + "\"," + WalkScore.getWalkRating(walkScore, "transitscore")  + ",\"" + WalkScore.getWalkDesc(walkScore, "transitscore_desc")
				+ "\")";
		
		return query;
	}
	
	/**
	 * Generates query string to insert a single comparable home into database cache with all associated property details
	 * @return Query String
	 */
	public String generateComparableQuery(String homeID, int index) {
		String query = "";
		
		JSONArray comparables;
		
		try {
			comparables = new JSONArray(getComparables().toString().replace("\\", "").replace("\"{", "{").replace("}\"", "}"));
		
			JSONObject currentComparable = comparables.getJSONObject(index);
			query = "INSERT INTO `comparables_cache`(`home_id`, `shortAddress`, `price`, `squareFeet`, `bedrooms`, `bathrooms`, `lat`, `long`) VALUES(\"" + homeID + "\", \"" + currentComparable.getString("shortAddress") + "\", "
					+ currentComparable.getInt("price") + "," + currentComparable.getInt("squareFeet") + "," + currentComparable.getInt("bedrooms") + "," + currentComparable.getInt("bathrooms") 
					+ "," +currentComparable.getDouble("lat") + "," +currentComparable.getDouble("long") + ")";
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return query;
	}
}
