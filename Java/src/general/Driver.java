package general;

import database.DBConnection;
import objects.TypeAheadObject;

/**
 * Driver class to facilitate with application testing 
 *
 */
public class Driver {

	/**
	 * Tests typeahead object is functional
	 */
	public void typeahead() {
		TypeAheadObject t = new TypeAheadObject("110 end");
		System.out.println(t.generateJSON().toString());
	}
	/**
	 * Tests database connection
	 */
	public void database() {
		DBConnection d = new DBConnection();
		d.initialize_db_connection();
	
	}

	public static void main(String[] args) {

		Driver d = new Driver();
		d.typeahead();
	}
}
