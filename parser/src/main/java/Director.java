import java.util.ArrayList;
import java.util.List;

public class Director {
    private String name;
    private List<Movie> movie;

    public Director() {
        this.name = "";
        this.movie = new ArrayList<Movie>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Movie> getMovie() {
        return movie;
    }

    public void setMovie(List<Movie> movie) {
        this.movie = movie;
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

            for(Movie m: movie)
            {
                String temptitle = m.getTitle();
                int tempyear = m.getYear();
                temp += "Movie title: " + temptitle +"\n";
                temp += "Year: " + tempyear + "\n";
                temp += "Director: " + name + "\n";
                temp += "Genres: ";
                List<String> templist =  m.getGenres();
                for(String s: templist)
                {
                    temp += s +" ";
                }
                temp += "\n";
            }
        }

        return temp;
    }

    public String test()
    {
        String testoutput = "";
        testoutput = testoutput + "Director name is: " + name + ". ";
        int size = movie.size();
        testoutput = testoutput + "He has " + size + " movies";

        return testoutput;
    }

    public int getMoviesize()
    {
        int size = -1;
        size = movie.size();
        return size;
    }

    @Override
    public String toString()
    {
        return (help());
    }

}
