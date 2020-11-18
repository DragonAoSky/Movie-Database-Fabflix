
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

// server endpoint URL
@WebServlet("/hero-suggestion")
public class HeroSuggestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	


    public HeroSuggestion() {
        super();
    }

    /*
     * 
     * Match the query against superheroes and return a JSON response.
     * 
     * For example, if the query is "super":
     * The JSON response look like this:
     * [
     * 	{ "value": "Superman", "data": { "heroID": 101 } },
     * 	{ "value": "Supergirl", "data": { "heroID": 113 } }
     * ]
     * 
     * The format is like this because it can be directly used by the 
     *   JSON auto complete library this example is using. So that you don't have to convert the format.
     *   
     * The response contains a list of suggestions.
     * In each suggestion object, the "value" is the item string shown in the dropdown list,
     *   the "data" object can contain any additional information.
     * 
     * 
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		String temp1 = request.getParameter("name");
		String temp2 = request.getParameter("hero");

		String title = "";

//		if(temp1 != null)
//			if(!temp1.equals(""))
//				System.out.println("name is not null and not empty");
//			else
//				System.out.println("name is not null but empty");
//		else
//			System.out.println("name is null");
//
//		if(temp2 != null)
//			if(!temp2.equals(""))
//				System.out.println("hero is not null and not empty");
//			else
//				System.out.println("hero is not null but empty");
//		else
//			System.out.println("hero is null");




		if(temp1 == null)
			if(temp2 != null)
				if(!temp2.equals(""))
					title = temp2;
		else
			if(!temp1.equals(""))
				title = temp1;


		session.setAttribute("title", null);
		session.setAttribute("year", null);
		session.setAttribute("director", null);
		session.setAttribute("star", null);
		session.setAttribute("autoflag", "1");

		session.setAttribute("mode", null);
		session.setAttribute("browse", null);
		session.setAttribute("sort", null);
		session.setAttribute("pageNumber", 1);
		session.setAttribute("num", null);
		session.setAttribute("TotalPage", null);
		session.setAttribute("query", null);
		session.setAttribute("check", 1);

		if(title.compareTo("")!= 0)
		{
			session.setAttribute("title", title);

		}


		response.sendRedirect("index.html?mode=1&sort=1&num=25");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			int CacheFlag = 0;

			HttpSession session = request.getSession(true);


			//get the search history
			HashMap<String, Object> old = (HashMap<String, Object>) session.getAttribute("Cache");

			if(old == null)
			{
				old = new HashMap<String, Object>();
			}

			Cache tempC = new Cache();

			// setup the response json arrray
			JsonArray jsonArray = new JsonArray();
			
			// get the query string from parameter
			String title = request.getParameter("query");
			
			// return the empty json array if query is null or empty
			if (title == null || title.trim().isEmpty()) {
				response.getWriter().write(jsonArray.toString());
				return;
			}	
			
			// search on superheroes and add the results to JSON Array
			// this example only does a substring match
			// TODO: in project 4, you should do full text search with MySQL to find the matches on movies and stars



			List<String> titleList = new ArrayList<>();
			String FinalTitle = "";

			if(title != null)
			{
				String[] split2 = title.split(" ");
				for (String tempGen2 : split2) {
					titleList.add(tempGen2);
				}


				if(title != null)
				{
					for(String t: titleList)
					{
						FinalTitle += "+" + t + "* ";
					}
				}
			}

			//check if the search query is in hashmap or not
			Cache check = (Cache) old.get(FinalTitle);

			if(check == null)
			{
				//set up log message
				JsonObject tempObj = new JsonObject();
				tempObj.addProperty("status", "query");
				jsonArray.add(tempObj);

				JsonArray tempArray = new JsonArray();

				Context initContext = new InitialContext();
				Context envContext = (Context) initContext.lookup("java:/comp/env");
				DataSource ds = (DataSource) envContext.lookup("jdbc/moviedb");
				Connection connection = ds.getConnection();


				String query = "select movies.id, movies.title from movies where match(title) against (? IN BOOLEAN MODE) limit 10";
				ResultSet rs = null;
				PreparedStatement statement = null;
				statement = connection.prepareStatement(query);
				statement.setString(1, FinalTitle);
				rs = statement.executeQuery();
				while (rs.next())
				{
					String id = rs.getString("id");
					String Mtitle = rs.getString("title");
					tempArray.add(generateJsonObject(id, Mtitle));
				}

				//add new Cache to list
				tempC.setJsonArray(tempArray);
				jsonArray.addAll(tempArray);

				old.put(FinalTitle,tempC);
				rs.close();
				statement.close();
				connection.close();

			}
			//exist!
			else
			{
				//System.out.println("Exists!");

				//add log message
				JsonObject tempObj = new JsonObject();
				tempObj.addProperty("status", "cache");
				jsonArray.add(tempObj);

				JsonArray temparray = check.getJsonArray();
				for (int i = 0; i < temparray.size(); i++) {
					jsonArray.add(temparray.get(i));
				}

			}














			//----------------------------------------------------------








			//set Cache to session
			session.setAttribute("Cache", old);
			response.getWriter().write(jsonArray.toString());

			return;
		} catch (Exception e) {
			System.out.println(e);
			response.sendError(500, e.getMessage());
		}
	}
	
	/*
	 * Generate the JSON Object from hero to be like this format:
	 * {
	 *   "value": "Iron Man",
	 *   "data": { "heroID": 11 }
	 * }
	 * 
	 */
//	private static JsonObject generateJsonObject(Integer heroID, String heroName) {
//		JsonObject jsonObject = new JsonObject();
//		jsonObject.addProperty("value", heroName);
//
//		JsonObject additionalDataJsonObject = new JsonObject();
//		additionalDataJsonObject.addProperty("heroID", heroID);
//
//		jsonObject.add("data", additionalDataJsonObject);
//		return jsonObject;
//	}

	private static JsonObject generateJsonObject(String movieID, String title) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("value", title);

		JsonObject additionalDataJsonObject = new JsonObject();
		additionalDataJsonObject.addProperty("movieID", movieID);

		jsonObject.add("data", additionalDataJsonObject);
		return jsonObject;
	}


}
