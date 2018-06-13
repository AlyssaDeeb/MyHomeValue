package general;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class to manage the authentication of the CoreLogic API credentials 
 *
 */
public class AuthGranter {
	
	private final String URL = "/* ENTER AUTHENTICATION URL */"; 		// URL for CorLogic APIs
	private final String CLIENT_KEY = "/* ENTER CLIENT KEY*/";			// CoreLogic Client Key
	private final String CLIENT_SECRET = "/* ENTER SECRET KEY */";		// CoreLogic Secret Key
	private String fullClientString;
	
	/**
	 * Default constructor 
	 */
	public AuthGranter() {
		this.fullClientString = this.CLIENT_KEY + ":" + this.CLIENT_SECRET;
	}
	
	/**
	 * Encode the client string to base 64
	 * @return
	 */
	public String encode_base64() {
		return Base64.getEncoder().encodeToString(this.fullClientString.getBytes(StandardCharsets.UTF_8));
	}
	
	/**
	 * Get the access token from the the JSON object 
	 * @return
	 */
	public String getTokenFromJSON() {
		try {
			JSONObject root = new JSONObject(getJSON());
			return root.getString("access_token");
		} catch (JSONException e) {			
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Make authorization request to the CoreLogic API
	 * @return String JSON object
	 */
	public String getJSON() {
		SimpleRequest sr = new SimpleRequest(this.URL);
		sr.addHeader("Authorization", this.encode_base64());
		return sr.post();
	}
	
}
