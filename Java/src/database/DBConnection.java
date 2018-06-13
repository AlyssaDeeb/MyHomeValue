package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;
import javax.json.Json;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import objects.PropertyObject;

/**
 * Class to manage the MySQL database connection and all associated queries 
 *
 */
public class DBConnection {
	private final String user_name = "/* INSERT USER NAME */";		// MySQL login name
	private final String password = "/* INSERT PASSWORD */";		// MySQL password
	private final String port = "/* INSERT PORT */";				// Port  
	private final String host = "/* INSERT ENDPOINT */"; 			// endpoint host address
	private final String dbName = "/* INSERT DATABASE NAME */";		// database name

	/**
	 * Initializes the connection with the AWS RDS MySQL Database
	 * Throws various errors depends on exception
	 * @return Connection to database
	 */
	public final Connection initialize_db_connection() {
		Connection connection = null;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + dbName +"?user=" + this.user_name + "&password=" + this.password);
		
		} catch (InstantiationException e) {
			System.out.println("Unable to create a JDBC instance");
			e.printStackTrace();
		
		} catch (IllegalAccessException e) {
			System.out.println("Unable to access JDBC driver");
			e.printStackTrace();
		
		} catch (ClassNotFoundException e) {
			System.out.println("Unable to find definition for JDBC");
			e.printStackTrace();
		
		} catch (SQLException e) {
			System.out.println("Unable to connect to database: " + e.getMessage());
		}
		
		return connection;
	}
	
	/**
	 * Queries the database to check if a user matches the provided email. 
	 * @param String email
	 * @return Boolean query value
	 */
	protected final boolean userExists(String email) {

		try (Connection connection = initialize_db_connection()) {
			String stmt_exists_user = "SELECT id FROM user WHERE email = ?";

			try (PreparedStatement get_user = connection.prepareStatement(stmt_exists_user)) {
				get_user.setString(1, email);				// email

				try (ResultSet result_user = get_user.executeQuery()) {
					return result_user.isBeforeFirst();		// returns if the query result is within bounds
				}
			
			} catch (SQLException e) {
				System.out.println("Unable to authenticate login credentials");
				e.printStackTrace();
			}
			connection.close();
		} catch (SQLException e1) {
			System.out.println("Unable to connect to database");
			e1.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Creates a new user in the database. 
	 * All values for the user are initially set to unknown. User ID will be auto incremented in the MySQL database.
	 */
	public final void newUser(String ipAddress)  {
		String queryStatement = "INSERT INTO user(`email`, `password`, `first_name`, `last_name`, `ip_address`) VALUES(?, ?, ?, ?, ?)";

		try (Connection conn = initialize_db_connection()) {
			try (PreparedStatement addUser = conn.prepareStatement(queryStatement)) {
				addUser.setString(1, "unknown");			// email
				addUser.setString(2, "unknown");			// password
				addUser.setString(3, "unknown");			// first_name
				addUser.setString(4, "unknown");			// last_name
				addUser.setString(5, ipAddress);			// ip_address
				 		

				addUser.executeUpdate();
			}
			System.out.println("User Added!");
			conn.close();
		} catch (SQLException e) {
			System.out.println("Unable to add new user");
		}
	}
	
	/**
	 * Gets the ID of the last user inserted into the user table within the database.
	 * @return int ID
	 */
	public final int lastInserted(){
		int id = 0;
		
		try (Connection conn = initialize_db_connection()) {
			String queryStatement = "SELECT id FROM user ORDER BY id DESC LIMIT 1;";
			try (PreparedStatement get_id = conn.prepareStatement(queryStatement)) {
				try (ResultSet result_id = get_id.executeQuery()) {			
					if (result_id.next()) {					// if the query has a result
						id = result_id.getInt("id");		// assign the id to be return
					}
				}
			}
			conn.close();
		} catch (SQLException e) {
			System.out.println("Unable to get next ID");
			return 0;
		}
		
		return id;
	}
	
	/**
	 *  Given a the email of an existing user, returns the ID associated with that user. 
	 * @param String email
	 * @return int ID
	 */
	public final int getUserID(String email) {
		int id = 0;
		try (Connection conn = initialize_db_connection()) {
			String queryStatement = "SELECT id FROM user WHERE email = \"" + email + "\";";
			try (PreparedStatement get_id = conn.prepareStatement(queryStatement)) {
				try (ResultSet result_id = get_id.executeQuery()) {
					if (result_id.next()) {					// if the query has a result
						id = result_id.getInt("id");		// assign the id to be returned
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("Unable to get user ID");
			return 0;
		}
		
		return id;
	}

	/**
	 * Inserts the user ID and CoreLogic home ID into the saved_homes table in the database.
	 * @param int userID
	 * @param String homeID
	 */
	public final void saveHome(String userID, String homeID, PropertyObject property)  {
		String queryStatement = "INSERT IGNORE INTO saved_homes(`user_id`, `home_id`, `address`, `bed`, `bath`, `sq_ft`, `image_url`) VALUES(?, ?, ?, ?, ?, ? ,?)";

		try (Connection conn = initialize_db_connection()) {
			try (PreparedStatement save = conn.prepareStatement(queryStatement)) {
				save.setInt(1, Integer.parseInt(userID));			// user_id
				save.setString(2, homeID);							// home_id
				save.setString(3, property.getAddress());			// address
				save.setInt(4, property.getBeds());					// bed
				save.setDouble(5, property.getBaths());				// bath
				save.setString(6, property.getSqft());				// sq_ft
				save.setString(7, property.getFirstImage());		// image_url
			
				save.executeUpdate();						
			}
			System.out.println("Home saved!");
			conn.close();
		}catch (SQLException e) {
			System.out.println("Unable to save home");
		}
	}
	
	/**
	 * Removed provided homeID from user's saved homes list
	 * @param homeID
	 */
	public void removeSavedHome(String userID, String homeID)  {

		try (Connection conn = initialize_db_connection()) {
			String remove_cached = "DELETE FROM `saved_homes` WHERE user_ID = ? AND home_id = ?";
			try (PreparedStatement remove = conn.prepareStatement(remove_cached)) {
				remove.setInt(1, Integer.parseInt(userID));			// user_id
				remove.setString(2, homeID);						// home_id
				remove.executeUpdate();						
			}
			System.out.println("Removed Home: " + homeID +" from user: " + userID + "'s savedHomes");
			conn.close();
		}catch (SQLException e) {
			System.out.println("Unable to remove saved home");
		}
	}

	/**
	 * Records the search of a user by inserting the user ID, CoreLogic home ID, and current date into the database. 
	 * @param int userID
	 * @param String homeID
	 */
	public final void recordSearch(String userID, String homeID) {
		String queryStatement = "INSERT INTO search_history(user_id, home_id) VALUES(?, ?)";
		
		try (Connection conn = initialize_db_connection()) {
			try (PreparedStatement searched = conn.prepareStatement(queryStatement)) {
				searched.setInt(1, Integer.parseInt(userID));	// user_id
				searched.setString(2, homeID);					// home_id

				searched.executeUpdate();
			}
			System.out.println("Search saved!");
			conn.close();
		} catch (SQLException e) {
			System.out.println("Unable to record search");
		}
	}

	/**
	 * Query the database to get the list of all CoreLogic home IDs the current user has saved
	 * @param String userID
	 * @return Stack of home IDs
	 */
	public final String getSavedHomes(String userID) {
		String resultString = "";
		
		String queryStatement = "SELECT * FROM saved_homes WHERE user_id = ?";
		
		try (Connection conn = initialize_db_connection()) {
			try (PreparedStatement get_saved_homes = conn.prepareStatement(queryStatement)) {
				get_saved_homes.setInt(1, Integer.parseInt(userID));	// user_id
				
				try (ResultSet results = get_saved_homes.executeQuery()) {
					
					JSONArray saved = new JSONArray();
						
					while (results.next()) {
						String node = Json.createObjectBuilder()
							.add("home_id", results.getString("home_id"))
							.add("address", results.getString("address"))
							.add("bed", results.getInt("bed"))
							.add("bath", results.getDouble("bath"))
							.add("sq_ft", results.getInt("sq_ft"))
							.add("image_url", results.getString("image_url"))
							.build()
							.toString();
					
							saved.put(node);
					}
					resultString = saved.toString().replace("\\", "").replace("\"{", "{").replace("}\"", "}");
				}
			}
			conn.close();		
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Unable to get saved homes");
			return null;
		}
		
		if (resultString.equals("[]")) {
			resultString = "['empty']";
		}
		
		return resultString;
	}
	
	/**
	 * Query the database to see if the current user has saved this specific home
	 * @param String userID
	 * @return Stack of home IDs
	 */
	public final boolean checkSavedHomes(String userID, String homeID) {
		
		String queryStatement = "SELECT EXISTS(SELECT * FROM saved_homes WHERE `user_id` = ? AND `home_id` = ?)";
		
		try (Connection conn = initialize_db_connection()) {
			try (PreparedStatement check_saved_homes = conn.prepareStatement(queryStatement)) {
				check_saved_homes.setInt(1, Integer.parseInt(userID));	// user_id
				check_saved_homes.setString(2, homeID);					// home_id
				
				try (ResultSet results = check_saved_homes.executeQuery()) {
	
					if (results.next()) {
						if(results.getInt(1) == 1)
							return true;
						else
							return false;
					}
					
				}
			}
			conn.close();		
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Unable to get saved homes");
			return false;
		}
		
		return false;
	}
	
	/**
	 * Gets all of the searches recorded in the database within the provided number of days.
	 * @param int days
	 * @return Stack of home ids
	 */
	public final Stack<String[]> getSearchHistory(int days) {
		Stack<String[]> searches = new Stack<String[]>();
		
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.DATE, -days);
		Date lastDate = cal.getTime();
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		String mysqlDateString = formatter.format(lastDate);
	
		
		String queryStatement = "SELECT * FROM user WHERE user = (SELECT user_id FROM search_history WHERE search_date > " + mysqlDateString + ";";
		
		try (Connection conn = initialize_db_connection()) {
			try (PreparedStatement get_saved_homes = conn.prepareStatement(queryStatement)) {

				try (ResultSet savedHomes = get_saved_homes.executeQuery()) {
					while (savedHomes.next()) {
						System.out.println(savedHomes.getInt(1));
						searches.push(new String[] { String.valueOf(savedHomes.getInt(1))});
					}
				}
			}
			conn.close();
		} catch (SQLException e) {
			System.out.println("Unable to get search history");
			return null;
		}
		return searches;
	}
	
	/**
	 * Adds CoreLogic API home data to temporary database cache. 
	 * @param PropertyObject of current property
	 */
	public void addToCache(PropertyObject property)  {

		try (Connection conn = initialize_db_connection()) {
			try (PreparedStatement cache = conn.prepareStatement(property.generateQuery())) {
				cache.executeUpdate();						
			}
			System.out.println("Home cached!");
			conn.close();
			addAllComparablesToCache(property);
		}catch (SQLException e) {
			System.out.println("Unable to cache home");
		}
	}
	
	/**
	 * Removes specific home id from database home cache
	 * @param String of CoreLogic Home ID
	 */
	public void removeCached(String homeID)  {

		try (Connection conn = initialize_db_connection()) {
			String remove_cached = "DELETE FROM `homes_cache` WHERE home_id = ?";
			try (PreparedStatement cache = conn.prepareStatement(remove_cached)) {
				cache.setString(1, homeID);					// home_id
				cache.executeUpdate();						
			}
			System.out.println("Home cache updated!");
			conn.close();
		}catch (SQLException e) {
			System.out.println("Unable to update cached home");
		}
	}
	
	/**
	 * Queries the cache to see if that current home is already in the database. If it is, the last accessed date will be updated to present date and time. 
	 * @param String of CoreLogic Home ID
	 * @return Boolean value. True if home is already cached, else False
	 */
	public boolean checkCache(String homeID)  {

		try (Connection connection = initialize_db_connection()) {
			String stmt_home_cached = "SELECT insert_date FROM `homes_cache` WHERE home_id = ?";

			try (PreparedStatement get_user = connection.prepareStatement(stmt_home_cached)) {
				get_user.setString(1, homeID);				
					
				try (ResultSet cachedResults = get_user.executeQuery()) {
					Date now = new Date();
					
					if(cachedResults.next()) {
						Date queryDate = cachedResults.getDate("insert_date");
						
						int daysCached = (int) (Math.abs ((now.getTime() - queryDate.getTime()) / 86400000));
						
						if(daysCached > 1){
							System.out.println("Outdated cache. Adding: " + homeID);
							removeCached(homeID);
							return false;
						} else {
							System.out.println("Updating cache datetime: " + homeID);
							updateCacheDate(homeID);
							return true;
						}
					} else {
						System.out.println("Not in cache. Adding: " + homeID);
						return false;
					}
				}
			
			} catch (SQLException e) {
				System.out.println("Unable to search cache!");
				e.printStackTrace();
			}
			connection.close();
		} catch (SQLException e1) {
			System.out.println("Unable to check cache!");
			e1.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Updates the date that the provided home was last accessed. 
	 * @param String of CoreLogic Home ID
	 */
	public final void updateCacheDate(String homeID) {
		String queryStatement = "UPDATE `homes_cache` SET `cache_date` = NOW() WHERE home_id = ?";

		try (Connection conn = initialize_db_connection()) {
			try (PreparedStatement update = conn.prepareStatement(queryStatement)) {	
				update.setString(1, homeID);					// home_id

				update.executeUpdate();
			}
			System.out.println("Cached date updated!");
			conn.close();
		} catch (SQLException e) {
			System.out.println("Unable to update cache date");
		}
	}
	
	/**
	 * Retrieved home information from database and inputs database fields to JSON string object. 
	 * @param String of CoreLogic homeID
	 * @return String of JSON home details
	 */
	public String getCachedHome(String homeID, String userID) {
		String queryStatement = "SELECT * FROM `homes_cache` WHERE home_id = ?";
		String resultString = null;

		try (Connection conn = initialize_db_connection()) {
			try (PreparedStatement getHome = conn.prepareStatement(queryStatement)) {	
				getHome.setString(1, homeID);					// home_id

				try (ResultSet results = getHome.executeQuery()) {
						
					resultString = PropertyObject.generateDatabaseJSONString(results, getComparablesCachedHome(homeID), checkSavedHomes(userID, homeID));
				}
			}
			
			System.out.println("Home data retrieved!");
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("Unable to retrieve home data");
		}
		return resultString;
	}

	
	/**
	 * Adds list of comparables homes from CoreLogic API home data to temporary comparables database cache. 
	 * @param PropertyObject of current property
	 */
	public void addAllComparablesToCache(PropertyObject property)  {
		
		String homeID = property.getID();
		JSONArray comparables;

		try {
			comparables = new JSONArray(property.getComparables().toString().replace("\\", "").replace("\"{", "{").replace("}\"", "}"));
			
			for (int i = 0; i < comparables.length(); i++){
				JSONObject currentComparable = new JSONObject();
				try {
					currentComparable = comparables.getJSONObject(i);
					String currentAddress = currentComparable.getString("shortAddress");
					boolean comparableInCache = checkComparablesCache(currentAddress, homeID);
					
					if(!comparableInCache){
						addToComparablesCache(property, i);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		
	}
	
	/**
	 * Adds list of comparables homes from CoreLogic API home data to temporary comparables database cache. 
	 * @param PropertyObject of current property
	 */
	public void addToComparablesCache(PropertyObject property, int index)  {
		
		try (Connection conn = initialize_db_connection()) {
			try (PreparedStatement cache = conn.prepareStatement(property.generateComparableQuery(property.getID(), index))) {
				cache.executeUpdate();						
			}
			System.out.println("Home comparables cached!");
			conn.close();
		}catch (SQLException e) {
			System.out.println("Unable to cache comparable home");
		}
		
	}
	
	/**
	 * Removes specific home id from database home cache
	 * @param String of CoreLogic Home ID
	 */
	public void removeComparablesCached(String shortAddress, String homeID)  {

		try (Connection conn = initialize_db_connection()) {
			String remove_cached = "DELETE FROM `comparables_cache` WHERE shortAddress = ? AND home_id = ?";
			try (PreparedStatement cache = conn.prepareStatement(remove_cached)) {
				cache.setString(1, shortAddress);				// shortAddress
				cache.setString(2, homeID);						// home_id
				cache.executeUpdate();						
			}
			System.out.println("Removed from comparable cache!");
			conn.close();
		}catch (SQLException e) {
			System.out.println("Unable to remove from comparable cache");
		}
	}
	
	/**
	 * Queries the cache to see if that current comparable home is already in the database. If it is, the last accessed date will be updated to present date and time. 
	 * @param String of short address line
	 * @return Boolean value. True if home is already cached, else False
	 */
	public boolean checkComparablesCache(String shortAddress, String homeID)  {
		
		try (Connection connection = initialize_db_connection()) {
			String stmt_home_cached = "SELECT insert_date FROM `comparables_cache` WHERE shortAddress = ? AND home_id = ?";

			try (PreparedStatement get_comparable = connection.prepareStatement(stmt_home_cached)) {
				get_comparable.setString(1, shortAddress);	
				get_comparable.setString(2, homeID);	
					
				try (ResultSet cachedResults = get_comparable.executeQuery()) {
					Date now = new Date();
					
					if(cachedResults.next()) {
						Date queryDate = cachedResults.getDate("insert_date");
						
						int daysCached = (int) (Math.abs ((now.getTime() - queryDate.getTime()) / 86400000));
						
						if(daysCached > 1){
							System.out.println("Outdated comparables cache. Adding: " + shortAddress);
							removeComparablesCached(shortAddress, homeID);
							return false;
						} else {
							System.out.println("Updating comparables cache datetime: " + shortAddress);
							updateComparablesCacheDate(shortAddress, homeID);
							return true;
						}
					} else {
						System.out.println("Not in comparables cache. Adding: " + shortAddress);
						return false;
					}
				}
			
			} catch (SQLException e) {
				System.out.println("Unable to search comparables cache!");
				e.printStackTrace();
			}
			connection.close();
		} catch (SQLException e1) {
			System.out.println("Unable to check comparables cache!");
			e1.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Updates the date that the provided home was last accessed. 
	 * @param String of CoreLogic Home ID
	 */
	public final void updateComparablesCacheDate(String shortAddress, String homeID) {
		String queryStatement = "UPDATE `comparables_cache` SET `cache_date` = NOW() WHERE shortAddress = ? AND home_id = ?";

		try (Connection conn = initialize_db_connection()) {
			try (PreparedStatement update = conn.prepareStatement(queryStatement)) {	
				update.setString(1, shortAddress);				// shortAddress
				update.setString(2, homeID);					// home_id

				update.executeUpdate();
			}
			System.out.println("Comparable cached date updated!");
			conn.close();
		} catch (SQLException e) {
			System.out.println("Unable to update comparable cache date");
		}
	}
	
	
	/**
	 * Retrieves comparable home information from database and inputs database fields to JSON string object. 
	 * @param String of CoreLogic homeID
	 * @return String of JSON home details
	 */
	public JSONArray getComparablesCachedHome(String homeID) {
		String queryStatement = "SELECT * FROM `comparables_cache` WHERE home_id = ?";
		JSONArray resultArray = new JSONArray();

		try (Connection conn = initialize_db_connection()) {
			try (PreparedStatement getHome = conn.prepareStatement(queryStatement)) {	
				getHome.setString(1, homeID);			// home_id

				try (ResultSet results = getHome.executeQuery()) {
						
					resultArray = PropertyObject.generateComparablesJSONArray(results);
				}
			}
			
			System.out.println("Home comparables data retrieved!");
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("Unable to retrieve home comparable data");
		}
		return resultArray;
	}
	
	/**
	 * Tests to see if database is connecting properly
	 */
	public final Boolean testConnection() {
		Stack<String[]> results = new Stack<String[]>();
		String query = "SELECT * FROM user";
		
		try (java.sql.Connection conn = initialize_db_connection()) {
			
			try (java.sql.PreparedStatement get_genre_list = conn.prepareStatement(query)) {
				try (ResultSet result = get_genre_list.executeQuery()) {
					while (result.next()) {
						results.push(new String[] { String.valueOf(result.getInt(1)), result.getString(2) });
						System.out.println(result.getInt(1));
						System.out.println(result.getString(2));
					}
				}
			}
			conn.close();
			return true;
		
		} catch (SQLException e) {
			System.out.println("Unable to connect");
			return false;
		}
	}
	
	/**
	 * Queries the database to get the search history for the last 100 searches
	 * @return String search history results
	 */
	public String getPDFData() {
		String query = "SELECT user_id, home_id, search_date FROM search_history order by search_date desc limit 100;";
		String resultString = "";
		
		try (java.sql.Connection conn = initialize_db_connection()) {
			
			try (java.sql.PreparedStatement get_search_data = conn.prepareStatement(query)) {
				try (ResultSet result = get_search_data.executeQuery()) {
					while (result.next()) {
						resultString += "        " + result.getString(1) + "	" + result.getString(2) + "	" + result.getString(3).substring(0, result.getString(3).length()-2) + " \n";
					}
				}
			}
			conn.close();
			return resultString;
		
		} catch (SQLException e) {
			System.out.println("Unable to connect");
			return null;
		}
	}
}