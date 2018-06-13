package Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DBConnection;
import general.DataProvider;
import objects.PropertyObject;
import objects.TypeAheadObject;

/**
 * Servlet implementation class BackendServlet
 */
@WebServlet("/BackendServlet")
public class BackendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BackendServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Access-Control-Allow-Origin
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        response.addHeader("Access-Control-Max-Age", "1728000");
		
		String currentUserID = null;
		DBConnection db = new DBConnection();  									// Database connection class
		
		/*
		 * If the request contains a "type" parameter
		 */
		if(request.getParameter("type") != null){								// Check for http request parameters
			
			/*
			 * If the type is "getNewUserID"
			 */
			if (request.getParameter("type").equals("getNewUserID")) {
				db.newUser(request.getHeader("X-FORWARDED-FOR"));
				response.getWriter().append("{\"user\":\"" + db.lastInserted() + "\"}");
				System.out.println("Created cookie: " + db.lastInserted() );
			
			/*
			 * Else if the type is "typeAhead"
			 */
			} else if (request.getParameter("type").equals("typeAhead")) {
				TypeAheadObject t = new TypeAheadObject(request.getParameter("partial").toString());
				String data = t.generateJSON().toString();
				response.getWriter().append(data);
			
			/*
			 * Else if the request contains "userID"
			 */	
			} else if (request.getParameterMap().containsKey("userID") && (request.getParameter("userID") != null 
					|| request.getParameter("userID").toString().equals("undefined") 
					|| request.getParameter("userID").toString().equals(""))) {
				
				currentUserID = request.getParameter("userID").toString();
				
				/*
				 * If the type is "address"
				 */	
				if (request.getParameter("type").toString().equals("address")) {	// If request is address
					DataProvider data = new DataProvider();							
																					// Query CoreLogic API to get home ID
					if(request.getParameter("addressLine") != null && request.getParameter("zipCode") != null) {
						String id = data.getCoreLogicID(request.getParameter("addressLine").toString(), request.getParameter("zipCode").toString());
						System.out.println(id);
						request.getSession().setAttribute("id", id);				// Set the home ID
						
						PropertyObject p = new PropertyObject(id);
						String jData = p.generateJSONString(db.checkSavedHomes(currentUserID, p.getID()));			// Use home ID to get JSON property attributes
						response.getWriter().append(jData);
						
						db.recordSearch(currentUserID, id);							// Record the search of that address in database
						
					}
					
				/*
				 * Else if the type is "propertyInfo"
				 */		
				} else if (request.getParameter("type").equals("propertyInfo")) {			// If request is property info
					String homeID = request.getParameter("id").toString();
					
					boolean homeInCache = db.checkCache(homeID);
					String data = "";
					
					if (!homeInCache) {
						PropertyObject p = new PropertyObject(request.getParameter("id").toString());
						data = p.generateJSONString(db.checkSavedHomes(currentUserID, homeID));						//  Get JSON property attributes for the provided home id
						db.addToCache(p);
					} else {
						data = db.getCachedHome(homeID, currentUserID); 
					}
					
					db.recordSearch(currentUserID, homeID);						// Record the search of that address in database
					response.getWriter().append(data);
			
					
					//db.recordSearch(currentUserID, request.getParameter("id")); // Record the search of that home id in database
				
				/*
				 * Else if the type is "savedHomes"
				 */		
				} else if (request.getParameter("type").equals("savedHomes")) {			// If request is to get saved homes list
					response.getWriter().append(db.getSavedHomes(currentUserID));		// Adds saved Homes to JSON response

				/*
				 * Else if the type is "saveHome"
				 */		
				} else if (request.getParameter("type").equals("saveHome")) {			// If request is to save home
					String homeID = request.getParameter("id").toString();
					
					boolean homeInCache = db.checkCache(homeID);
					PropertyObject p; 
					
					if (!homeInCache) {
						p = new PropertyObject(homeID);
						db.addToCache(p);
					} else {
						
						p = new PropertyObject(homeID, db.getCachedHome(homeID, currentUserID), db.getComparablesCachedHome(homeID)); 
					}
					
					db.saveHome(currentUserID, homeID, p);
				
				/*
				 * Else if the type is "removeHome"
				 */		
				} else if (request.getParameter("type").equals("removeHome")) {		// If request is to remove saved home
					db.removeSavedHome(currentUserID, request.getParameter("id").toString());
					
				} 
				
				// Add additional request parameters here
				
			}
		} 
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
