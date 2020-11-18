import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * A servlet that takes input from a html <form> and talks to MySQL moviedb,
 * generates output as a html <table>
 */

// Declaring a WebServlet called SearchPage, which maps to url "/form"
@WebServlet(name = "SearchPage", urlPatterns = "/search")
public class SearchPage extends HttpServlet {


    private static final long serialVersionUID = 4L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        // Allgenres = null;
        JsonArray jsonArray = new JsonArray();

        try
        {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/moviedb");
            Connection dbcon = ds.getConnection();


            PreparedStatement statement = null;
            ResultSet rs = null;

            //search all genres
            String query = "select name from genres order by name ASC";
            statement = dbcon.prepareStatement(query);
            rs = statement.executeQuery();

            while (rs.next()) {

                String temp_genres = rs.getString("name");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("genres", temp_genres);
                jsonArray.add(jsonObject);
            }



            rs.close();
            statement.close();
            dbcon.close();

        }catch (Exception e) {

        }



        //String test = "Yess";
        out.write(jsonArray.toString());
        out.close();


    }

    // Use http GET
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {


        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a instance of current session on the request
        HttpSession session = request.getSession(true);

            session.setAttribute("title", null);
            session.setAttribute("year", null);
            session.setAttribute("director", null);
            session.setAttribute("star", null);

            session.setAttribute("mode", null);
            session.setAttribute("browse", null);
            session.setAttribute("sort", null);
            session.setAttribute("pageNumber", 1);
            session.setAttribute("num", null);
            session.setAttribute("TotalPage", null);
            session.setAttribute("query", null);
            session.setAttribute("check", 1);

            session.setAttribute("autoflag", null);

            session.setAttribute("thread", 0);




        // Retrieve parameter "name" from the http request, which refers to the value of <input name="name"> in index.html
            String title = request.getParameter("title");
            String year = request.getParameter("year");
            String director = request.getParameter("director");
            String star = request.getParameter("star");




            if(title.compareTo("")!= 0)
            {
                session.setAttribute("title", title);

            }
            if(year.compareTo("")!= 0)
            {
                session.setAttribute("year", year);

            }
            if(director.compareTo("")!= 0)
            {
                session.setAttribute("director", director);

            }
            if(star.compareTo("")!= 0)
            {
                session.setAttribute("star", star);

            }

            //request.getRequestDispatcher("/index.html").forward(request,response);
            response.sendRedirect("index.html?mode=1&sort=1&num=25");

        out.close();
    }

}
