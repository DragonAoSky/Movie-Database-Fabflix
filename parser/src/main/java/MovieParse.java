
import java.io.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class MovieParse extends DefaultHandler {

    //write to txt
    private static String filenameTemp;
    private static String filenameTemp2;


    List<Director> myDir;

    private String tempVal;

    //to maintain context
    private Director tempDir;
    private Movie tempMovie;
    List<Movie> tempMovieList;

    int nktFlag = 0;
    int yearErrorFlag = 0;
    String tempFid = "";

    public MovieParse() {
        myDir = new ArrayList<Director>();
    }

    public void runExample() throws Exception {
        parseDocument();
        printData();
        insertdata();
    }

    private void parseDocument() {

        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("mains243.xml", this);

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

        System.out.println("Number of director '" + myDir.size() + "'.");

//        Iterator<Director> it = myDir.iterator();
//        while (it.hasNext()) {
//            //System.out.println(it.next().toString());
//
//            String message = it.next().toString();
//            writeTest(message);
//
//        }
    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("directorfilms")) {
            tempDir = new Director();
            tempMovieList = new ArrayList<Movie>();
        }
        else if(qName.equalsIgnoreCase("film"))
        {
            tempMovie = new Movie();
            String tempDirname = tempDir.getName();
            tempMovie.setDirector(tempDirname);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
        if(tempVal.equals("NKT"))
            nktFlag = 1;
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("directorfilms")) {
            //add it to the list
            int size = tempMovieList.size();
            if(size > 0)
            {
                tempDir.setMovie(tempMovieList);
                myDir.add(tempDir);
            }
            else
            {
                String name = tempDir.getName();
                String message = "The Director " + name + " does not have any movie!";
                try {
                    writeTxtFile(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        } else if (qName.equalsIgnoreCase("dirname")) {
            tempDir.setName(tempVal);
        } else if (qName.equalsIgnoreCase("t")) {
                tempMovie.setTitle(tempVal);
        } else if (qName.equalsIgnoreCase("year")) {
            String checknumber = tempVal;
            //check year is correct or not
            if(isNumeric(checknumber))
            {
                tempMovie.setYear(Integer.parseInt(tempVal));
            }
            else
            {
                yearErrorFlag = 1;
            }

        } else if (qName.equalsIgnoreCase("film")) {
            //check if genre exist and year is correct
            int checkyear = tempMovie.getYear();
            int checkgenre = tempMovie.getGenreSize();
            if(nktFlag != 1 && checkyear > 0 && checkgenre > 0 && yearErrorFlag != 1)
                tempMovieList.add(tempMovie);
            else
            {
                String errormessage = "";
                errormessage += "Invalid Moive! Movie ID: " + tempFid + ". ";
                if(checkyear <= 0)
                    errormessage += "Year " + checkyear +  " is invalid. ";
                if(checkgenre == 0)
                    errormessage += "Number of Genre is 0. ";
                if(yearErrorFlag == 1)
                    errormessage += "Year is incorrect. ";
                if(nktFlag == 1)
                    errormessage += "Title is unknown";
                try {
                    writeTxtFile(errormessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                nktFlag = 0;
                yearErrorFlag = 0;
            }

        } else if (qName.equalsIgnoreCase("cat")) {
            tempMovie.addgenres(tempVal);
        }
        else if (qName.equalsIgnoreCase("cattext")) {
            tempMovie.addgenres(tempVal);
        }
        else if (qName.equalsIgnoreCase("fid")) {
            tempFid = tempVal;
        }

    }

    public void insertdata () throws IOException, Exception
    {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection connection = DriverManager.getConnection("jdbc:" + Parameters.dbtype + ":///" + Parameters.dbname + "?autoReconnect=true&useSSL=false",
                Parameters.username, Parameters.password);

        CallableStatement cs = null;
        String return_response = null;

        if (connection != null) {
            System.out.println("Connection established!!");
            System.out.println();
        }

        //start insert
        cs = connection.prepareCall("{call insert_new_movie(?,?,?,?)}");
        connection.setAutoCommit(false);
        int[] iNoRows=null;
        List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();


        for(Director d: myDir)
        {

            List<Movie> movie = d.getMovie();
            for(Movie m: movie)
            {
                String title = m.getTitle();
                int tempyear = m.getYear();
                String year = Integer.toString(tempyear);
                String director = m.getDirector();
                List<String> genre = m.getGenres();

                for(String g: genre)
                {
                    cs.setString(1,title);
                    cs.setString(2,director);
                    cs.setString(3,year);
                    cs.setString(4,g);
                    cs.addBatch();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("director", director);
                    map.put("title", title);
                    tempList.add(map);
//                    rs = cs.executeQuery();
//                    if(rs.next())
//                    {
//                        return_response = rs.getString("answer");
//                        if(!return_response.equals("yes"))
//                            writeTxtFile(return_response);
//                    }
                }
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
                String message = "The movie " + title + " with director " + director + " insert failed";
                writeTxtFile(message);
            }
            index++;
        }


        cs.close();
        connection.close();
        System.out.println("Finished insertion of movies!!");

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



    public static void main(String[] args) throws Exception {
        creatTxtFile("inconsistent_movie.txt");
        createTest("output_movie.txt"); //--------------------
        MovieParse spe = new MovieParse();
        spe.runExample();
    }



}
