package general;

import org.json.JSONObject;
import APIs.GoogleGeocode;
import APIs.ZillowImages;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 * Class to handle the collection of data from the various CoreLogic APIs
 *
 */
public class DataProvider {
	
	private final String baseURL = "/* Enter API URL */"; // URL for CoreLogic APIs
	private String authCode;
	private String propertyID;
	private boolean dummy = false;
	
	/*
	 * ownership
	 * building
	 * foreclosure
	 * tax-assessment
	 * avm
	 * 	vp4
	 * 	pass
	 * 	hpa
	 * 	pb6
	 * 	vectorgeocore
	 * 	avmx
	 * 	vectoravmax
	 * sales-history
	 * finance-history
	 * site
	 * summary
	 * location
	 * comparables
	 * 
	 */
	
	/**
	 * Default constructor
	 */
	public DataProvider(){	
		this.authCode = "Bearer " + this.generateAuthCode();
	}
	
	/**
	 * Constructor with the dummy data signal
	 * @param Boolean dummy flag
	 */
	public DataProvider(boolean dummy){
		this.dummy = dummy;
	}
	
	/**
	 * Constructor provided String of CoreLogic property ID
	 * @param String CoreLogic propertyID
	 */
	public DataProvider(String propertyID) {
		this.authCode = "Bearer " + this.generateAuthCode();
		this.propertyID = propertyID;
	}
	
	/**
	 * Generates CoreLogic API authentication code
	 * @return String of authentication token
	 */
	private String generateAuthCode() {
		AuthGranter ag = new AuthGranter();
		
		return ag.getTokenFromJSON();
	}
	
	/**
	 * Make SimpleRequest for provided info type
	 * @param String infotype
	 * @return String URL response
	 */
	public String makeRequest(String infotype) {
		
		String url = this.baseURL + "/" + this.propertyID + "/" + infotype;
		SimpleRequest r = new SimpleRequest(url);
		
		r.addHeader("authorization", this.authCode);

		return r.get();
	}
	
	/**
	 * Make SimpleRequest for provided info type
	 * @param String infotype
	 * @param int timeout
	 * @return String URL response
	 */
	public String makeRequest(String infotype, int timeout) {
		
		String url = this.baseURL + "/" + this.propertyID + "/" + infotype;
		SimpleRequest r = new SimpleRequest(url, timeout);
		
		r.addHeader("authorization", this.authCode);

		return r.get();
	}
	
