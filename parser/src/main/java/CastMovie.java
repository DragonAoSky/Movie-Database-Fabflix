import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CastMovie {
    private String title;
    private String director;
    private List<String> star;
    private String id;

    public CastMovie() {
        this.title = "";
        this.director = "";
        this.star = new ArrayList<String>();
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getStar() {
        return star;
    }

    public void setStar(List<String> star) {
        this.star = star;
    }

    public void addStar(String s)
    {
        star.add(s);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void reduceDUP()
    {
        List<String> listWithoutDup = new ArrayList<String>(new HashSet<String>(star));
        setStar(listWithoutDup);
    }

    public int getStarsize()
    {
        int size = star.size();
        return size;
    }

    public String help()
    {
        String temp = "";
        if(star.size() == 0)
        {
            return "";
        }
        else
        {

                //String temptitle = m.getTitle();
                temp += "Movie title: " + title +"\n";
                temp += "ID is: " + id + "\n";
                temp += "Director: " + director + "\n";
                temp += "Stars: ";
                for(String s: star)
                {
                    temp += s +" | ";
                }
                temp += "\n";
                temp += "\n";

        }

        return temp;
    }

    @Override
    public String toString()
    {
        return (help());
    }
}
