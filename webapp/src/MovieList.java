import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Declaring a WebServlet called StarsServlet, which maps to url "/api/stars"
@WebServlet(name = "MovieList", urlPatterns = "/api/movie")
public class MovieList extends HttpServlet {
    private static String filenameTemp;
    private static final long serialVersionUID = 1L;


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long Servlet_startTime = System.currentTimeMillis();
        long temp_startTime = 0;
        long temp_endTime = 0;
        long Total_JDBC_Time = 0;
        long Total_Servlet_Time = 0;
        int numthread = 0;



        //start anay request
        String agent=request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(agent);
        Browser browser = userAgent.getBrowser();
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        //System.out.println("Device Type:"+operatingSystem.getDeviceType());
        String deviceType = operatingSystem.getDeviceType().toString();

        //System.out.println(deviceType);
        //------------------------------------

        response.setContentType("application/json"); // Response mime type

        String mode = request.getParameter("mode");
        String temp_browse = request.getParameter("browse");
        String temp_sort = request.getParameter("sort");
        String page = request.getParameter("page");
        String temp_num = request.getParameter("num");
        String jmeter = request.getParameter("jmeter");
        int jmeterFlag = 0;

        String test_title = request.getParameter("title");
        int Checkmode = 0;
//        System.out.println("mode is " + mode);
//        System.out.println("title is " + test_title);



        int autoflag = 0;


        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        //HttpSession session = request.getSession(true);
        HttpSession session = request.getSession();

