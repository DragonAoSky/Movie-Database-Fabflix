public class Star {
    private String name;
    private int boa;

    public Star(String name, int boa) {
        this.name = name;
        this.boa = boa;
    }

    public Star() {
        boa = 0;
        name = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBoa() {
        return boa;
    }

    public void setBoa(int boa) {
        this.boa = boa;
    }

    @Override
    public String toString()
    {
        String message = "";
        message += "Star name: " + name + "\tBOA is: " + boa;
        return (message);
    }
}
