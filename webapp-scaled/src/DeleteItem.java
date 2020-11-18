import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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


@WebServlet(name = "DeleteItem",urlPatterns = "/api/delete")
public class DeleteItem extends HttpServlet {


    public void  doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        JsonArray jsonArray = new JsonArray();

        //get item ID we want to delete
        String itemID = request.getParameter("id");
        // get the previous items in a ArrayList
        ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");

        int count = -1;
        int place = 0;
        String DeleteItem = null;

        for (String combineItem : previousItems)
        {
            count++;
            String tempString = combineItem;
            String[] split = tempString.split(",");
            String MovieID = split[0];

            //Found!
            if(itemID.equals(MovieID))
            {
                place = count; // remember place
                DeleteItem = combineItem;
            }
        }

        previousItems.remove(DeleteItem);
        session.setAttribute("previousItems", previousItems);

        PrintWriter out=response.getWriter();
        out.write(itemID);
        out.close();
    }

}