package edu.uci.ics.fabflixmobile;

public class Star {
    private String name;
    private String id;
    private String count;

    public Star( String name,String id, String count) {
        this.name = name;
        this.id = id;
        this.count=count;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
