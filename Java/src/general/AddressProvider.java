package general;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Class to handle the CoreLogic typeahead service 
 *
 */
public class AddressProvider {
	
	private final String baseURL = "/* ENTER API URL */";		// CoreLogic TypeAhead API URL
	private String authCode;
	private String partialAddress;
	
	/**
	 * Constructor provided the String of the partial address
	 * @param String partial
	 */
	public AddressProvider(String partial) {
		this.authCode = "Bearer " + this.generateAuthCode();
		this.partialAddress = partial;
	}
	
	/**
	 * Constructor provided the String of the partial address and the dummy data flag
	 * @param String partial
	 * @param Boolean dummy
	 */
	public AddressProvider(String partial, boolean dummy) {
		this.partialAddress = partial;
	}
	
	/**
	 * Generates authentication code to make typeahead request
	 * @return String authentication token
	 */
	private String generateAuthCode() {
		AuthGranter ag = new AuthGranter();
		return ag.getTokenFromJSON();
	}
	
	/**
	 * Make typeahead request
	 * @return String URL response 
	 */
	public String makeRequest() {
		String url;
		try {
			url = this.baseURL + "?input=" + URLEncoder.encode(this.partialAddress, "UTF-8");
			
			SimpleRequest r = new SimpleRequest(url);
			
			r.addHeader("authorization", this.authCode);

			return r.get();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Generate typeahead dummy data
	 * @return String JSON dummy data
	 */
	public String generateDummyData() {
		return "[{'address':'110 WITS END TRL, SENECA, SC 29678','addressLine1':'110 WITS END TRL','city':'SENECA','state':'SC','zip':'29678'},{'address':'110 VANORE RD, WEST END, NC 27376','addressLine1':'110 VANORE RD','city':'WEST END','state':'NC','zip':'27376'},{'address':'110 N END RD, CINCINNATUS, NY 13040','addressLine1':'110 N END RD','city':'CINCINNATUS','state':'NY','zip':'13040'}]";
	}
	
	/**
	 * Generate suggested list of addresses provided by the CoreLogic typeahead service
	 * @return
	 */
	public String generateSuggestions() {
		String data = this.makeRequest();
		JSONArray suggestions = new JSONArray();
		try {
			JSONObject returned = new JSONObject(data);
			for (int i=0; i<returned.getJSONArray("results").length(); i++) {
				JSONObject j = new JSONObject(returned.getJSONArray("results").get(i).toString());
				if (!j.getString("address").contains("null")) {
					suggestions.put(j);
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return suggestions.toString();
	}

}