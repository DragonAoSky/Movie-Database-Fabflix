
import java.io.*;
import java.sql.*;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class newCast extends DefaultHandler {

    //write to txt
    private static String filenameTemp;
    private static String filenameTemp2;

    private CastMovie tempMovie;
    List<CastMovie> tempMovieList;
    List<CastMovie> DeleteMovieList;

    private String tempVal;
    private List<String> Alldirctor;
    private List<String> DeleteDirctor;
    private List<Integer> DeleteIndex;
    //private List<Integer> DeleteIndex2;

    String directorname = null;

    //to maintain context
    int changetitle = 0;
    int unknownStar = 0;
    int changedir = 0;
    int sameMOVIE = 0;

    //------------------------------------
    Map<String, String> Moviemap;
    Map<String, String> Starmap;
    Map<String, String> SIMmap;
    List<CastMovie> SecondCheckList;
    List<CastMovie> ThirdCheckList;




    //-----------------------------------

    public newCast() {
        tempMovieList = new ArrayList<CastMovie>();
        DeleteMovieList = new ArrayList<CastMovie>();
        Alldirctor = new ArrayList<String>();
        DeleteDirctor = new ArrayList<String>();
        DeleteIndex = new ArrayList<Integer>();
        //---------------------
        Moviemap = new HashMap<String, String>();
        Starmap = new HashMap<String, String>();
        SIMmap = new HashMap<String, String>();
        SecondCheckList = new ArrayList<CastMovie>();
        ThirdCheckList = new ArrayList<CastMovie>();
    }

    public void runExample() throws Exception {
        parseDocument();
//        System.out.println("No of movies '" + tempMovieList.size() + "'.");
//        System.out.println("Size of Dir '" + Alldirctor.size() + "'.");
        getData();
        System.out.println("No of movies '" + Moviemap.size() + "'.");
        System.out.println("No of star '" + Starmap.size() + "'.");
        System.out.println("No of SIM '" + SIMmap.size() + "'.");
        //reduceDup();
        //reduceDup2();
        insertdata();
        //printData();
    }

    private void parseDocument() {

        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("casts124.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Iterate through the list and print
     * the contents
     */
    private void printData() throws IOException {



        Iterator<CastMovie> it = tempMovieList.iterator();
        while (it.hasNext()) {
            String message = it.next().toString();
            writeTest(message);
        }
    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if(qName.equalsIgnoreCase("filmc"))
        {
            tempMovie = new CastMovie();
            changetitle = 1;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
        if(tempVal.equals("sa") || tempVal.equals("s a"))
            unknownStar = 1;
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("dirfilms")) {
            //add it to the list


        } else if (qName.equalsIgnoreCase("filmc")) {

            tempMovie.setDirector(directorname);
            tempMovie.reduceDUP();
            tempMovieList.add(tempMovie);
            sameMOVIE = 0;

        } else if (qName.equalsIgnoreCase("t")) {

            if (changetitle == 1)
            {
                List<CastMovie> DeleteList = new ArrayList<CastMovie>();
                for(CastMovie m: tempMovieList)
                {
                    String title = m.getTitle();
                    String director = m.getDirector();
                    //find same movie!
                    if(director.equals(directorname) && title.equals(tempVal))
                    {
                        sameMOVIE = 1;
                        tempMovie.setDirector(m.getDirector());
                        tempMovie.setTitle(m.getTitle());
                        tempMovie.setStar(m.getStar());
                        DeleteList.add(m);
                        changetitle = 0;
                        break;
                    }
                }
                  if(sameMOVIE == 0)
                  {
                      tempMovie.setTitle(tempVal);
                      changetitle = 0;
                  }
                  else
                  {
                      tempMovieList.removeAll(DeleteList);
                  }
            }
        } else if (qName.equalsIgnoreCase("a")) {

            if(unknownStar == 0)
                tempMovie.addStar(tempVal);
            else
                unknownStar = 0;
        } else if (qName.equalsIgnoreCase("is")) {
            directorname = tempVal;
            Alldirctor.add(directorname);
        }

    }

    public void reduceDup () throws Exception
    {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection connection = DriverManager.getConnection("jdbc:" + Parameters.dbtype + ":///" + Parameters.dbname + "?autoReconnect=true&useSSL=false",
                Parameters.username, Parameters.password);

        if (connection != null) {
            System.out.println("Start reduce duplicate of director!");
            System.out.println();
        }

        //----------------------------
        CallableStatement cs = null;
        connection.setAutoCommit(false);
        cs = connection.prepareCall("{call test (?)}");
        int[] iNoRows = null;

        for(String d: Alldirctor)
        {
            cs.setString(1, d);
            cs.addBatch();
        }

        iNoRows = cs.executeBatch();
        connection.commit();

        int index = 0;

        for (int i: iNoRows)
        {
            //already exist
            if(i == -1)
            {
                String tempDelDirector = Alldirctor.get(index);
                DeleteDirctor.add(tempDelDirector);
            }
            index++;
        }

        index = 0;
        //start delete director
        for(CastMovie m: tempMovieList)
        {
            String dir = m.getDirector();
            for(String DelDir: DeleteDirctor)
            {
                if(dir.equals(DelDir))
                {
                    DeleteIndex.add(index);
                    break;
                }
            }
            index++;
        }
        Collections.sort(DeleteIndex,Collections.reverseOrder());

        //remove it from our index
        for( int i: DeleteIndex)
        {
            tempMovieList.remove(i);
        }

        System.out.println("Size of DeleteDir '" + DeleteDirctor.size() + "'.");
        System.out.println("Size of Dir after deleted '" + Alldirctor.size() + "'.");
        System.out.println("Size of tempMovieList after delete director '" + tempMovieList.size() + "'.");


        cs.close();
        connection.close();
    }


    public static boolean creatTxtFile(String name) throws IOException {
        boolean flag = false;
        filenameTemp =  name;
        File filename = new File(filenameTemp);
        if (!filename.exists()) {
            filename.createNewFile();
            flag = true;
        }
        return flag;
    }

    public static boolean writeTxtFile(String newStr) throws IOException {
        boolean flag = false;
        String filein = newStr + "\r\n";
        String temp = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            File file = new File(filenameTemp);
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buf = new StringBuffer();

            for (int j = 1; (temp = br.readLine()) != null; j++) {
                buf = buf.append(temp);
                buf = buf.append(System.getProperty("line.separator"));
            }
            buf.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buf.toString().toCharArray());
            pw.flush();
            flag = true;
        } catch (IOException e1) {
            throw e1;
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return flag;
    }


    public static boolean createTest(String name) throws IOException {
        boolean flag = false;
        filenameTemp2 =  name;
        File filename = new File(filenameTemp2);
        if (!filename.exists()) {
            filename.createNewFile();
            flag = true;
        }
        return flag;
    }
    public static boolean writeTest(String newStr) throws IOException {
        boolean flag = false;
        String filein = newStr + "\r\n";
        String temp = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            File file = new File(filenameTemp2);
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buf = new StringBuffer();

            for (int j = 1; (temp = br.readLine()) != null; j++) {
                buf = buf.append(temp);
                buf = buf.append(System.getProperty("line.separator"));
            }
            buf.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buf.toString().toCharArray());
            pw.flush();
            flag = true;
        } catch (IOException e1) {
            throw e1;
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return flag;
    }

    public static boolean isNumeric(String str){
        for(int i=str.length();--i>=0;){
            int chr=str.charAt(i);
            if(chr<48 || chr>57)
                return false;
        }
        return true;
    }

    public void insertdata_old () throws IOException, Exception
    {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection connection = DriverManager.getConnection("jdbc:" + Parameters.dbtype + ":///" + Parameters.dbname + "?autoReconnect=true&useSSL=false",
                Parameters.username, Parameters.password);
        //ResultSet rs = null;
        CallableStatement cs = null;
        String return_response = null;

        if (connection != null) {
            System.out.println("Connection established!!");
            System.out.println();
        }

        connection.setAutoCommit(false);
        cs = connection.prepareCall("{call CAST (?,?,?)}");
        int[] iNoRows=null;

        List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();

        for(CastMovie m: tempMovieList)
        {
            String title = m.getTitle();
            String director = m.getDirector();
            List<String> stars = m.getStar();
            for(String s: stars)
            {
                cs.setString(1,title);
                cs.setString(2,s);
                cs.setString(3,director);
                cs.addBatch();

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("director", director);
                map.put("title", title);
                map.put("star", s);
                tempList.add(map);
            }
        }


        iNoRows=cs.executeBatch();
        connection.commit();

        int index  = 0;

        for (int i: iNoRows)
        {
            if(i == -1)
            {
                Map<String, Object> map = tempList.get(index);
                String director = (String) map.get("director");
                String title = (String) map.get("title");
                String star = (String) map.get("star");
                String message = "The relationship between movie " + title + " and director " + director + " and star " + star + " insert failed";
                writeTxtFile(message);
            }
            index++;
        }


        //rs.close();
        cs.close();
        connection.close();
        System.out.println("Finished Casting!!");
    }


    public void insertdata () throws IOException, Exception
    {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection connection = DriverManager.getConnection("jdbc:" + Parameters.dbtype + ":///" + Parameters.dbname + "?autoReconnect=true&useSSL=false",
                Parameters.username, Parameters.password);
        PreparedStatement cs = null;

        if (connection != null) {
            System.out.println("Start insert data!");
            System.out.println();
        }
        connection.setAutoCommit(false);
        String query = "insert into stars_in_movies(starId,movieId) values(?,?)";
        cs = connection.prepareStatement(query);

        //check if movie exist
        for(CastMovie m: tempMovieList)
        {
            String checkkey = "";
            String director = m.getDirector();
            String title = m.getTitle();
            checkkey = title + "-" + director;
            String check = Moviemap.get(checkkey);
            //movie does not exist!
            if(check == null)
            {
                writeTxtFile("Movie " + title + " does not exist!");
            }
            else
            {
                m.setId(check);
                SecondCheckList.add(m);
            }
        }

        System.out.println("No of movie after 1st check '" + SecondCheckList.size() + "'.");
        //check star and SIM
        for (CastMovie m: SecondCheckList)
        {
            String title = m.getTitle();
            String id = m.getId();
            List<String> tempStarList = new ArrayList<>();

            String starlist = SIMmap.get(id);




            List<String> star = m.getStar();

            //start check star and SIM
            for(String s: star)
            {
                String starID = Starmap.get(s);
                if(starID == null)
                {
                    writeTxtFile("Star " + s + " does not exist!");
                }
                //star exist
                else
                {
                    //in sim movie does not exist!
                    if(starlist == null)
                    {
                        cs.setString(1,starID);
                        cs.setString(2,id);
                        cs.addBatch();
                    }
                    else
                    {
                        String[] split = starlist.split(",");
                        for (String tempGen2 : split) {
                            tempStarList.add(tempGen2);
                        }

                        //SIM exist!
                        if(tempStarList.contains(starID))
                        {
                            writeTxtFile("The relationship between movie " + title + " and star " + s + " already exist!");
                        }
                        //SIM does not exist!
                        else
                        {
                            cs.setString(1,starID);
                            cs.setString(2,id);
                            cs.addBatch();
                        }
                    }


                }

            }
        }
        int[] iNoRows=null;
        iNoRows=cs.executeBatch();
        connection.commit();




        cs.close();
        connection.close();

    }

    public void getData () throws IOException, Exception{

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection connection = DriverManager.getConnection("jdbc:" + Parameters.dbtype + ":///" + Parameters.dbname + "?autoReconnect=true&useSSL=false",
                Parameters.username, Parameters.password);

        if (connection != null) {
            System.out.println("Start get data!");
            System.out.println();
        }

        PreparedStatement statement1 = null;
        PreparedStatement statement2 = null;
        PreparedStatement statement3 = null;
        ResultSet rs = null;
        //ResultSet rs = null;
        //ResultSet rs = null;
        String query1 = "select name, id from stars";
        String query2 = "select id,substring_index(group_concat(title,'-',director), ',', 100) as combine from movies group by id";
        String query3 = "select movieID , substring_index(group_concat(distinct starID SEPARATOR ','), ',', 100) as star from stars_in_movies group by movieID";

        statement1 = connection.prepareStatement(query1);
        statement2 = connection.prepareStatement(query2);
        statement3 = connection.prepareStatement(query3);

        //get all stars
        rs = statement1.executeQuery();
        while(rs.next())
        {
            String tempSname = rs.getString("name");
            String tempSID = rs.getString("id");
            Starmap.put(tempSname,tempSID);
        }
        rs = null;
        rs = statement2.executeQuery();
        while(rs.next())
        {
            String tempSname = rs.getString("combine");
            String tempSID = rs.getString("id");
            Moviemap.put(tempSname,tempSID);
        }
        rs = null;
        rs = statement3.executeQuery();
        while (rs.next())
        {
            String tempSname = rs.getString("star");
            String tempSID = rs.getString("movieID");
            SIMmap.put(tempSID,tempSname);
        }


        rs.close();
        statement1.close();
        statement2.close();
        statement3.close();
        connection.close();
    }


    public void reduceDup2 () throws Exception
    {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection connection = DriverManager.getConnection("jdbc:" + Parameters.dbtype + ":///" + Parameters.dbname + "?autoReconnect=true&useSSL=false",
                Parameters.username, Parameters.password);

        if (connection != null) {
            System.out.println("Start reduce duplicate of Movie!");
            System.out.println();
        }

        //----------------------------
        //CallableStatement cs = null;
        //connection.setAutoCommit(false);
        //cs = connection.prepareCall("{call test2 (?,?)}");
        PreparedStatement cs = null;
        ResultSet rs = null;
        String query = "select id from movies where title = ? and director = ? limit 1";
        //int[] iNoRows = null;
        cs = connection.prepareStatement(query);

        int index = 0;
        for(CastMovie m: tempMovieList)
        {
            rs = null;
            String director = m.getDirector();
            String title = m.getTitle();
            cs.setString(1, title);
            cs.setString(2, director);
            //cs.addBatch();
            rs = cs.executeQuery();
            if(rs.next())
            {
                String id = rs.getString("id");
                m.setId(id);
            }
            else
            {
                DeleteMovieList.add(m);
            }
        }

        //iNoRows = cs.executeBatch();
        //connection.commit();

//        index = 0;
//        for (int i: iNoRows)
//        {
//            //already exist
//            if(i == -1)
//            {
//                CastMovie tempM = new CastMovie();
//                tempM = tempMovieList.get(index);
//                //tempM =
//                //D.add(tempDelDirector);
//                DeleteMovieList.add(tempM);
//            }
//            index++;
//        }

        System.out.println("Size of DeleteMovieList '" + DeleteMovieList.size() + "'.");
        tempMovieList.removeAll(DeleteMovieList);
        System.out.println("Size of Movie list after deleted '" + tempMovieList.size() + "'.");

        rs.close();
        cs.close();
        connection.close();
    }



    public static void main(String[] args) throws Exception {
        creatTxtFile("inconsistent_cast.txt");
        createTest("output_cast.txt"); //--------------------
        newCast spe = new newCast();
        spe.runExample();
    }

}
