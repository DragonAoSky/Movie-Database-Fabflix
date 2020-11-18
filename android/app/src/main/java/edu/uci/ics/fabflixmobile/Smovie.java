package edu.uci.ics.fabflixmobile;

import java.util.ArrayList;
import java.util.List;

public class Smovie {

    private List<Star> starList;
    private String title;
    private String year;
    private String director;
    private String rating;
    private String genres;

    public Smovie() {
        this.starList = new ArrayList<>();
        this.title = "";
        this.year = "";
        this.director = "";
        this.genres = "";
        this.rating = "";
    }

    public List<Star> getStarList() {
        return starList;
    }

    public void setStarList(List<Star> starList) {
        this.starList = starList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void addStar(Star s)
    {
        starList.add(s);
    }
}
