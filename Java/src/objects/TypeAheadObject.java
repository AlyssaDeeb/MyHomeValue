package objects;

import org.json.JSONArray;
import org.json.JSONException;
import general.AddressProvider;

/**
 * Class to manage the data for the CoreLogic typeahead service
 *
 */
public class TypeAheadObject {
	String data;
	
	/**
	 * Constructor provided the partial input address
	 * @param String partial
	 */
	public TypeAheadObject(String partial) {
		AddressProvider ap = new AddressProvider(partial);
		this.data = ap.generateSuggestions();
	}
	
	/**
	 * Constructor provided the partial input address and dummy data flag
	 * @param String partial
	 * @param boolean dummy
	 */
	public TypeAheadObject(String partial, boolean dummy) {
		
		if (dummy) {
			AddressProvider ap = new AddressProvider(partial, true);
			this.data = ap.generateDummyData();
		}
		else {
			AddressProvider ap = new AddressProvider(partial);
			this.data = ap.generateSuggestions();
		}
	}
	
	/**
	 * Generates JSON object from partially typed address
	 * @return JSONArray results
	 */
	public JSONArray generateJSON() {
		JSONArray result;
		try {
			result = new JSONArray(this.data);
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}