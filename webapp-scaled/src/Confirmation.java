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
import java.util.ArrayList;


@WebServlet(name = "Confirmation",urlPatterns = "/api/confirmation")
public class Confirmation extends HttpServlet {


    public void  doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        JsonArray jsonArray = new JsonArray();

        //get all information need
        int totalPrice = (Integer) session.getAttribute("Total_Price");
        ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");
        String UserID = (String)session.getAttribute("customerID");

        //initialize global
        session.setAttribute("Total_Price", 0);
        session.setAttribute("previousItems", null);



        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("totalPrice", totalPrice);
        jsonObject.addProperty("UserID", UserID);
        jsonArray.add(jsonObject);

        PrintWriter out = response.getWriter();
        //JsonArray carList = (JsonArray) request.getSession().getAttribute("carList");
        out.write(jsonArray.toString());


        try
        {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/moviedbMaster");
            Connection dbcon = ds.getConnection();
            ResultSet rs = null;
            String query = "insert into sales(customerID,movieID,saleDate,quantity) values(?,?,CURDATE(), ?)";
            String query2 = "SET time_zone = '-08:00'";
            PreparedStatement statement = dbcon.prepareStatement(query);
            PreparedStatement statement2 = dbcon.prepareStatement(query2);
            statement2.executeUpdate();

            // get the previous items in a ArrayList
            for (String combineItem : previousItems)
            {
                rs = null;
                String tempString = combineItem;
                String[] split = tempString.split(",");
                String MovieID = split[0];
                String quantity = split[1];
                statement.setString(1, UserID);
                statement.setString(2, MovieID);
                statement.setString(3, quantity);
                statement.executeUpdate();
            }

            rs.close();
            statement.close();
            statement2.close();
            dbcon.close();



        }
        catch (Exception e)
        {

        }



    }

}