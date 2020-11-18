
import java.io.*;
import java.sql.*;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class Cast extends DefaultHandler {

    //write to txt
    private static String filenameTemp;
    private static String filenameTemp2;

    List<CastDir> myDir;

    private String tempVal;

    //to maintain context
    private CastDir tempDir;
    private CastMovie tempMovie;
    List<CastMovie> tempMovieList;

    int changetitle = 0;
    int unknownStar = 0;

    public Cast() {
        myDir = new ArrayList<CastDir>();
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

        System.out.println("No of Director '" + myDir.size() + "'.");

//        Iterator<CastDir> it = myDir.iterator();
//        while (it.hasNext()) {
//            //System.out.println(it.next().toString());
//            String message = it.next().toString();
//            writeTest(message);
//        }
    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("dirfilms")) {
            //create a new instance of employee
            tempDir = new CastDir();
            tempMovieList = new ArrayList<>();
        }
        else if(qName.equalsIgnoreCase("filmc"))
        {
            tempMovie = new CastMovie();
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
            tempDir.setMovie(tempMovieList);
            int size = tempDir.getMoviesize();
            if(size > 0)
            {
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

        } else if (qName.equalsIgnoreCase("filmc")) {
            changetitle = 0;
            int size = tempMovie.getStarsize();
            if(size > 0)
            {
                List<String> l1 = new ArrayList<String>(tempMovie.getStar());
                List<String> listWithoutDup = new ArrayList<String>(new HashSet<String>(l1));
                tempMovie.setStar(listWithoutDup);
                tempMovieList.add(tempMovie);
            }


        } else if (qName.equalsIgnoreCase("t")) {
            if (changetitle == 0)
            {
                tempMovie.setTitle(tempVal);
                changetitle = 1;
            }
        } else if (qName.equalsIgnoreCase("a")) {

            if(unknownStar == 0)
                tempMovie.addStar(tempVal);
            else
                unknownStar = 0;
        } else if (qName.equalsIgnoreCase("is")) {

            tempDir.setName(tempVal);
        }

    }

    public void insertdata () throws IOException, Exception
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

        for(CastDir d: myDir)
        {
            String director = d.getName();
            List<CastMovie> movie = d.getMovie();

            for(CastMovie m: movie)
            {
                List<String> star = m.getStar();
                String title = m.getTitle();

                for(String s: star)
                {
                    //rs = null;
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

    public void reduceDup() {

        //a whole new DIR
        List<CastDir> templistDIR = new ArrayList<CastDir>(myDir);

        for(CastDir D: myDir)
        {
            //DIR we want to delete
            List<CastDir> deleteDirList = new ArrayList<CastDir>();

            String myDirName = D.getName();

            for(CastDir tempD: templistDIR)
            {
                String tempName = tempD.getName();
                //if dir have same name
                if(tempName.equals(myDirName))
                {
                    deleteDirList.add(tempD);
                    //start compare all movies with same director
                    List<CastMovie> myDirMovieList = new ArrayList<CastMovie>(D.getMovie());
                    List<CastMovie> tempDirMovieList = new ArrayList<CastMovie>(tempD.getMovie());
                    List<CastMovie> deleteMovieList= new ArrayList<CastMovie>();

                    for(CastMovie myDirMovie:  myDirMovieList)
                    {
                        String myDirTitle = myDirMovie.getTitle();


                    }

                }
            }
        }


    }

    public static void main(String[] args) throws Exception {
        creatTxtFile("inconsistent_cast.txt");
        createTest("output_cast.txt"); //--------------------
        Cast spe = new Cast();
        spe.runExample();
    }

}
