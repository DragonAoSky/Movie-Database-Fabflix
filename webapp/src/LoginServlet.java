import com.google.gson.JsonObject;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.jasypt.util.password.StrongPasswordEncryptor;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {

    public String getServletInfo() {
        return "Servlet connects to MySQL database and displays result of a SELECT";
    }


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JsonObject responseJsonObject = new JsonObject();

        PrintWriter out = response.getWriter();
        String username = null;
        String password = null;
        String FinalUsername = null;
        String FinalPW = null;
        String customerID = null;
        int admin = 0;

        //start anay request
        String agent=request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(agent);
        Browser browser = userAgent.getBrowser();
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        //System.out.println("Device Type:"+operatingSystem.getDeviceType());
        String deviceType = operatingSystem.getDeviceType().toString();
        //System.out.println(deviceType);

        //if not mobile, do recap
        if(!deviceType.equals("MOBILE"))
        {
            //recap
            String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
            int checkcap = -1;

            try {
                checkcap = RecaptchaVerifyUtils.verify(gRecaptchaResponse);
            } catch (Exception e) { }

            //success
            if(checkcap == 1)
            {

            }
            //fail
            else if(checkcap == 0)
            {

                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "Please complete the robot check");
                out.write(responseJsonObject.toString());
                out.close();
                return;
            }
            //error
            else if(checkcap == -1)
            {

                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "Error! Recap does not work!");
                out.write(responseJsonObject.toString());
                out.close();
                return;
            }
        }




        try
        {
            customerID = null;
            boolean success = false;

            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/moviedb");
            Connection dbcon = ds.getConnection();

            PreparedStatement statement = null;
            ResultSet rs = null;

            username = request.getParameter("username");
            password = request.getParameter("password");

            if(username.equals("") || password.equals(""))
            {
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "Please enter your Username/Password");
            }
            //normal login
            else
            {
                String query1 = null;
                if(username.equals("classta@email.edu"))
                {
                    query1 = "select employees.* from employees where email = ?";
                    admin = 1;
                }
                else
                    query1 = "select customers.* from customers where email = ?";
                statement = dbcon.prepareStatement(query1);
                statement.setString(1, username);

                //check username
                rs = statement.executeQuery();

                if (rs.next())
                {
                    String encryptedPassword = rs.getString("password");
                    //if is not admin
                    if(admin == 0)
                        customerID = rs.getString("id");
                    success = new StrongPasswordEncryptor().checkPassword(password, encryptedPassword);

                    //success!
                    if(success)
                    {
                        //is admin
                        if(admin == 1)
                        {
                            request.getSession().setAttribute("admin", "admin");
                        }
                        // set this user into the session
                        if(admin == 0)
                            request.getSession().setAttribute("customerID", customerID);    //user ID
                        else
                            request.getSession().setAttribute("customerID", "000000"); //admin special ID
                        request.getSession().setAttribute("user", new User(username));  //user email

                        if(admin == 1)
                        {
                            responseJsonObject.addProperty("status", "admin");
                            responseJsonObject.addProperty("message", "success");
                        }
                        else
                        {
                            responseJsonObject.addProperty("status", "success");
                            responseJsonObject.addProperty("message", "success");
                        }


                        //start insert metadata
                        DatabaseMetaData dbmd = dbcon.getMetaData();
                        List<String> tablename = new ArrayList<String>();
                        List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
                        ResultSet tableRS = null;
                        ResultSet colRS = null;
                        tableRS = dbmd.getTables(null,"moviedb","%",null);
                        //start get table
                        while(tableRS.next()) {
                            //Map<String, Object> map = new HashMap<String, Object>();
                            String temp_Table_name = tableRS.getString("TABLE_NAME");
                            //map.put("Table name", rs.getString("TABLE_NAME"));
                            tablename.add(temp_Table_name);
                        }

                        //start get columns in table
                        for(String tableName: tablename)
                        {
                            colRS = dbmd.getColumns(null, "moviedb", tableName, "%");
                            while(colRS.next())
                            {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("tableName", tableName);
                                map.put("COLUMN_NAME", colRS.getString("COLUMN_NAME"));
                                map.put("TYPE_NAME", colRS.getString("TYPE_NAME"));
                                map.put("COLUMN_SIZE", colRS.getString("COLUMN_SIZE"));
                                map.put("NULLABLE", colRS.getString("NULLABLE"));
                                tempList.add(map);
                                //System.out.println(map.get("Table name"));
                                //System.out.println(map);
                            }
                        }

                        //insert into session
                        request.getSession().setAttribute("tablename", tablename);
                        request.getSession().setAttribute("colname", tempList);

                        //close rs
                        tableRS.close();
                        colRS.close();
                    }
                    //failed
                    else
                    {
                        // Login fail
                        responseJsonObject.addProperty("status", "fail");
                        responseJsonObject.addProperty("message", "Your password is incorrect"); }
                    }
                else
                {
                    // Login fail
                    responseJsonObject.addProperty("status", "fail");
                    responseJsonObject.addProperty("message", "user " + username + " doesn't exists");
                }
            }



            rs.close();
            statement.close();
            dbcon.close();
        } catch (Exception e) {

        }







        out.write(responseJsonObject.toString());
        out.close();
        /* This example only allows username/password to be test/test
        /  in the real project, you should talk to the database to verify username/password
        */







    }
}
