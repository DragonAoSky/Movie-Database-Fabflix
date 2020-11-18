import java.util.ArrayList;
import java.util.List;

public class Movie {
    private  String title;
    private int year;
    private String director;
    private List<String> genres;

    public Movie() {
        this.title = "";
        this.year = 0;
        this.director = "";
        this.genres = new ArrayList<String>();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getGenreSize()
    {
        int size =genres.size();
        return size;
    }

    public void addgenres(String g) {
        if(g.equals("Susp") || g.equals("susp"))
        {
            genres.add("Thriller");
        }
        else if(g.equals("CnR") || g.equals("CmR") || g.equals("CnRb") || g.equals("CnRbb") || g.equals("Crim"))
        {
            genres.add("Crime");
        }
        else if(g.equals("Dram") || g.equals("Draam") || g.equals("DRam") || g.equals("Dram>") || g.equals("Dramn") || g.equals("DRAM") || g.equals("dram") )
        {
            genres.add("Drama");
        }
        else if(g.equals("West") || g.equals("West1") )
        {
            genres.add("Western");
        }
        else if(g.equals("S.F.") || g.equals("ScFi") || g.equals("SciF") || g.equals("Sctn") || g.equals("SxFi") || g.equals("Scfi")) //
        {
            genres.add("Sci-Fi");
        }
        else if(g.equals("Advt"))
        {
            genres.add("Adventure");
        }
        else if(g.equals("Horr") || g.equals("Hor"))
        {
            genres.add("Horror");
        }
        else if(g.equals("Romt") || g.equals("romt"))
        {
            genres.add("Romance");
        }
        else if(g.equals("Comd") || g.equals("Comdx") || g.equals("Cond") || g.equals("comd"))
        {
            genres.add("Comedy");
        }
        else if(g.equals("Musc") || g.equals("musc") || g.equals("Muusc") || g.equals("stage musical") || g.equals("Muscl"))
        {
            genres.add("Musical");
        }
        else if(g.equals("Docu"))
        {
            genres.add("Documentary");
        }
        else if(g.equals("Docu Dram") || g.equals("Dram Docu") || g.equals("Dramd") ) //
        {
            genres.add("Documentary");
            genres.add("Drama");
        }
        else if(g.equals("Porn"))
        {
            genres.add("Adult");
        }
        else if(g.equals("Noir") || g.equals("black") || g.equals("noir"))
        {
            genres.add("Black");
        }
        else if(g.equals("BioP") || g.equals("BioB") || g.equals("BioG") || g.equals("Biop") || g.equals("BioPx")  || g.equals("Bio"))
        {
            genres.add("Biography");
        }
        else if(g.equals("TV"))
        {
            genres.add("Reality-TV");
        }
        else if(g.equals("TVs"))
        {
            genres.add("TV series");
        }
        else if(g.equals("Myst") || g.equals("Mystp"))
        {
            genres.add("Mystery");
        }
        else if(g.equals("TVm"))
        {
            genres.add("TV miniseries");
        }
        else if(g.equals("Ctxx") || g.equals("Ctxxx") || g.equals("Ctcxx") || g.equals("txx"))
        {
            genres.add("Uncategorized");
        }
        else if(g.equals("Actn") || g.equals("Act") || g.equals("Adct") || g.equals("Adctx") || g.equals("Viol") || g.equals("actn"))
        {
            genres.add("Violence");
        }
        else if(g.equals("Camp"))
        {
            genres.add("Now-Camp");
        }
        else if(g.equals("Disa") || g.equals("Disaster")) //Dist
        {
            genres.add("Disaster");
        }
        else if(g.equals("Epic"))
        {
            genres.add("Epic");
        }
        else if(g.equals("Cart"))
        {
            genres.add("Animation");
        }
        else if(g.equals("Faml"))
        {
            genres.add("Family");
        }
        else if(g.equals("Surl") || g.equals("Fant") || g.equals("Surr") || g.equals("surreal") || g.equals("fant")) //Fant
        {
            genres.add("Fantasy");
        }
        else if(g.equals("AvGa"))
        {
            genres.add("Avant Garde");
        }
        else if(g.equals("Hist"))
        {
            genres.add("History");
        }
        else if(g.equals("'homoerotic'") || g.equals("Homo"))
        {
            genres.add("Homoerotic");
        }
        else if(g.equals("Comd Noir") || g.equals("Noir Comd"))
        {
            genres.add("Comedy");
            genres.add("Black");
        }
        else if(g.equals("Comd West"))
        {
            genres.add("Comedy");
            genres.add("Western");
        }
        else if(g.equals("Dram.Actn"))
        {
            genres.add("Drama");
            genres.add("Action");
        }
        else if(g.equals("Noir Comd Romt"))
        {
            genres.add("Comedy");
            genres.add("Black");
            genres.add("Romance");
        }
        else if(g.equals("Romt Comd") || g.equals("Romt. Comd")) //
        {
            genres.add("Comedy");
            genres.add("Romance");
        }
        else if(g.equals("Romt Dram"))
        {
            genres.add("Drama");
            genres.add("Romance");
        }
        else if(g.equals("Romt Actn"))
        {
            genres.add("Action");
            genres.add("Romance");
        }
        else if(g.equals("Romt Fant"))
        {
            genres.add("Fantasy");
            genres.add("Romance");
        }
        else if(g.equals("RomtAdvt"))
        {
            genres.add("Adventure");
            genres.add("Romance");
        }
        else if(g.equals("sports"))
        {
            genres.add("Sport");
        }
        else if(g.equals("UnDr") || g.equals("underground"))
        {
            genres.add("Underground");
        }
        else
        {
            genres.add(g);
        }
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}
