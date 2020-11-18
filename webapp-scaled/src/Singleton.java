import java.io.*;

public class Singleton {
    private static final Singleton inst= new Singleton();

    private Singleton() {
        super();
    }

    public synchronized void writeToFile(String newStr) throws IOException {
        // Do whatever
        String filein = newStr + "\r\n";
        String temp = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            File file = new File("time_used.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
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

    }

    public static Singleton getInstance() {
        return inst;
    }

}