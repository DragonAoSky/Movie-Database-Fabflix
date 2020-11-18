import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
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
import java.util.ListIterator;

@WebServlet(name = "CheckOut", urlPatterns = "/api/checkout")
public class CheckOut extends HttpServlet {

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int totalPrice = 0;
        int count = -1;
        int Tempplace = 0;
        //calculate total price
        HttpSession session = request.getSession();
        JsonArray jsonArray = new JsonArray();
        ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");
        ArrayList<Integer> place = new ArrayList<>();
        //empty
        if (previousItems == null) {
            previousItems = new ArrayList<>();
            //session.setAttribute("previousItems", previousItems);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("total", totalPrice);
            jsonArray.add(jsonObject);
        }
        //not empty
        else
        {
            //start calculate
            for (String combineItem : previousItems)
            {
                count++;
                String tempString = combineItem;
                String[] split = tempString.split(",");
                String Sprice = split[1];
                int price = Integer.parseInt(Sprice);
                if(price <= 0)
                {
                    Tempplace = count;
                    place.add(Tempplace);
                }
                else
                {
                    totalPrice = totalPrice + price;
                }
            }

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("total", totalPrice);

            //set as global
            session.setAttribute("Total_Price", totalPrice);
            jsonArray.add(jsonObject);

            //delete 0 item
            ListIterator<Integer> lit = place.listIterator();
            while (lit.hasNext()) {
            }

            while (lit.hasPrevious()) {
                int t = lit.previous();
                previousItems.remove(t);
            }

            if(previousItems.size() > 0)
                session.setAttribute("previousItems", previousItems);
            else
                session.setAttribute("previousItems", null);

        }

        PrintWriter out = response.getWriter();
        //JsonArray carList = (JsonArray) request.getSession().getAttribute("carList");

        out.write(jsonArray.toString());

    }



    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JsonObject responseJsonObject = new JsonObject();

        PrintWriter out = response.getWriter();
        String Fname = null;
        String Lname = null;
        String Cnumber = null;
        String Edate = null;


        try
        {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/moviedbMaster");
            Connection dbcon = ds.getConnection();

            PreparedStatement statement = null;
            ResultSet rs = null;

            Fname = request.getParameter("Fname");
            Lname = request.getParameter("Lname");
            Cnumber = request.getParameter("Cnumber");
            Edate = request.getParameter("Edate");
            String FinalFname = null;
            String FinalLname = null;
            //String FinalCnumber = null;
            String FinalEdate = null;

            if(Fname.equals("") || Lname.equals("") || Cnumber.equals("") || Edate.equals(""))
            {
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "You cannot leave any field blank");
            }
            else
            {
                String query1 = "select creditcards.id from creditcards where id = ? and firstName = ? and lastName = ? and expiration = ?";
                statement = dbcon.prepareStatement(query1);
                statement.setString(1, Cnumber);
                statement.setString(2, Fname);
                statement.setString(3, Lname);
                statement.setString(4, Edate);

                //check username
                //rs = statement.executeQuery(query1);
                rs = statement.executeQuery();


                if (rs.next())
                {
                    responseJsonObject.addProperty("status", "success");
                    responseJsonObject.addProperty("message", "success");
                }
                else
                {
                    responseJsonObject.addProperty("status", "fail");
                    responseJsonObject.addProperty("message", "Credit card information is incorrect!");
                }


            }

            rs.close();
            statement.close();
            dbcon.close();
        } catch (Exception e) {

        }

        out.write(responseJsonObject.toString());


    }
}