	/**
	 * Given the provided street address and zip code, obtain the CoreLogic ID for that property
	 * @param String addressLine
	 * @param String zipCode
	 * @return String CoreLogic ID
	 */
	public String getCoreLogicID(String addressLine, String zipCode) {
		
		if (!this.dummy) {
			addressLine = addressLine.replaceAll(" ", "%20");
			String url = this.baseURL + "?address=" + addressLine + "&zip5=" + zipCode;
			
			SimpleRequest r = new SimpleRequest(url);
			r.addHeader("authorization", this.authCode);
			
			System.out.println("DataProvider: getCoreLogicID: " + addressLine );
			
			try {
				JSONObject d = new JSONObject(r.get());
				String id = d.getJSONArray("data").getJSONObject(0).getString("corelogicPropertyId");
				return id;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			return null;
		}
		else {
			return "06059:1624301";
		}
	}
	
	
	
	/**************************
	 ****** Data Section ******
	**************************/
	
	/**
	 * Makes Ownership Data request
	 * @return String URL response
	 */
	public String getOwnershipData() {
		return this.makeRequest("ownership");
	}
	
	/**
	 * Makes a building data request and obtains the number of stories, year built, number of parking spaces, 
	 * and number of bathrooms
	 * @return String JSON object
	 */
	public String getBuildingData() {
		String building = this.makeRequest("building");	
		 	
		int stories = 0, yearBuilt = 0, bedrooms= 0, parkingSpaces = 0, fullBaths= 0, halfBaths = 0;
		
		try {
			JSONObject root = new JSONObject(building);
			if (root.has("stories") && !root.isNull("stories") && !root.get("stories").equals(""))
				 stories = root.getInt("stories");
			if (root.has("yearBuilt") && !root.isNull("yearBuilt") && !root.get("yearBuilt").equals(""))
				yearBuilt = root.getInt("yearBuilt");
			if (root.has("bedrooms") && !root.isNull("bedrooms") && !root.get("bedrooms").equals(""))
				bedrooms = root.getInt("bedrooms");
			if (root.has("parkingSpaces") && !root.isNull("parkingSpaces") && !root.get("parkingSpaces").equals(""))
				parkingSpaces = root.getInt("parkingSpaces");
			if (root.has("fullBaths") && !root.isNull("fullBaths") && !root.get("fullBaths").equals(""))
				fullBaths = root.getInt("fullBaths");
			if (root.has("halfBaths") && !root.isNull("halfBaths") && !root.get("halfBaths").equals(""))
				halfBaths = root.getInt("halfBaths");
		
			
			building = Json.createObjectBuilder()
					.add("stories", stories)
					.add("yearBuilt", yearBuilt)
					.add("bedrooms", bedrooms)
					.add("parkingSpaces", parkingSpaces)
					.add("fullBaths", fullBaths)
					.add("halfBaths", halfBaths)					
					.build()
					.toString();				
				
			return building;
		} catch (JSONException e) {			
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Makes foreclosure request
	 * @return String URL response
	 */
	public String getForeclosureData() {
		return this.makeRequest("foreclosure");
	}
	
	/**
	 * Makes tax assessment request 
	 * @return String URL response
	 */
	public String getTaxAssessmentData() {
		return this.makeRequest("tax-assessment");
	}
	
	/**
	 * Makes sales history request 
	 * @return String URL response
	 */
	public String getSalesHistoryData() {
		return this.makeRequest("sales-history");
	}
	
	/**
	 * Makes finance history request 
	 * @return String URL response
	 */
	public String getFinanceHistoryData() {
		return this.makeRequest("finance-history");
	}
	
	/**
	 * Makes a site data request and obtains the property acres and property type
	 * @return String JSON object
	 */
	public String getSiteData() {
		String site = this.makeRequest("site");
		try {
			double acres = 0.0;
			String houseType = "";
			
			JSONObject root = new JSONObject(site);
			if (root.has("acres") && !root.isNull("acres") && !root.get("acres").equals(""))
				acres = root.getDouble("acres");
			if (root.has("countyUse") && !root.isNull("countyUse") && !root.get("countyUse").equals(""))
				houseType = root.getString("countyUse");
			
			site = Json.createObjectBuilder()
					.add("acres", acres)
					.add("houseType", houseType)
					.build()
					.toString();				
				
			return site;
		}
		catch (JSONException e) {			
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Makes a prospector AVM request and obtains the property value
	 * @return String JSON object
	 */
	public int getValue() {
		//Uses Prospector AVM
		String avm = this.makeRequest("avm/Prospector");
		try {
			JSONObject root = new JSONObject(avm);
			int value = 0;
			
			if (root.has("value") && !root.isNull("value") && !root.get("value").equals(""))
				value = root.getInt("value");
			return value;
		}
		catch (JSONException e) {			
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Makes a site data request and obtains the property acres and property type
	 * @return String JSON object
	 */
	public String getSaleData() {
		String lms = this.makeRequest("last-market-sale");
		int soldvalue = 0, soldyear = 0;
		try {
			JSONObject root = new JSONObject(lms);
			if (root.has("salePrice") && !root.isNull("salePrice") && !root.get("salePrice").equals(""))
				soldvalue = root.getInt("salePrice");
			if (root.has("saleDate") && !root.isNull("saleDate") && !root.get("saleDate").equals(""))
				soldyear = Integer.parseInt(root.getString("saleDate").substring(0, 4));
			
			lms = Json.createObjectBuilder()
					.add("soldvalue", soldvalue)
					.add("soldyear", soldyear)
					.build()
					.toString();
			
			return lms;
		}
		catch (JSONException e) {			
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Makes comparables request to obtain the information on the 10 nearby homes
	 * Inputs the street address, sale price, square feet, number of bedrooms, and number of bathrooms 
	 * @param String city
	 * @param String state(abbreviated)
	 * @return JSONArray of comparables
	 */
	public JSONArray getComparables(String city, String state) {
	String comparables = this.makeRequest("comparables", 3000);
		try {
			
			JSONObject root = new JSONObject(comparables);
			JSONArray cmp = root.getJSONArray("comparables");
			JSONArray homes = new JSONArray();
			for (int i=0; i<cmp.length(); i++) {
				
				JSONObject home = new JSONObject(cmp.get(i).toString());
				
				String streetAddress = "";
				int salePrice = 0, sqft = 0, bed = 0, bath = 0;
				
				if (home.has("streetAddress") && !home.isNull("streetAddress") && !home.get("streetAddress").equals(""))
					streetAddress = home.getString("streetAddress");
				if (home.has("salePrice") && !home.isNull("salePrice") && !home.get("salePrice").equals(""))
					salePrice = home.getInt("salePrice");
				if (home.has("buildingSquareFeet") && !home.isNull("buildingSquareFeet") && !home.get("buildingSquareFeet").equals(""))
					sqft = home.getInt("buildingSquareFeet");
				if (home.has("bedrooms") && !home.isNull("bedrooms") && !home.get("bedrooms").equals(""))
					bed = home.getInt("bedrooms");
				if (home.has("baths") && !home.isNull("baths") && !home.get("baths").equals(""))
					bath = home.getInt("baths");
				
				GoogleGeocode gg = new GoogleGeocode(home.getString("streetAddress"), city, state);
				String node = Json.createObjectBuilder()
						.add("shortAddress", streetAddress )
						.add("price", salePrice)
						.add("squareFeet", sqft)
						.add("bedrooms", bed)
						.add("bathrooms", bath)
						.add("lat", gg.getLat())
						.add("long", gg.getLong())
						.build()
						.toString();
				homes.put(node);
			}
			
			return homes;
		}
		catch (JSONException e) {			
			e.printStackTrace();
		}
		return new JSONArray();
	}
	
	/**
	 * Makes a summary request to obtain the address, city, state, zip code, number of bedrooms, number of bathrooms, and the square footage of the property
	 * @return String JSON Object
	 */
	public String getSummaryData() {
		String summary = this.makeRequest("summary");
		String address = "", city = "", state = "", zip5 = "";				
		int buildingSquareFeet = 0, bedrooms = 0, baths = 0;		
		
		try {
			JSONObject root = new JSONObject(summary);
			if (root.has("address") && !root.isNull("address") && !root.get("address").equals(""))
				address = root.getString("address");
			if (root.has("city") && !root.isNull("city") && !root.get("city").equals(""))
				city = root.getString("city");
			if (root.has("state") && !root.isNull("state") && !root.get("state").equals(""))
				state = root.getString("state");
			if (root.has("zip5") && !root.isNull("zip5") && !root.get("zip5").equals(""))
				zip5 = root.getString("zip5");
			if (root.has("bedrooms") && !root.isNull("bedrooms") && !root.get("bedrooms").equals(""))
				bedrooms = root.getInt("bedrooms");
			if (root.has("baths") && !root.isNull("baths") && !root.get("baths").equals(""))
				baths = root.getInt("baths");
			if (root.has("buildingSquareFeet") && !root.isNull("buildingSquareFeet") && !root.get("buildingSquareFeet").equals(""))
				buildingSquareFeet = root.getInt("buildingSquareFeet");
			
			summary = Json.createObjectBuilder()
					.add("address", address)
					.add("city", city)
					.add("state", state)
					.add("zip5", zip5)
					.add("bedrooms", bedrooms)
					.add("baths", baths)
					.add("buildingSquareFeet", buildingSquareFeet)
					.build()
					.toString();				
				
			return summary;
		} catch (JSONException e) {			
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Makes location request to obtain the county, state, and school district for the property
	 * @return String JSON Object
	 */
	public String getLocationData() {
		String location = this.makeRequest("location");
		String county = "", state ="", schoolDistrict = "";
		
		try {
			JSONObject root = new JSONObject(location);
			if (root.has("county") && !root.isNull("county") && !root.get("county").equals(""))
				county = root.getString("county");
			if (root.has("state") && !root.isNull("state") && !root.get("state").equals(""))
				state = root.getString("state");
			if (root.has("schoolDistrict") && !root.isNull("schoolDistrict") && !root.get("schoolDistrict").equals(""))
				schoolDistrict = root.getString("schoolDistrict");
			
			
			location = Json.createObjectBuilder()
					.add("county", county)
					.add("state", state)
					.add("schoolDistrict", schoolDistrict)			
					.build()
					.toString();				
				
			return location;
		} catch (JSONException e) {			
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Makes property request to obtain the latitude and longitude of the property
	 * @return
	 */
	public String getPropertyData() {
		String url = baseURL + "/property/" + this.propertyID;
		SimpleRequest r = new SimpleRequest(url);
			
		r.addHeader("authorization", this.authCode);
		
		String property = r.get();
		
		double latitude = 0.0, longitude = 0.0;
		
		try {
			JSONObject root = new JSONObject(property);
			if (root.has("latitude") && !root.isNull("latitude") && !root.get("latitude").equals(""))
				latitude = root.getDouble("latitude");
			if (root.has("longitude") && !root.isNull("longitude") && !root.get("longitude").equals(""))
				longitude = root.getDouble("longitude");
		
			property = Json.createObjectBuilder().add("latitude", latitude).add("longitude", longitude).build().toString();				
			return property;
			
		} catch (JSONException e) {			
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * Makes a request to the Zillow Images API to add the image URL to the property information
	 * @param String addressLine 
	 * @param String city
	 * @param String state (abbreviation)
	 * @return
	 */
	public String getImageData(String addressLine, String city, String state) {
		
		ZillowImages z = new ZillowImages(addressLine, city, state);
		String property = "";
		
		ArrayList<String> urls  = z.getImageURLs(z.getZillowID());
		JsonObjectBuilder builder = Json.createObjectBuilder();
		
		for(String images: urls) {
			  builder.add("imageURL", images);
		}
								
		property = builder.build().toString();
		
		return property;
	}
}