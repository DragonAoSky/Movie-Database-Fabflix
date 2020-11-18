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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


@WebServlet(name = "ShowCart",urlPatterns = "/api/showcart")
public class ShowCart extends HttpServlet {


    public void  doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        JsonArray jsonArray = new JsonArray();


        try
        {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/moviedb");
            Connection dbcon = ds.getConnection();


            Statement statement = dbcon.createStatement();
            ResultSet rs = null;

            // get the previous items in a ArrayList
            ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");

            for (String combineItem : previousItems)
            {
                String title = null;
                rs = null;
                String tempString = combineItem;
                String[] split = tempString.split(",");
                String MovieID = split[0];
                String quantity = split[1];

                String query = "select movies.title from movies where movies.id = '" + MovieID + "'";
                rs = statement.executeQuery(query);
                if(rs.next())
                    title = rs.getString("title");

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_title", title);
                jsonObject.addProperty("movie_id", MovieID);
                jsonObject.addProperty("quantity", quantity);
                jsonArray.add(jsonObject);
            }

            rs.close();
            statement.close();
            dbcon.close();

            PrintWriter out = response.getWriter();
            //JsonArray carList = (JsonArray) request.getSession().getAttribute("carList");

            out.write(jsonArray.toString());

        }
        catch (Exception e)
        {

        }



    }

}