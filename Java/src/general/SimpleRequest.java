package general;

import java.io.IOException;
import org.apache.http.client.fluent.Request;

/**
 * Class to handle requests to the CoreLogic APIs
 *
 */
public class SimpleRequest {
	String URL = "";
	String headerKey = "";
	String headerValue = "";
	int timeout = 1000;
	
	/**
	 * Constructor provided the String of the URL
	 * @param String url
	 */
	public SimpleRequest(String url) {
		this.URL = url;
	}
	
	/**
	 * Constructor provided the String of the URL and the integer timeout
	 * @param String url
	 * @param int timeout
	 */
	public SimpleRequest(String url, int timeout) {
		this.URL = url;
		this.timeout = timeout;
	}
	
	/**
	 * Adds key and value to the request header
	 * @param String key
	 * @param String value
	 */
	public void addHeader(String key, String value) {
		this.headerKey = key;
		this.headerValue = value;
	}
	
	/**
	 * Makes a get request to the provided URL with the added header and key
	 * @return String of response
	 */
	public String get() {

		for (int i=0; i<3; i++) {
			try {
				System.out.println("MAKING A GET REQUEST");
				return Request.Get(this.URL)
				        .connectTimeout(1000)
				        .socketTimeout(this.timeout)
				        .addHeader(this.headerKey, this.headerValue)
				        .execute().returnContent().toString();
			} catch (IOException e) {
				System.out.println("Attempt " + i + " failed. Please retry");
			}
		}

		return null;
	}
	
	/**
	 * Makes a post request to the provided URL with the added header and key
	 * @return String of reponse
	 */
	public String post() {
		System.out.println("MAKING A POST REQUEST");

		try {
			return Request.Post(this.URL)
			        .connectTimeout(1000)
			        .socketTimeout(1000)
			        .addHeader(this.headerKey, this.headerValue)
			        .execute().returnContent().toString();
		} catch (IOException e) {
			System.out.println("Please retry");
			e.printStackTrace();
		}

		return null;
	}
}
