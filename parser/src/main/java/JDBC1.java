import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBC1 implements Parameters {

    public static void main(String[] arg) throws Exception {

        // Incorporate mySQL driver
        Class.forName("com.mysql.jdbc.Driver").newInstance();

        // Connect to the test database
        Connection connection = DriverManager.getConnection("jdbc:" + Parameters.dbtype + ":///" + Parameters.dbname + "?autoReconnect=true&useSSL=false",
                Parameters.username, Parameters.password);

        if (connection != null) {
            System.out.println("Connection established!!");
            System.out.println();
        }

        //----------------------------
//        DatabaseMetaData dbmd = connection.getMetaData();
//        List<String> tablename = new ArrayList<String>();
//        List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
//        ResultSet tableRS = null;
//        ResultSet colRS = null;
//        tableRS = dbmd.getTables(null,"moviedb","%",null);
//
//        //start get table
//        while(tableRS.next()) {
//            //Map<String, Object> map = new HashMap<String, Object>();
//            String temp_Table_name = tableRS.getString("TABLE_NAME");
//            //map.put("Table name", rs.getString("TABLE_NAME"));
//            tablename.add(temp_Table_name);
//        }
//
//        //start get columns in table
//        for(String tableName: tablename)
//        {
//            colRS = dbmd.getColumns(null, "moviedb", tableName, "%");
//            while(colRS.next())
//            {
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("tableName", tableName);
//                map.put("COLUMN_NAME", colRS.getString("COLUMN_NAME"));
//                map.put("TYPE_NAME", colRS.getString("TYPE_NAME"));
//                map.put("COLUMN_SIZE", colRS.getString("COLUMN_SIZE"));
//                map.put("NULLABLE", colRS.getString("NULLABLE"));
//                tempList.add(map);
//                System.out.println(map.get("tableName"));
//                System.out.println(map);
//            }
//        }

        //---------------------------

//        Map<checkMovies, String> Moviemap;
//        Moviemap = new HashMap<checkMovies, String>();
//
//        checkMovies temp = new checkMovies();
//        checkMovies temp2 = new checkMovies();
//        String check = null;
//        temp.setDirector("1");
//        temp.setId("1");
//        temp.setTitle("1");
//        Moviemap.put(temp,"2");
//        temp2.setDirector("1");
//        temp2.setId("1");
//        temp2.setTitle("1");
//        //Moviemap.put(temp2,"3");
//        check = Moviemap.get(temp2);
//
//        System.out.println(check);

        List<CastMovie> tempMovieList = new ArrayList<CastMovie>();

        CastMovie temp1 = new CastMovie();
        CastMovie temp2 = new CastMovie();

        temp1.setId("333");
        temp2.setId("444");
        tempMovieList.add(temp1);
        tempMovieList.add(temp2);

        for(CastMovie m: tempMovieList)
        {
            String id = m.getId();
            System.out.println(id);
            m.setId("000");
        }

        for(CastMovie m: tempMovieList)
        {
            String id = m.getId();
            System.out.println(id);
            //m.setId("000");
        }









        //-----------------


        // Get metatdata from stars; print # of attributes in table
//        System.out.println("The results of the query");
//        ResultSetMetaData metadata = result.getMetaData();
//        System.out.println("There are " + metadata.getColumnCount() + " columns");
//
//        // Print type of each attribute
//        for (int i = 1; i <= metadata.getColumnCount(); i++)
//            System.out.println("Type of column " + i + " is " + metadata.getColumnTypeName(i));
//
//        // print table's contents, field by field
//        while (result.next()) {
//            System.out.println("Id = " + result.getString("id"));
//            System.out.println("Name = " + result.getString("name"));
//            System.out.println("birthYear = " + result.getInt("birthYear"));
//            System.out.println();
//        }




    }
}
