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

/**
 * A servlet that takes input from a html <form> and talks to MySQL moviedb,
 * generates output as a html <table>
 */

// Declaring a WebServlet called SearchPage, which maps to url "/form"
@WebServlet(name = "AddMovie", urlPatterns = "/add-movie")
public class AddMovie extends HttpServlet {

    private DataSource dataSource;

    private static final long serialVersionUID = 4L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {


    }

    // Use http GET
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {


        JsonObject responseJsonObject = new JsonObject();

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a instance of current session on the request
        HttpSession session = request.getSession(true);

        String return_response = null;


        // Retrieve parameter "name" from the http request, which refers to the value of <input name="name"> in index.html
        String title  = request.getParameter("add_movie_title");
        String director = request.getParameter("add_movie_dir");
        String year = request.getParameter("add_movie_year");
        String star = request.getParameter("add_movie_star");
        String genre = request.getParameter("add_movie_genre");


        //failed
        if(title.compareTo("") == 0 || director.compareTo("") == 0 || year.compareTo("") == 0 || star.compareTo("") == 0 || genre.compareTo("") == 0)
        {
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", "You cannot leave blank");
        }
        //success
        else
        {
            try
            {

                Context initContext = new InitialContext();
                Context envContext = (Context) initContext.lookup("java:/comp/env");
                DataSource ds = (DataSource) envContext.lookup("jdbc/moviedbMaster");
                Connection dbcon = ds.getConnection();

                CallableStatement cs = null;

                cs = dbcon.prepareCall("{call add_new_movie(?,?,?,?,?)}");
                cs.setString(1,title);
                cs.setString(2,director);
                cs.setString(3,year);
                cs.setString(4,star);
                cs.setString(5,genre);
                ResultSet rs = cs.executeQuery(); //execute
                if(rs.next())
                    return_response = rs.getString("answer");


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
