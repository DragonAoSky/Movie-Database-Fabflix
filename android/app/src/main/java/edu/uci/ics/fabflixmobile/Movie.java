package edu.uci.ics.fabflixmobile;

public class Movie {
    private String name;
    private String director;
    private String id;
    private String genre;
    private String stars;
    private String rating;
    private String year;

    public Movie(String name, String year,String director,String genre,String stars,String id,String rating) {
        this.name = name;
        this.year = year;
        this.director = director;
        this.genre = genre;
        this.stars = stars;
        this.id = id;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }
}