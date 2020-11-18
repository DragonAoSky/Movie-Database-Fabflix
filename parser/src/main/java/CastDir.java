import java.util.ArrayList;
import java.util.List;

public class CastDir {

    private String name;
    private List<CastMovie> movie;

    public CastDir() {
        this.name = "";
        this.movie = new ArrayList<CastMovie>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CastMovie> getMovie() {
        return movie;
    }

    public void setMovie(List<CastMovie> movie) {
        this.movie = movie;
    }

    public int getMoviesize()
    {
        int size = -1;
        size = movie.size();
        return size;
    }

    public String help()
    {
        String temp = "";
        if(movie.size() == 0)
        {
            return "";
        }
        else
        {
            for(CastMovie m: movie)
            {
                String temptitle = m.getTitle();
                temp += "Movie title: " + temptitle +"\n";
                temp += "Director: " + name + "\n";
                temp += "Stars: ";
                List<String> templist =  m.getStar();
                for(String s: templist)
                {
                    temp += s +" | ";
                }
                temp += "\n";
                temp += "\n";
            }
        }

        return temp;
    }

    @Override
    public String toString()
    {
        return (help());
    }





}