        try {
            // Get a connection from dataSource


            temp_startTime = System.currentTimeMillis();
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/moviedb");
            Connection dbcon = ds.getConnection();
            temp_endTime = System.currentTimeMillis();
            Total_JDBC_Time += (temp_endTime-temp_startTime);

            int star_year = 0; // 0:default 1:no star 2:Only star 3:year 4:year + star

            //get parameters
            ResultSet rs = null;
            ResultSet rs2 = null;
            ResultSet rs3 = null;
            JsonArray jsonArray = new JsonArray();
            Integer check = (Integer)session.getAttribute("check");
//            if(jmeter != null)
//                if(!jmeter.equals("null"))
//                    check = null;
            if(check == null)
            {
                //System.out.println("Clean check");
                session.setAttribute("title", null);
                session.setAttribute("year", null);
                session.setAttribute("director", null);
                session.setAttribute("star", null);
                session.setAttribute("thread", 0);
                session.setAttribute("mode", null);
                session.setAttribute("browse", null);
                session.setAttribute("sort", null);
                session.setAttribute("pageNumber", 1);
                session.setAttribute("num", null);
                session.setAttribute("TotalPage", null);
                session.setAttribute("query", null);
                session.setAttribute("check", 1);
            }

            numthread = (Integer)session.getAttribute("thread");
            if(numthread == 0)
            {
                session.setAttribute("thread", 1);
                //create txt
//                creatTxtFile("time_used.txt");
            }



            // init total page count
            session.setAttribute("TotalPage", null);

            //save parameter to global
            //detect mode
            String temp_Mode = (String)session.getAttribute("mode");
            //a new action
            Checkmode = Integer.parseInt(mode);
            if(!mode.equals("3"))
                session.setAttribute("mode", mode);
            else
                mode = temp_Mode;


            //-----------------------------------
            if(temp_sort != null)
                if(!temp_sort.equals("null"))
                    session.setAttribute("sort", temp_sort);
            if(temp_num != null)
                if(!temp_num.equals("null"))
                    session.setAttribute("num", temp_num);
            if(temp_browse != null)
                if(!temp_browse.equals("null"))
                    session.setAttribute("browse", temp_browse);

            //assign value
            String sort = (String)session.getAttribute("sort");
            String num = (String)session.getAttribute("num");
            String browse = (String)session.getAttribute("browse");
            String checkauto = (String)session.getAttribute("autoflag");

            if(checkauto != null || deviceType.equals("MOBILE"))
            {
                autoflag = 1;
            }

            //-----------------------------------





            //search
            if(mode.compareTo("1") == 0)
            {
                PreparedStatement statement = null;

                PreparedStatement statement3 = null;
                PreparedStatement statement2 = null;

                statement3 = dbcon.prepareStatement("");
                statement2 = dbcon.prepareStatement("");
                statement = dbcon.prepareStatement("");

                String title = "";
                String year = "";
                String director = "";
                String star = "";

                //get search result
                title = (String)session.getAttribute("title");
                year = (String)session.getAttribute("year");
                director = (String)session.getAttribute("director");
                star = (String)session.getAttribute("star");

                //from mobile
                if(deviceType.equals("MOBILE") && Checkmode != 3)
                {
                    title = test_title;
                    session.setAttribute("title",title);
                }
                else if(deviceType.equals("MOBILE") && Checkmode == 3)
                {
                    title = (String) session.getAttribute("title");
                }
                //from Jmeter
                else if(test_title != null)
                {
                    if(!test_title.equals("null"))
                    {
                        title = test_title;
                        session.setAttribute("title",title);
                    }
                }


                List<String> titleList = new ArrayList<>();
                String FinalTitle = "";

                session.setAttribute("browse", null);

                String query = null;
                //set up query
                if(autoflag == 0)
                {
                    if(title == null && year == null && director == null && star == null)
                    {
                        query = "select * from topmovies";
                    }
                    // has title but not year
                    else if((title != null || director != null) && year == null)
                    {
                        if(director == null)
                            director = "";
                        if(title == null)
                            title = "";
                        //without star
                        if(star == null)
                        {
                            query = "select * from topmovies where topmovies.title like ? AND topmovies.director like ?";
                            star_year = 1;
                        }
                        //with star
                        else
                        {
                            star_year = 2;
                            //query = "select topmovies.* from topmovies, stars, stars_in_movies where topmovies.title like '%" + title + "%' AND topmovies.director like '%" + director + "%' AND topmovies.id = stars_in_movies.movieId AND stars_in_movies.starId = stars.id AND stars.name like '%" + star + "%' group by topmovies.id";
                            query = "select topmovies.* from topmovies, stars, stars_in_movies where topmovies.title like ? AND topmovies.director like ? AND topmovies.id = stars_in_movies.movieId AND stars_in_movies.starId = stars.id AND stars.name like ? group by topmovies.id";
                        }

                    }
                    //include year
                    else if(year != null)
                    {
                        if(director == null)
                            director = "";
                        if(title == null)
                            title = "";

                        //without stars
                        if(star == null)
                        {
                            star_year = 3;
//                        query = "select * from topmovies where topmovies.title like '%";
//                        query = query + title + "%' AND topmovies.director like '%" + director + "%'" + " AND year = '" + year + "'";
                            query = "select * from topmovies where topmovies.title like ? AND topmovies.director like ? AND year = ?";
                        }
                        //with year and stars
                        else
                        {
                            star_year = 4;
//                        query = "select topmovies.* from topmovies, stars, stars_in_movies where topmovies.title like '%";
//                        query = query + title + "%' AND topmovies.director like '%" + director + "%' AND year = '" + year + "' AND topmovies.id = stars_in_movies.movieId and stars_in_movies.starId = stars.id AND stars.name like '%" + star + "%' group by topmovies.id";
                            query = "select topmovies.* from topmovies, stars, stars_in_movies where topmovies.title like ? AND topmovies.director like ? AND year = ? AND topmovies.id = stars_in_movies.movieId and stars_in_movies.starId = stars.id AND stars.name like ? group by topmovies.id";
                        }
                    }
                }
                else
                {
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

                    query = "select movies.id, movies.title, movies.year, movies.director, substring_index(group_concat(distinct genres.name order by genres.name SEPARATOR ','), ',', 3) as genres from movies , genres, genres_in_movies where movies.id = genres_in_movies.movieId and genres_in_movies.genreId = genres.id";
                    query += " and match(title) against (? IN BOOLEAN MODE)";

                }

                //System.out.println("FinalTitle is:" + FinalTitle);

                //calculate total result and total page number
                String resultCount = "";
                if(autoflag == 1)
                    resultCount = "select count(*) as count from (" + "select * from(" + query +" group by movies.id) a LEFT JOIN(select movies.id as tempid, ratings.rating from movies, ratings where movies.id = ratings.movieId group by movies.id)b on a.id = b.tempid) as t";
                else
                    resultCount = "select count(*) as count from ("  + query + ") as t";

                temp_startTime = System.currentTimeMillis();
                statement3 = dbcon.prepareStatement(resultCount);
                temp_endTime = System.currentTimeMillis();
                Total_JDBC_Time += (temp_endTime-temp_startTime);

                //start set value
                if(autoflag == 0)
                {
                    if(star_year == 0)
                    {
                        //no action
                    }
                    else if(star_year == 1)
                    {
                        statement3.setString(1, "%" + title + "%");
                        statement3.setString(2, "%" + director + "%");
                    }
                    else if(star_year == 2)
                    {
                        statement3.setString(1, "%" + title + "%");
                        statement3.setString(2, "%" + director + "%");
                        statement3.setString(3, "%" + star + "%");
                    }
                    else if(star_year == 3)
                    {
                        statement3.setString(1, "%" + title + "%");
                        statement3.setString(2, "%" + director + "%");
                        statement3.setString(3, year );
                    }
                    else if(star_year == 4)
                    {
                        statement3.setString(1, "%" + title + "%");
                        statement3.setString(2, "%" + director + "%");
                        statement3.setString(3, year );
                        statement3.setString(4, "%" + star + "%");
                    }
                }
                else
                {
                    statement3.setString(1, FinalTitle);
                }


                temp_startTime = System.currentTimeMillis();
                //execute find total pages
                rs3 = statement3.executeQuery();

                String tempRN = "";
                temp_endTime = System.currentTimeMillis();
                Total_JDBC_Time += (temp_endTime-temp_startTime);




                while (rs3.next())
                {
                    tempRN = rs3.getString("count");
                }

                int num_result = Integer.valueOf(tempRN);
                //there are no result
                if(num_result == 0)
                {
                    JsonObject jsonObject_jmeter = new JsonObject();
                    jsonObject_jmeter.addProperty("status", "fail");
                    jsonArray.add(jsonObject_jmeter);


                }
                else
                {
                    JsonObject jsonObject_jmeter = new JsonObject();
                    jsonObject_jmeter.addProperty("status", "success");
                    jsonArray.add(jsonObject_jmeter);


                    int Single_Page = Integer.valueOf(num);

                    int Total_page = num_result / Single_Page;
                    if( (num_result % Single_Page) != 0)
                        Total_page++;

                    //save it to global
                    session.setAttribute("TotalPage", Total_page);

                    if(autoflag != 0)
                        query = "select * from(" + query + " group by movies.id) a LEFT JOIN(select movies.id as tempid, ratings.rating from movies, ratings where movies.id = ratings.movieId group by movies.id)b on a.id = b.tempid";

                    //--------------------------------------
                    if(autoflag == 0)
                    {
                        if(sort.compareTo("1") == 0)
                        {
                            query = query + " order by topmovies.rating DESC, topmovies.title DESC";
                        }
                        else if(sort.compareTo("2") == 0)
                        {
                            query = query + " order by topmovies.rating ASC, topmovies.title ASC";
                        }
                        else if(sort.compareTo("3") == 0)
                        {
                            query = query + " order by topmovies.title DESC, topmovies.rating DESC";
                        }
                        else if(sort.compareTo("4") == 0)
                        {
                            query = query + " order by topmovies.title ASC, topmovies.rating ASC";
                        }
                        else if(sort.compareTo("5") == 0)
                        {
                            query = query + " order by topmovies.rating ASC, topmovies.title DESC";
                        }
                        else if(sort.compareTo("6") == 0)
                        {
                            query = query + " order by topmovies.rating DESC, topmovies.title ASC";
                        }
                        else if(sort.compareTo("7") == 0)
                        {
                            query = query + " order by topmovies.title ASC, topmovies.rating DESC";
                        }
                        else if(sort.compareTo("8") == 0)
                        {
                            query = query + " order by topmovies.title DESC, topmovies.rating ASC";
                        }
                    }
                    else
                    {
                        if(sort.compareTo("1") == 0)
                        {
                            query = query + " order by rating DESC, title DESC";
                        }
                        else if(sort.compareTo("2") == 0)
                        {
                            query = query + " order by rating ASC, title ASC";
                        }
                        else if(sort.compareTo("3") == 0)
                        {
                            query = query + " order by title DESC, rating DESC";
                        }
                        else if(sort.compareTo("4") == 0)
                        {
                            query = query + " order by title ASC, rating ASC";
                        }
                        else if(sort.compareTo("5") == 0)
                        {
                            query = query + " order by rating ASC, title DESC";
                        }
                        else if(sort.compareTo("6") == 0)
                        {
                            query = query + " order by rating DESC, title ASC";
                        }
                        else if(sort.compareTo("7") == 0)
                        {
                            query = query + " order by title ASC, rating DESC";
                        }
                        else if(sort.compareTo("8") == 0)
                        {
                            query = query + " order by title DESC, rating ASC";
                        }
                    }

                    if(num.compareTo("10") == 0)
                    {
                        query = query + " limit 10";
                    }
                    else if(num.compareTo("25") == 0 )
                    {
                        query = query + " limit 25";
                    }
                    else if(num.compareTo("50") == 0)
                    {
                        query = query + " limit 50";
                    }
                    else if(num.compareTo("100") == 0)
                    {
                        query = query + " limit 100";
                    }
                    else if(num.compareTo("20") == 0)
                    {
                        query = query + " limit 20";
                    }

                    //edit offset
                    Integer currentPage = (Integer)session.getAttribute("pageNumber");
                    //calculate correct current page
                    if(page != null)
                    {
                        //prev
                        if(page.compareTo("1") == 0)
                        {
                            currentPage = currentPage - 1;
                            if (currentPage < 1)
                                currentPage = 1;
                        }
                        //next
                        else if(page.compareTo("2") == 0)
                        {
                            currentPage = currentPage + 1;
                            if(currentPage > Total_page)
                                currentPage = Total_page;
                        }
                    }

                    //update page number
                    session.setAttribute("pageNumber", currentPage);

                    //----------------

                    int offset = 0;
                    if(currentPage == 1)
                    {

                    }
                    else if(currentPage <= Total_page)
                    {
                        offset = (currentPage - 1) * Single_Page;
                        query = query + " offset " + offset;
                    }
                    else if(currentPage > Total_page)
                    {
                        offset = (Total_page - 1) * Single_Page;
                        query = query + " offset " + offset;
                    }

                    // Perform the query
                    statement = dbcon.prepareStatement(query);
                    rs = null;
                    //set query
                    if(autoflag == 0)
                    {
                        if(star_year == 0)
                        {
                            //no action
                        }
                        else if(star_year == 1)
                        {
                            statement.setString(1, "%" + title + "%");
                            statement.setString(2, "%" + director + "%");
                        }
                        else if(star_year == 2)
                        {
                            statement.setString(1, "%" + title + "%");
                            statement.setString(2, "%" + director + "%");
                            statement.setString(3, "%" + star + "%");
                        }
                        else if(star_year == 3)
                        {
                            statement.setString(1, "%" + title + "%");
                            statement.setString(2, "%" + director + "%");
                            statement.setString(3, year );
                        }
                        else if(star_year == 4)
                        {
                            statement.setString(1, "%" + title + "%");
                            statement.setString(2, "%" + director + "%");
                            statement.setString(3, year );
                            statement.setString(4, "%" + star + "%");
                        }
                    }
                    else
                    {
                        statement.setString(1, FinalTitle);
                    }

                    temp_startTime = System.currentTimeMillis();
                    rs = statement.executeQuery();
                    rs2 = null;
                    temp_endTime = System.currentTimeMillis();
                    Total_JDBC_Time += (temp_endTime-temp_startTime);


                    // Iterate through each row of rs
                    while (rs.next()) {
                        rs2 = null;
                        ArrayList list1 = new ArrayList();
                        ArrayList list2 = new ArrayList();
                        ArrayList list3 = new ArrayList();

                        String movie_id = rs.getString("id");
                        String movie_title = rs.getString("title");
                        String movie_year = rs.getString("year");
                        String movie_director = rs.getString("director");
                        String movie_genres = rs.getString("genres");
                        String movie_rating = rs.getString("rating");


                        //assign string
                        String star1 = "";
                        String star2 = "";
                        String star3 = "";
                        String ID1 = "";
                        String ID2 = "";
                        String ID3 = "";
                        String gen1 = "";
                        String gen2 = "";
                        String gen3 = "";

                        //split gen
                        String tempGen = movie_genres;
                        String[] split = tempGen.split(",");
                        for (String tempGen2 : split) {
                            list3.add(tempGen2);
                        }

                        //insert gens
                        if(list3.size() == 0)
                        {
                        }
                        else if(list3.size() == 1)
                        {
                            gen1 = gen1 + list3.get(0);
                        }
                        else if(list3.size() == 2)
                        {
                            gen1 = gen1 + list3.get(0);
                            gen2 = gen2 + list3.get(1);
                        }
                        else if(list3.size() == 3)
                        {
                            gen1 = gen1 + list3.get(0);
                            gen2 = gen2 + list3.get(1);
                            gen3 = gen3 + list3.get(2);
                        }

                        //All 3 stars
//                    String query2 = "select count(*) as count, stars.id, stars.name from stars_in_movies, stars where stars.id = stars_in_movies.starId AND (stars.id = ANY(select stars.id from movies, stars, stars_in_movies where stars.id = stars_in_movies.starId AND stars_in_movies.movieId = movies.id AND movies.id = '";
//                    query2 = query2 + movie_id + "')) group by stars.id order by count DESC,name ASC limit 3";
                        String query2 = "select count(*) as count, stars.id, stars.name from stars_in_movies, stars where stars.id = stars_in_movies.starId AND (stars.id = ANY(select stars.id from movies, stars, stars_in_movies where stars.id = stars_in_movies.starId AND stars_in_movies.movieId = movies.id AND movies.id = ? )) group by stars.id order by count DESC,name ASC limit 3";
                        statement2 = dbcon.prepareStatement(query2);
                        statement2.setString(1,movie_id);
                        temp_startTime = System.currentTimeMillis();
                        rs2 = statement2.executeQuery();
                        temp_endTime = System.currentTimeMillis();
                        Total_JDBC_Time += (temp_endTime-temp_startTime);

                        // insert all stars to list
                        while (rs2.next())
                        {
                            String tempN = rs2.getString("name");
                            String tempID = rs2.getString("id");
                            list1.add(tempN);
                            list2.add(tempID);
                        }
                        //rs2 = null;

                        if(list2.size() == 0)
                        {
                        }
                        else if(list2.size() == 1)
                        {
                            ID1 = ID1 + list2.get(0);
                            star1 = star1 + list1.get(0);
                        }
                        else if(list2.size() == 2)
                        {
                            ID1 = ID1 + list2.get(0);
                            star1 = star1 + list1.get(0);
                            ID2 = ID2 + list2.get(1);
                            star2 = star2 + list1.get(1);
                            star1 = star1 + ", ";
                        }
                        else if(list2.size() == 3)
                        {
                            ID1 = ID1 + list2.get(0);
                            star1 = star1 + list1.get(0);
                            ID2 = ID2 + list2.get(1);
                            star2 = star2 + list1.get(1);
                            ID3 = ID3 + list2.get(2);
                            star3 = star3 + list1.get(2);
                            star1 = star1 + ", ";
                            star2 = star2 + ", ";
                        }

                        //-------------------
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("movie_id", movie_id);
                        jsonObject.addProperty("movie_title", movie_title);
                        jsonObject.addProperty("movie_year", movie_year);
                        jsonObject.addProperty("movie_director", movie_director);
                        //jsonObject.addProperty("movie_genres", movie_genres);
                        jsonObject.addProperty("gen1", gen1);
                        jsonObject.addProperty("gen2", gen2);
                        jsonObject.addProperty("gen3", gen3);
                        jsonObject.addProperty("star1", star1);
                        jsonObject.addProperty("star2", star2);
                        jsonObject.addProperty("star3", star3);
                        jsonObject.addProperty("ID1", ID1);
                        jsonObject.addProperty("ID2", ID2);
                        jsonObject.addProperty("ID3", ID3);
                        jsonObject.addProperty("movie_rating", movie_rating);

                        jsonArray.add(jsonObject);
                    }
                }




                statement.close();
                statement2.close();
                statement3.close();
            }
            //browse
            if(mode.compareTo("2") == 0)
            {

                PreparedStatement statement = null;
                PreparedStatement statement3 = null;
                PreparedStatement statement2 = null;
                int searchFlag = 0; //

                String musicQuery1 = "";
                String musicQuery2 = "";

                String query = null;

                //if a-z
                if(browse.length() == 1)
                {
                    //query = "select * from topmovies where title like '" + browse + "%'";
                    query = "select * from topmovies where title like ?";
                }
                //if *
                else if(browse.compareTo("test") == 0)
                {
                    query = "select * from topmovies where title regexp '^[^a-z0-9]'";
                    searchFlag = 1;
                }
                // by genres
                else if(browse.compareTo("Music") == 0)
                {
                    searchFlag = 2;
                    query="select * from (select * from topmovies where topmovies.genres like '%music%' and topmovies.genres not like '%musical%' UNION ALL select * from topmovies where topmovies.genres like '%music,musical%')topmovies";
//                    musicQuery1 = "select * from topmovies where topmovies.genres like '%music%' and topmovies.genres not like '%musical%'";
//                    musicQuery2 = "select * from topmovies where topmovies.genres like '%music,musical%'";
                }
                else
                {
                    searchFlag = 3;
                    //query = "select * from topmovies where topmovies.genres like '%" + browse + "%'";
                    query = "select * from topmovies where topmovies.genres like ?";
                }

                //calculate total result and total page number
                String resultCount = "select count(*) as count from (" + query + ") as t";

                statement3 = dbcon.prepareStatement(resultCount);
                if(searchFlag == 0)
                {
                    statement3.setString(1,  browse + "%");
                }
                else if(searchFlag == 1)
                {
                }
                else if(searchFlag == 2)
                {
                }
                else if(searchFlag == 3)
                {
                    statement3.setString(1, "%" + browse + "%");
                }



                rs3 = statement3.executeQuery();


                String tempRN = "";
                while (rs3.next())
                {
                    tempRN = rs3.getString("count");
                }

                int num_result = Integer.valueOf(tempRN);
                int Single_Page = Integer.valueOf(num);

                int Total_page = num_result / Single_Page;
                if( (num_result % Single_Page) != 0)
                    Total_page++;




                //--------------------------------------
                if(sort.compareTo("1") == 0)
                {
                    query = query + " order by topmovies.rating DESC, topmovies.title DESC";
                }
                else if(sort.compareTo("2") == 0)
                {
                    query = query + " order by topmovies.rating ASC, topmovies.title ASC";
                }
                else if(sort.compareTo("3") == 0)
                {
                    query = query + " order by topmovies.title DESC, topmovies.rating DESC";
                }
                else if(sort.compareTo("4") == 0)
                {
                    query = query + " order by topmovies.title ASC, topmovies.rating ASC";
                }
                else if(sort.compareTo("5") == 0)
                {
                    query = query + " order by topmovies.rating ASC, topmovies.title DESC";
                }
                else if(sort.compareTo("6") == 0)
                {
                    query = query + " order by topmovies.rating DESC, topmovies.title ASC";
                }
                else if(sort.compareTo("7") == 0)
                {
                    query = query + " order by topmovies.title ASC, topmovies.rating DESC";
                }
                else if(sort.compareTo("8") == 0)
                {
                    query = query + " order by topmovies.title DESC, topmovies.rating ASC";
                }

                if(num.compareTo("10") == 0)
                {
                    query = query + " limit 10";
                }
                else if(num.compareTo("25") == 0)
                {
                    query = query + " limit 25";
                }
                else if(num.compareTo("50") == 0)
                {
                    query = query + " limit 50";
                }
                else if(num.compareTo("100") == 0)
                {
                    query = query + " limit 100";
                }
                else if(num.compareTo("20") == 0)
                {
                    query = query + " limit 20";
                }

                //edit offset
                Integer currentPage = (Integer)session.getAttribute("pageNumber");
                //calculate correct current page
                //prev
                if(page.compareTo("1") == 0)
                {
                    currentPage = currentPage - 1;
                    if (currentPage < 1)
                        currentPage = 1;
                }
                //next
                else if(page.compareTo("2") == 0)
                {
                    currentPage = currentPage + 1;
                    if(currentPage > Total_page)
                        currentPage = Total_page;
                }

                //update page number
                session.setAttribute("pageNumber", currentPage);

                //----------------
                int offset = 0;
                if(currentPage == 1)
                {

                }
                else if(currentPage <= Total_page)
                {
                    offset = (currentPage - 1) * Single_Page;
                    query = query + " offset " + offset;
                }
                else if(currentPage > Total_page)
                {
                    offset = (Total_page - 1) * Single_Page;
                    query = query + " offset " + offset;
                }

                statement = dbcon.prepareStatement(query);
                rs = null;

                if(searchFlag == 0)
                {
                    statement.setString(1,  browse + "%");
                }
                else if(searchFlag == 1)
                {
                }
                else if(searchFlag == 2)
                {
                }
                else if(searchFlag == 3)
                {
                    statement.setString(1, "%" + browse + "%");
                }


                rs = statement.executeQuery();
                rs2 = null;

                while (rs.next())
                {
                    rs2 = null;
                    ArrayList list1 = new ArrayList();
                    ArrayList list2 = new ArrayList();
                    ArrayList list3 = new ArrayList();

                    String movie_id = rs.getString("id");
                    String movie_title = rs.getString("title");
                    String movie_year = rs.getString("year");
                    String movie_director = rs.getString("director");
                    String movie_genres = rs.getString("genres");
                    String movie_rating = rs.getString("rating");


                    //assign string
                    String star1 = "";
                    String star2 = "";
                    String star3 = "";
                    String ID1 = "";
                    String ID2 = "";
                    String ID3 = "";
                    String gen1 = "";
                    String gen2 = "";
                    String gen3 = "";

                    //split gen
                    String tempGen = movie_genres.substring(0, movie_genres.length());
                    String[] split = tempGen.split(",");
                    for (String tempGen2 : split) {
                        list3.add(tempGen2);
                    }

                    //insert gens
                    if(list3.size() == 0)
                    {
                    }
                    else if(list3.size() == 1)
                    {
                        gen1 = gen1 + list3.get(0);
                    }
                    else if(list3.size() == 2)
                    {
                        gen1 = gen1 + list3.get(0);
                        gen2 = gen2 + list3.get(1);
                    }
                    else if(list3.size() == 3)
                    {
                        gen1 = gen1 + list3.get(0);
                        gen2 = gen2 + list3.get(1);
                        gen3 = gen3 + list3.get(2);
                    }

                    //All 3 stars
//                    String query2 = "select count(*) as count, stars.id, stars.name from stars_in_movies, stars where stars.id = stars_in_movies.starId AND (stars.id = ANY(select stars.id from movies, stars, stars_in_movies where stars.id = stars_in_movies.starId AND stars_in_movies.movieId = movies.id AND movies.id = '";
//                    query2 = query2 + movie_id + "')) group by stars.id order by count DESC,name ASC limit 3";
                    String query2 = "select count(*) as count, stars.id, stars.name from stars_in_movies, stars where stars.id = stars_in_movies.starId AND (stars.id = ANY(select stars.id from movies, stars, stars_in_movies where stars.id = stars_in_movies.starId AND stars_in_movies.movieId = movies.id AND movies.id = ? )) group by stars.id order by count DESC,name ASC limit 3";
                    statement2 = dbcon.prepareStatement(query2);
                    statement2.setString(1,movie_id);
                    rs2 = statement2.executeQuery();

                    // insert all stars to list
                    while (rs2.next())
                    {
                        String tempN = rs2.getString("name");
                        String tempID = rs2.getString("id");
                        list1.add(tempN);
                        list2.add(tempID);
                    }
                    //rs2 = null;

                    if(list2.size() == 0)
                    {
                    }
                    else if(list2.size() == 1)
                    {
                        ID1 = ID1 + list2.get(0);
                        star1 = star1 + list1.get(0);
                    }
                    else if(list2.size() == 2)
                    {
                        ID1 = ID1 + list2.get(0);
                        star1 = star1 + list1.get(0);
                        ID2 = ID2 + list2.get(1);
                        star2 = star2 + list1.get(1);
                        star1 = star1 + ", ";
                    }
                    else if(list2.size() == 3)
                    {
                        ID1 = ID1 + list2.get(0);
                        star1 = star1 + list1.get(0);
                        ID2 = ID2 + list2.get(1);
                        star2 = star2 + list1.get(1);
                        ID3 = ID3 + list2.get(2);
                        star3 = star3 + list1.get(2);
                        star1 = star1 + ", ";
                        star2 = star2 + ", ";
                    }

                    //-------------------
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("movie_id", movie_id);
                    jsonObject.addProperty("movie_title", movie_title);
                    jsonObject.addProperty("movie_year", movie_year);
                    jsonObject.addProperty("movie_director", movie_director);
                    //jsonObject.addProperty("movie_genres", movie_genres);
                    jsonObject.addProperty("gen1", gen1);
                    jsonObject.addProperty("gen2", gen2);
                    jsonObject.addProperty("gen3", gen3);
                    jsonObject.addProperty("star1", star1);
                    jsonObject.addProperty("star2", star2);
                    jsonObject.addProperty("star3", star3);
                    jsonObject.addProperty("ID1", ID1);
                    jsonObject.addProperty("ID2", ID2);
                    jsonObject.addProperty("ID3", ID3);
                    jsonObject.addProperty("movie_rating", movie_rating);

                    jsonArray.add(jsonObject);
                }
                statement.close();
                statement2.close();
                statement3.close();


            }



            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

            rs.close();
            rs2.close();
            rs3.close();


            dbcon.close();
            //response.setHeader("refresh", "3");
        } catch (Exception e) {
            //e.printStackTrace();

            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // set reponse status to 500 (Internal Server Error)
            response.setStatus(500);

        }
        out.close();

        long Servlet_endTime = System.currentTimeMillis();
        Total_Servlet_Time = Servlet_endTime - Servlet_startTime;

        Singleton.getInstance().writeToFile("TS=" + Total_Servlet_Time);
        Singleton.getInstance().writeToFile("TJ=" + Total_JDBC_Time);



    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        this.doGet(request,response);

    }

}