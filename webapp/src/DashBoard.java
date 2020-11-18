import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * A servlet that takes input from a html <form> and talks to MySQL moviedb,
 * generates output as a html <table>
 */

// Declaring a WebServlet called SearchPage, which maps to url "/form"
@WebServlet(name = "DashBoard", urlPatterns = "/dash-board")
public class DashBoard extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();
        JsonArray jsonArray = new JsonArray();

    //get table info from session
        List<Map<String, Object>> tempList = (List<Map<String, Object>>) request.getSession().getAttribute("colname");
        String tempTable = null;
        int counter = 0;
        String longstring = "";

        //start insert all table info:
        for(Map<String, Object> map : tempList)
        {


            //Get info from map
            String tableName = (String) map.get("tableName");
            String COLUMN_NAME = (String) map.get("COLUMN_NAME");
            String TYPE_NAME = (String) map.get("TYPE_NAME");
            String COLUMN_SIZE = (String) map.get("COLUMN_SIZE");
            String NULLABLE = (String) map.get("NULLABLE");
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("tableName", tableName);
            jsonObject.addProperty("COLUMN_NAME", COLUMN_NAME);
            jsonObject.addProperty("TYPE_NAME", TYPE_NAME);
            jsonObject.addProperty("COLUMN_SIZE", COLUMN_SIZE);
            jsonObject.addProperty("NULLABLE", NULLABLE);

            jsonArray.add(jsonObject);
        }

        out.write(jsonArray.toString());
        out.close();
    }

    // Use http GET
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {


        JsonObject responseJsonObject = new JsonObject();

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a instance of current session on the request
        HttpSession session = request.getSession(true);


        // Retrieve parameter "name" from the http request, which refers to the value of <input name="name"> in index.html
        String starname  = request.getParameter("add_star_name");
        String boa = request.getParameter("add_star_boa");

        //failed
        if(starname.compareTo("") == 0)
        {
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", "You must enter a valid star name");
        }
        //success
        else
        {
            if(boa.compareTo("") == 0)
                boa = null;

            try
            {

                Context initContext = new InitialContext();
                Context envContext = (Context) initContext.lookup("java:/comp/env");
                DataSource ds = (DataSource) envContext.lookup("jdbc/moviedb");
                Connection dbcon = ds.getConnection();

                CallableStatement cs = null;
                String return_response = null;

                cs = dbcon.prepareCall("{call add_new_star(?,?)}");
                cs.setString(1,starname);
                cs.setString(2,boa);
                ResultSet rs = cs.executeQuery(); //execute
                if(rs.next())
                {
                    return_response = rs.getString("answer");
                }

                rs.close();
                cs.close();
                dbcon.close();
                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", return_response);
            }
            catch (Exception e) {}

        }

        out.write(responseJsonObject.toString());
        out.close();
    }

}
