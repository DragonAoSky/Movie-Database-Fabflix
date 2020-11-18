import java.util.ArrayList;
import java.util.List;

public class runparser implements Parameters{

    public static void main(String[] arg) throws Exception {

        long startTime = System.currentTimeMillis();
        StarParse.main(null);
        MovieParse.main(null);
        newCast.main(null);
        System.out.println("All 3 parser finished!");
        long endTime = System.currentTimeMillis();
        System.out.println("Total runtime is: " + (endTime - startTime) + "ms");

        //---------test------------
//        String test = statictest.getQuery();
//        System.out.println(test);
//        statictest.setQuery("2nd");
//        test = statictest.getQuery();
//        System.out.println(test);
    }

}
