import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * This IndexServlet is declared in the web annotation below,
 * which is mapped to the URL pattern /api/index.
 */
@WebServlet(name = "Cart", urlPatterns = "/api/cart")
public class Cart extends HttpServlet {

    /**
     * handles GET requests to store session information
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {


        String itemID = request.getParameter("id");
        System.out.println(itemID);
        HttpSession session = request.getSession();

        // get the previous items in a ArrayList
        ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");

        int flag = 0; // check if added
        int count = -1;
        int place = 0;
        String NewItem = null;

        //creat new item list if not exist
        if (previousItems == null) {
            previousItems = new ArrayList<>();
            String combineItem = itemID + "," + "1";
            previousItems.add(combineItem);
            session.setAttribute("previousItems", previousItems);
        } else {
            // prevent corrupted states through sharing under multi-threads
            // will only be executed by one thread at a time
            synchronized (previousItems) {

                //check if already exist or not
                for (String combineItem : previousItems)
                {
                    count++;
                    String tempString = combineItem;
                    String[] split = tempString.split(",");
                    String MovieID = split[0];
                    //already exists
                    if(itemID.equals(MovieID))
                    {
                        flag = 1;
                        place = count;
                        String tempNum = split[1];
                        int NumMovie = Integer.parseInt(tempNum);
                        NumMovie++;
                        NewItem = itemID + "," + NumMovie;
                    }
                }
                //Not exists
                if(flag == 0)
                {
                    NewItem = itemID + "," + "1";
                    previousItems.add(NewItem);
                }
                else
                {
                    previousItems.set(place,NewItem);
                }
            }

            session.setAttribute("previousItems", previousItems);
        }



        response.getWriter().write(String.join(",", previousItems));
    }

    /**
     * handles POST requests to add and show the item list information
     */
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//
//    }
}
