
import java.io.*;
import java.sql.*;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class StarParse extends DefaultHandler implements Parameters{

    //write to txt
    private static String filenameTemp;
    private static String filenameTemp2;

    int checkname = 0;
    int checkboa = 0;

    private List<Star> myStars;

    private String tempVal;

    //to maintain context
    private Star tempStar;

    public StarParse() {
        myStars = new ArrayList<Star>();
    }

    public void runExample() throws Exception {
        parseDocument();
        printData();
        reduceDup();
        insertdata();
    }

    private void parseDocument() {

        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("actors63.xml", this);

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

        System.out.println("No of Stars '" + myStars.size() + "'.");

//        Iterator<Star> it = myStars.iterator();
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
        if (qName.equalsIgnoreCase("actor")) {
            //create a new instance of employee
            tempStar = new Star();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("actor")) {
            //add it to the list
            if(checkboa != 1 && checkname != 1)
                myStars.add(tempStar);
            else
            {
                String error = "";
                if(checkboa == 1)
                    error += "Actor " + tempStar.getName() + "'s BOA is invalid. ";
                if(checkname == 1)
                    error += "This actor does not have name. ";
                try {
                    writeTxtFile(error);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                checkboa = 0;
                checkname = 0;
            }

        } else if (qName.equalsIgnoreCase("stagename")) {
            if(tempVal == null || tempVal.equals(""))
            {
                checkname = 1;
            }
            else
                tempStar.setName(tempVal);
        } else if (qName.equalsIgnoreCase("dob")) {
            String tempDOB = tempVal;
            if(tempVal == null || tempVal.equals(""))
            {

            }
            else if(isNumeric(tempDOB))
            {
                tempStar.setBoa(Integer.parseInt(tempVal));
            }
            else
            {
                checkboa = 1;
            }
        }

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
        cs = connection.prepareCall("{call insert_new_star(?, ?)}");
        int[] iNoRows=null;
        connection.setAutoCommit(false);

        List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();

        for(Star s: myStars)
        {
            int tempboa = s.getBoa();
            String name = s.getName();
            String boa = Integer.toString(tempboa);
            //check boa exist
            if(boa.equals("0"))
                boa = null;
            cs.setString(1,name);
            cs.setString(2,boa);
            cs.addBatch();

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("star", name);
            tempList.add(map);
            //rs = cs.executeQuery();
//            if(rs.next())
//            {
//                return_response = rs.getString("answer");
//                if(return_response.equals("no"))
//                {
//                    String error = name + " already exist!";
//                    writeTxtFile(error);
//                }
//            }
        }

        iNoRows=cs.executeBatch();
        connection.commit();

        int index  = 0;
        for (int i: iNoRows)
        {
            if(i == -1)
            {
                Map<String, Object> map = tempList.get(index);
                String star = (String) map.get("star");
                String message = "The insertion of star " + star + " failed.";
                writeTxtFile(message);
            }
            index++;
        }



        cs.close();
        connection.close();
        System.out.println("Finished insertion of stars!!");

    }

    public void reduceDup()
    {
        Set<Star> set = new HashSet<>(myStars);
        myStars.clear();
        myStars.addAll(set);
    }

    public static void main(String[] args) throws IOException, Exception {

        creatTxtFile("inconsistent_star.txt");
        createTest("output_star.txt"); //--------------------
        StarParse spe = new StarParse();
        spe.runExample();

        //------------------------------------------------
        // Connect to the test database

    }

}
